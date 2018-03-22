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
package be.atbash.ee.jessie.cli;

import be.atbash.ee.jessie.core.artifacts.Creator;
import be.atbash.ee.jessie.core.artifacts.DirectoryCreator;
import be.atbash.ee.jessie.core.config.ConfigurationParameter;
import be.atbash.ee.jessie.core.exception.JessieConfigurationException;
import be.atbash.ee.jessie.core.exception.JessieUnexpectedException;
import be.atbash.ee.jessie.core.file.ModelReader;
import be.atbash.ee.jessie.core.model.JessieModel;
import be.atbash.ee.jessie.core.model.ModelManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;

/**
 *
 */
@ApplicationScoped
public class JessieCLI {

    @Inject
    private ConfigurationParameter configurationParameter;

    @Inject
    private ModelReader modelReader;

    @Inject
    private ModelManager modelManager;

    @Inject
    private DirectoryCreator directoryCreator;

    @Inject
    private Creator creator;

    public void generateFromYamlFile(String jessieModelFile) {
        JessieModel model = modelReader.readModel(jessieModelFile);

        modelManager.prepareModel(model, true);

        File rootDirectory = new File(model.getDirectory());
        if (rootDirectory.exists()) {
            String message = String.format("Directory '%s' is not empty. Only empty directories supported", model.getDirectory());
            throw new JessieConfigurationException(message);
        }

        directoryCreator.createDirectory(model.getDirectory());

        storeExpandedConfig(rootDirectory, model);

        if (!configurationParameter.isDoNotGenerateFiles()) {
            creator.createArtifacts(model);
        }
    }

    private void storeExpandedConfig(File rootDirectory, JessieModel model) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            model.setTemplate(null);
            String expandedModel = mapper.writeValueAsString(model);
            FileOutputStream outputStream = new FileOutputStream(new File(rootDirectory, "jessie.yaml"));
            outputStream.write(expandedModel.getBytes());
            outputStream.close();

        } catch (java.io.IOException e) {
            throw new JessieUnexpectedException(e.getMessage());
        }

    }
}
