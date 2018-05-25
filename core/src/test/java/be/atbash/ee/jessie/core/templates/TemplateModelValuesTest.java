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
package be.atbash.ee.jessie.core.templates;

import be.atbash.ee.jessie.core.model.JessieModel;
import be.atbash.ee.jessie.core.model.JessieSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TemplateModelValuesTest {

    @Mock
    private TemplateModelLoader templateModelLoaderMock;

    @InjectMocks
    private TemplateModelValues templateModelValues;

    @Test
    public void applyTemplateValues() {
        JessieModel model = new JessieModel();
        model.setTemplate("minimal");

        JessieModel minimalModel = new JessieModel();
        minimalModel.setSpecification(new JessieSpecification());
        when(templateModelLoaderMock.loadTemplateValues("minimal")).thenReturn(minimalModel);

        templateModelValues.applyTemplateValues(model);

        verify(templateModelLoaderMock).loadTemplateValues(anyString());
    }

}