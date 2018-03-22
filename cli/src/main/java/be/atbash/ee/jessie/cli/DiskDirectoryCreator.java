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

import be.atbash.ee.jessie.core.artifacts.DirectoryCreator;
import be.atbash.ee.jessie.core.artifacts.JessieArtifactCreationException;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;

/**
 *
 */
@ApplicationScoped
public class DiskDirectoryCreator extends DirectoryCreator {

    public void createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        createDirectory(directory);
    }

    private void createDirectory(File directory) {
        boolean success = directory.mkdirs();
        if (!success) {
            String message = String.format("JES-101- Unable to create directory '%s'", directory.getAbsoluteFile());
            throw new JessieArtifactCreationException(message);
        }
    }

}
