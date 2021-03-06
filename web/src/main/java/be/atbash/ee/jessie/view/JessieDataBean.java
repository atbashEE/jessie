/*
 * Copyright 2017-2018 Rudy De Busscher (https://www.atbash.be)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.atbash.ee.jessie.view;

import be.atbash.ee.jessie.ZipFileCreator;
import be.atbash.ee.jessie.addon.microprofile.servers.model.MicroprofileSpec;
import be.atbash.ee.jessie.addon.microprofile.servers.model.SupportedServer;
import be.atbash.ee.jessie.core.artifacts.Creator;
import be.atbash.ee.jessie.core.model.*;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@ViewAccessScoped
@Named
public class JessieDataBean implements Serializable {

    @Inject
    private ModelManager modelManager;

    @Inject
    private Creator creator;

    @Inject
    private ZipFileCreator zipFileCreator;

    private String activeScreen;
    private JessieMaven mavenData;
    private String technologyStack;
    private String javaEEVersion;
    private String javaSEVersion;
    private String mpVersion;
    private String supportedServer;
    private String beansxmlMode;

    private TechnologyStack technologyStackType;

    private List<SelectItem> supportedServerItems;
    private List<String> selectedSpecs = new ArrayList<>();
    private List<SelectItem> specs;
    private String selectedSpecDescription;

    private boolean separateProfile;
    private boolean deltaspikeFeature;
    private boolean primefacesFeature;
    private boolean octopusFeature;

    private boolean hasErrors;

    @PostConstruct
    public void init() {
        mavenData = new JessieMaven();
        separateProfile = true;
    }

    public void activePage(String activeScreen) {
        this.activeScreen = activeScreen;
        if ("build".equals(activeScreen)) {
            validateMainValues();
        }
        if ("options".equals(activeScreen)) {
            if (isTechnologyStackTypeMP()) {
                MicroProfileVersion version = MicroProfileVersion.valueFor(mpVersion);

                defineSupportedServerItems(version);
                selectedSpecs.clear();
                defineExampleSpecs(version);
            }
        }
    }

    private void defineExampleSpecs(MicroProfileVersion version) {
        specs = new ArrayList<>();
        selectedSpecs.clear();

        for (MicroprofileSpec microprofileSpec : MicroprofileSpec.values()) {
            if (microprofileSpec.getMpVersions().contains(version)) {
                specs.add(new SelectItem(microprofileSpec.getCode(), microprofileSpec.getLabel()));
                selectedSpecs.add(microprofileSpec.getCode());
            }
        }

        // TODO As long as Helidon doesn't support JWT_Auth
        if (SupportedServer.HELIDON.getName().equals(supportedServer)) {
            selectedSpecs.remove(MicroprofileSpec.JWT_AUTH.getCode());
        }

    }

    private void defineSupportedServerItems(MicroProfileVersion version) {

        supportedServerItems = new ArrayList<>();
        for (SupportedServer supportedServer : SupportedServer.values()) {
            if (supportedServer.getMpVersions().contains(version)) {
                supportedServerItems.add(new SelectItem(supportedServer.getName(), supportedServer.getName()));
            }
        }
        randomizeSupportedServers();
    }

    private void randomizeSupportedServers() {
        Map<Integer, SelectItem> data = null;

        boolean randomValuesOk = false;
        while (!randomValuesOk) {
            Random rnd = new Random();
            try {
                data = supportedServerItems
                        .stream().collect(Collectors.toMap(s -> rnd.nextInt(500),
                                Function.identity()));

                randomValuesOk = true;
            } catch (IllegalStateException e) {
                // We have the same random value, try again.
            }

        }
        supportedServerItems = new ArrayList<>(data.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new))
                .values());

    }

    public String selectedMenuItemStyleClass(String menuItem) {
        return activeScreen.equals(menuItem) ? "activePage" : "";
    }

    public void validateMainValues() {
        hasErrors = false;
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (mavenData.getGroupId() == null || mavenData.getGroupId().trim().isEmpty()) {
            addErrorMessage(facesContext, "groupId is required");
        }

        if (mavenData.getArtifactId() == null || mavenData.getArtifactId().trim().isEmpty()) {
            addErrorMessage(facesContext, "artifactId is required");
        }

        if (technologyStackType == TechnologyStack.JAVA_EE && (javaEEVersion == null || javaEEVersion.trim().isEmpty())) {
            addErrorMessage(facesContext, "Java EE version is required");
        }

        if (javaSEVersion == null || javaSEVersion.trim().isEmpty()) {
            addErrorMessage(facesContext, "Java SE version is required");
        }

        if (technologyStackType == TechnologyStack.MP && "options".equals(activeScreen) && supportedServer.trim().isEmpty()) {
            addErrorMessage(facesContext, "Supported server selection is required");
        }

        if (technologyStackType == TechnologyStack.MP && SupportedServer.HELIDON.getName().equals(supportedServer)) {
            // Make sure JWT auth is not selected for Helidon
            selectedSpecs.remove(MicroprofileSpec.JWT_AUTH.getCode());
        }
    }

    private void addErrorMessage(FacesContext facesContext, String message) {
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
        hasErrors = true;
    }

    public void generateProject() {
        JessieModel model = new JessieModel();
        model.setDirectory(mavenData.getArtifactId());
        JessieMaven mavenModel = new JessieMaven();
        mavenModel.setGroupId(mavenData.getGroupId());
        mavenModel.setArtifactId(mavenData.getArtifactId());
        model.setMaven(mavenModel);

        JessieSpecification specifications = new JessieSpecification();

        if (technologyStackType == TechnologyStack.JAVA_EE) {
            specifications.setJavaEEVersion(JavaEEVersion.valueFor(javaEEVersion));
            specifications.setJavaSEVersion(JavaSEVersion.valueFor(javaSEVersion));
            specifications.setModuleStructure(ModuleStructure.SINGLE);

            List<ViewType> views = new ArrayList<>();
            views.add(ViewType.JSF);

            specifications.setViews(views);

            List<String> addons = new ArrayList<>();
            if (deltaspikeFeature) {
                addons.add("deltaspike");
            }
            if (primefacesFeature) {
                addons.add("primefaces");
            }
            if (octopusFeature) {
                addons.add("octopus");
            }
            model.setAddons(addons);
        } else {
            specifications.setJavaSEVersion(JavaSEVersion.valueFor(javaSEVersion));
            specifications.setModuleStructure(ModuleStructure.SINGLE);

            specifications.setMicroProfileVersion(MicroProfileVersion.valueFor(mpVersion));

            model.getOptions().put("mp.server", new OptionValue(supportedServer));
            model.getOptions().put("mp.specs", new OptionValue(selectedSpecs));
            model.getOptions().put("mp.mergeProfile", new OptionValue(((Boolean)separateProfile).toString()));
        }

        model.setSpecification(specifications);

        model.getOptions().put(BeansXMLMode.OptionName.name, new OptionValue(BeansXMLMode.getValue(beansxmlMode).getMode()));

        modelManager.prepareModel(model, false);
        creator.createArtifacts(model);

        download(zipFileCreator.createArchive());

    }

    private void download(byte[] archive) {
        String fileName = mavenData.getArtifactId() + ".zip";
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        ec.responseReset();
        ec.setResponseContentType("application/zip");
        ec.setResponseContentLength(archive.length);
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try {
            OutputStream outputStream = ec.getResponseOutputStream();

            outputStream.write(archive);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace(); // FIXME
        }

        fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    }

    public String getActiveScreen() {
        return activeScreen;
    }

    public JessieMaven getMavenData() {
        return mavenData;
    }

    public String getTechnologyStack() {
        return technologyStack;
    }

    public void setTechnologyStack(String technologyStack) {
        this.technologyStack = technologyStack;
        technologyStackType = TechnologyStack.valueFor(technologyStack);
        javaSEVersion = null;
        if (technologyStackType == TechnologyStack.MP) {
            javaSEVersion = "1.8";
        }
    }

    public boolean isTechnologyStackTypeJavaEE() {
        return technologyStackType == TechnologyStack.JAVA_EE;
    }

    public boolean isTechnologyStackTypeMP() {
        return technologyStackType == TechnologyStack.MP;
    }

    public String getJavaEEVersion() {
        return javaEEVersion;
    }

    public void setJavaEEVersion(String javaEEVersion) {
        this.javaEEVersion = javaEEVersion;
    }

    public String getJavaSEVersion() {
        return javaSEVersion;
    }

    public void setJavaSEVersion(String javaSEVersion) {
        this.javaSEVersion = javaSEVersion;
    }

    public String getMpVersion() {
        return mpVersion;
    }

    public void setMpVersion(String mpVersion) {
        this.mpVersion = mpVersion;
    }

    public List<SelectItem> getSupportedServerItems() {
        return supportedServerItems;
    }

    public List<SelectItem> getSpecs() {
        return specs;
    }

    public List<String> getSelectedSpecs() {
        return selectedSpecs;
    }

    public void setSelectedSpecs(List<String> selectedSpecs) {
        this.selectedSpecs = selectedSpecs;

        selectedSpecDescription = selectedSpecs.stream()
                .map(s -> MicroprofileSpec.valueFor(s).getLabel())
                .collect(Collectors.joining(", "));

    }

    public String getSelectedSpecDescription() {
        return selectedSpecDescription;
    }

    public String getSupportedServer() {
        return supportedServer;
    }

    public void setSupportedServer(String supportedServer) {
        this.supportedServer = supportedServer;
    }

    public boolean isDeltaspikeFeature() {
        return deltaspikeFeature;
    }

    public void setDeltaspikeFeature(boolean deltaspikeFeature) {
        this.deltaspikeFeature = deltaspikeFeature;
    }

    public boolean isPrimefacesFeature() {
        return primefacesFeature;
    }

    public void setPrimefacesFeature(boolean primefacesFeature) {
        this.primefacesFeature = primefacesFeature;
    }

    public boolean isOctopusFeature() {
        return octopusFeature;
    }

    public void setOctopusFeature(boolean octopusFeature) {
        this.octopusFeature = octopusFeature;
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public boolean isSeparateProfile() {
        return separateProfile;
    }

    public void setSeparateProfile(boolean separateProfile) {
        this.separateProfile = separateProfile;
    }

    public String getBeansxmlMode() {
        return beansxmlMode;
    }

    public void setBeansxmlMode(String beansxmlMode) {
        this.beansxmlMode = beansxmlMode;
    }

    public String getBeansxmlModelDescription() {
        String result;
        switch (BeansXMLMode.getValue(beansxmlMode)) {

            case IMPLICIT:
                result = "No beans.xml file generated (implicit)";
                break;
            case ANNOTATED:
                result = "beans.xml file generated with discovery mode 'annotated'";
                break;
            case ALL:
                result = "beans.xml file generated with discovery mode 'all'";
                break;
            default:
                throw new IllegalArgumentException(String.format("BeansXMLMode '%s' not supported", beansxmlMode));
        }
        return result;
    }
}
