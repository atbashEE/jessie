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
package be.atbash.ee.jessie.spi;

import be.atbash.ee.jessie.core.artifacts.DirectoryCreator;
import be.atbash.ee.jessie.core.artifacts.FileCreator;
import be.atbash.ee.jessie.core.artifacts.MavenCreator;
import be.atbash.ee.jessie.core.files.ThymeleafEngine;
import be.atbash.ee.jessie.core.model.JessieModel;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

/**
 *
 */

public abstract class AbstractAddon implements JessieAddon {

    protected Map<String, String> options;
    protected Map<String, String> defaultOptions;

    @Inject
    protected ThymeleafEngine thymeleafEngine;

    @Inject
    protected DirectoryCreator directoryCreator;

    @Inject
    protected FileCreator fileCreator;

    @Override
    public final void addonOptions(Map<String, String> options) {
        this.options = options;
    }

    protected final String getWebDirectory(JessieModel model) {
        return model.getDirectory() + "/" + MavenCreator.SRC_MAIN_WEBAPP;
    }

    protected final String getJavaApplicationRootPackage(JessieModel model) {
        return MavenCreator.SRC_MAIN_JAVA + "/" + directoryCreator.createPathForGroupAndArtifact(model.getMaven());
    }

    protected final void processTemplateFile(String directory, String templateFileName, String fileName, Set<String> alternatives, Map<String, String> variables) {
        String javaFile = thymeleafEngine.processFile(templateFileName, alternatives, variables);
        fileCreator.writeContents(directory, fileName, javaFile);
    }

    protected final void processTemplateFile(String directory, String fileName, Set<String> alternatives, Map<String, String> variables) {
        String javaFile = thymeleafEngine.processFile(fileName, alternatives, variables);
        fileCreator.writeContents(directory, fileName, javaFile);
    }

}
