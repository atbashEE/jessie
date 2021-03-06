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

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.BooleanConverter;

/**
 *
 */
public class CLIParameters {

    @Parameter(names = {"-file", "-f"}, description = "File name containing the Jessie model values YAML file")
    private String jessieModelFile = "jessie.yaml";

    @Parameter(names = {"-noGeneration", "-n"}, description = "When true, do not generates the files for the project."
            , converter = BooleanConverter.class)
    private boolean noGenerationFiles = false;

    public String getJessieModelFile() {
        return jessieModelFile;
    }

    public boolean isNoGenerationFiles() {
        return noGenerationFiles;
    }
}
