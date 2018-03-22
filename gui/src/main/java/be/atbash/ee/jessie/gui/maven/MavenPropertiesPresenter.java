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
package be.atbash.ee.jessie.gui.maven;

import be.atbash.ee.jessie.core.model.ModuleStructure;
import be.atbash.ee.jessie.gui.specification.SpecificationView;
import be.atbash.ee.jessie.gui.util.ComboBoxUtil;
import be.atbash.ee.jessie.gui.util.StageUtil;
import be.atbash.ee.jessie.gui.util.ValidationUtil;
import be.atbash.ee.jessie.gui.validation.ComboBoxValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 */
public class MavenPropertiesPresenter implements Initializable {

    private ValidationSupport validationSupport;

    @FXML
    private TextField groupId;

    @FXML
    private TextField artifactId;

    @FXML
    private ComboBox<ModuleStructure> moduleStructure;

    @FXML
    private Button nextButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        validationSupport = new ValidationSupport();
        validationSupport.registerValidator(groupId, Validator.createEmptyValidator("groupId is required"));
        validationSupport.registerValidator(artifactId, Validator.createEmptyValidator("artifactId is required"));

        // We need to register the validator first and then the items.
        // Adding the items will trigger validation and thus indication of error when starting the screen.
        validationSupport.registerValidator(moduleStructure, new ComboBoxValidator("structure is required").getValidator());
        ComboBoxUtil.initializeItems(moduleStructure, ModuleStructure.class);

        nextButton.disableProperty().bindBidirectional(ValidationUtil.initializeFeedback(validationSupport, groupId, artifactId, moduleStructure));
    }

    public void next() {

        if (!validationSupport.isInvalid()) {

            SpecificationView specificationView = new SpecificationView();

            Stage stage = (Stage) nextButton.getScene().getWindow();
            StageUtil.changeScene(stage, specificationView);

        }
    }
}