package customtable;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class DirectColorPickerTableCell<S> extends TableCell<S, Color> {
    private ColorPicker colorPicker = null;

    @Override
    protected void updateItem(Color item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            if (colorPicker == null) {
                colorPicker = new ColorPicker();
                colorPicker.setOnAction(event -> {
                    S entity = getTableView().getItems().get(getIndex());
                    ObservableValue<Color> ov = getTableColumn().getCellObservableValue(entity);
                    if (ov instanceof WritableValue) {
                        @SuppressWarnings("unchecked")
                        WritableValue<Color> wv = (WritableValue<Color>)ov;
                        wv.setValue(colorPicker.getValue());
                    }
                });
            }
            colorPicker.setValue(item);
            setGraphic(colorPicker);
        }
    }
}
