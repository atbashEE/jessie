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

import be.atbash.ee.jessie.core.model.ComboBoxItem;
import com.sun.javafx.collections.ImmutableObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/**
 *
 */

public class ComboBoxUtil {

    public static <T extends ComboBoxItem> void initializeItems(ComboBox<T> comboBox, Class<T> comboItem) {

        comboBox.setItems(new ImmutableObservableList(comboItem.getEnumConstants()));

        comboBox.setButtonCell(new ComboBoxItemListCell());
        comboBox.setCellFactory(param -> new ComboBoxItemListCell());

    }

    public static <T extends ComboBoxItem> void initializeItems(ListView<T> comboBox, Class<T> comboItem) {

        comboBox.setItems(new ImmutableObservableList(comboItem.getEnumConstants()));

        comboBox.setCellFactory(param -> new ComboBoxItemListCell());

    }

    private static class ComboBoxItemListCell<T extends ComboBoxItem> extends ListCell<T> {

        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getLabel());
            }
        }
    }

}
