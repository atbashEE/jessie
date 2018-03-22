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

import be.atbash.ee.jessie.core.config.ConfigurationParameter;
import be.atbash.ee.jessie.core.config.ConfigurationParameterProducer;
import be.atbash.ee.jessie.core.exception.JessieException;
import com.beust.jcommander.JCommander;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 *
 */
public class JessieMain {

    public static void main(String[] args) {
        Weld weld = new Weld();

        WeldContainer container = weld.initialize();

        CLIParameters parameters = new CLIParameters();
        new JCommander(parameters, args);

        try {
            ConfigurationParameter configParameters = new ConfigurationParameter();
            configParameters.setDoNotGenerateFiles(parameters.isNoGenerationFiles());

            container.select(ConfigurationParameterProducer.class).get().setConfigurationParameter(configParameters);
            container.select(JessieCLI.class).get().generateFromYamlFile(parameters.getJessieModelFile());

        } catch (JessieException exception) {
            // FIXME Logging
            System.err.println("ERROR : " + exception.getMessage());
        } finally {
            container.shutdown();
        }
    }
}
