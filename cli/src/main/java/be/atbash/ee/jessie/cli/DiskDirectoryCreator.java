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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;

/**
 *
 */
@ApplicationScoped
public class DiskDirectoryCreator extends DirectoryCreator {

    private Logger logger = LoggerFactory.getLogger(DiskDirectoryCreator.class);

    public void createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        createDirectory(directory);
    }

    private void createDirectory(File directory) {
        boolean success = true;
        if (!directory.exists()) {
            success = directory.mkdirs();
        }
        if (!success) {
            String message = String.format("JES-101- Unable to create directory '%s'", directory.getAbsoluteFile());
            throw new JessieArtifactCreationException(message);
        }
    }

    public void removeDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        int fileCount = countFiles(directory);
        if (fileCount > 10) {
            throw new JessieSafetyException(String.format("Delete of '%s' would result in deletion of %s files (delete is blocked for safety reasons).", directory, fileCount));
        }
        deleteFolder(directory);
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        logger.warn(String.format("Unable to delete '%s'", file.getAbsolutePath()));
                    }
                }
            }
        }
        boolean deleted = folder.delete();
        if (!deleted) {
            logger.warn(String.format("Unable to delete '%s'", folder.getAbsolutePath()));
        }
    }

    private int countFiles(File folder) {
        int result = 0;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    result += countFiles(file);
                } else {
                    result++;
                }
            }
        }
        return result;
    }
}
