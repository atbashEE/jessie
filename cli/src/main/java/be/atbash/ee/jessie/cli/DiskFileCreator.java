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

import be.atbash.ee.jessie.core.artifacts.FileCreator;
import be.atbash.ee.jessie.core.exception.TechnicalException;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;

/**
 *
 */
@ApplicationScoped
public class DiskFileCreator extends FileCreator {

    public void writeContents(String directory, String fileName, String contents) {
        File file = new File(directory, fileName);
        try {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(contents);
            }
        } catch (IOException e) {
            throw new TechnicalException(e);
        }

    }

    public void writeContents(String directory, String fileName, byte[] contents) {
        File file = new File(directory, fileName);
        try {
            try (OutputStream writer = new FileOutputStream(file)) {
                writer.write(contents);
            }
        } catch (IOException e) {
            throw new TechnicalException(e);
        }

    }
}
