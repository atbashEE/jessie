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

import com.airhacks.afterburner.views.FXMLView;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 */

public class StageUtil {

    private static String applicationCssURI;

    public static void initialScene(Stage stage, FXMLView fxmlView, String cssURI) {
        applicationCssURI = cssURI;

        Scene scene = new Scene(fxmlView.getView());
        scene.getStylesheets().add(cssURI);

        stage.setScene(scene);
        stage.setResizable(false);
    }

    public static void changeScene(Stage stage, FXMLView fxmlView) {
        Scene scene = new Scene(fxmlView.getView());

        scene.getStylesheets().add(applicationCssURI);

        stage.setScene(scene);
        stage.sizeToScene();
    }

}
