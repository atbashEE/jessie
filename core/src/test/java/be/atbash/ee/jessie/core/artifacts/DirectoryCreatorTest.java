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
package be.atbash.ee.jessie.core.artifacts;

import be.atbash.ee.jessie.core.model.JessieMaven;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */

public class DirectoryCreatorTest {

    private DirectoryCreator creator;

    @Before
    public void setup() {
        creator = new DirectoryCreator() {
            @Override
            public void createDirectory(String directoryPath) {

            }

            @Override
            public void removeDirectory(String directoryPath) {

            }
        };
    }

    @Test
    public void createPathForGroupAndArtifact() {
        JessieMaven model = new JessieMaven();
        model.setGroupId("be.atbash.ee.jessie");
        model.setArtifactId("demo");
        String result = creator.createPathForGroupAndArtifact(model);
        assertThat(result).isEqualTo("be/atbash/ee/jessie/demo");

    }

}