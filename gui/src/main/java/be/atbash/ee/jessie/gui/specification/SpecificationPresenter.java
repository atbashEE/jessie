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
package be.atbash.ee.jessie.gui.specification;

import be.atbash.ee.jessie.core.model.JavaEEVersion;
import be.atbash.ee.jessie.core.model.JavaSEVersion;
import be.atbash.ee.jessie.core.model.ViewType;
import be.atbash.ee.jessie.gui.util.ComboBoxUtil;
import be.atbash.ee.jessie.gui.validation.ComboBoxValidator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 *
 */
public class SpecificationPresenter implements Initializable {

    private ValidationSupport validationSupport;

    @FXML
    private ComboBox<JavaEEVersion> javaEEVersion;

    @FXML
    private ComboBox<JavaSEVersion> javaSEVersion;

    @FXML
    private ListView<ViewType> viewTypeItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validationSupport = new ValidationSupport();

        // We need to register the validator first and then the items.
        // Adding the items will trigger validation and thus indication of error when starting the screen.
        validationSupport.registerValidator(javaEEVersion, new ComboBoxValidator("Java EE Version is required").getValidator());
        ComboBoxUtil.initializeItems(javaEEVersion, JavaEEVersion.class);

        validationSupport.registerValidator(javaSEVersion, new ComboBoxValidator("Java SE Version is required").getValidator());
        ComboBoxUtil.initializeItems(javaSEVersion, JavaSEVersion.class);

        viewTypeItems.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        /*
        ValueExtractor.addObservableValueExtractor(
                c -> c instanceof ListView,
                c -> ((ListView)c).selectionModelProperty()
        );
*/
        validationSupport.registerValidator(viewTypeItems, Validator.createPredicateValidator(new Predicate<ObservableList<ViewType>>() {
            @Override
            public boolean test(ObservableList<ViewType> viewTypes) {
                return false;
            }
        }, "test"));
        ComboBoxUtil.initializeItems(viewTypeItems, ViewType.class);

    }

    public void next() {
        System.out.println(viewTypeItems.getSelectionModel().getSelectedItems());
    }

}
