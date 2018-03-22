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
package be.atbash.ee.jessie.gui.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;

/**
 */
public class ValidationUtil {

    public static BooleanProperty initializeFeedback(ValidationSupport validationSupport, Control... controls) {
        for (Control control : controls) {
            Tooltip validationTooltip = new Tooltip("???");
            validationTooltip.getStyleClass().add("validation-tooltip");
            control.setTooltip(validationTooltip);

        }

        BooleanProperty result = new SimpleBooleanProperty(true);
        validationSupport.validationResultProperty().addListener((o, oldValue, newValue) ->
        {
            result.set(!newValue.getErrors().isEmpty());

            Tooltip tooltip;
            if (oldValue != null) {
                for (ValidationMessage validationMessage : oldValue.getErrors()) {
                    tooltip = validationMessage.getTarget().getTooltip();
                    if (tooltip != null) {
                        tooltip.getStyleClass().add("validation-ok");
                    }
                }
            }

            for (ValidationMessage validationMessage : newValue.getErrors()) {
                tooltip = validationMessage.getTarget().getTooltip();
                if (tooltip != null) {
                    tooltip.setText(validationMessage.getText());
                    tooltip.getStyleClass().remove("validation-ok");
                    tooltip.getStyleClass().add("validation-failure");

                }
            }
        });

        return result;

    }
}
