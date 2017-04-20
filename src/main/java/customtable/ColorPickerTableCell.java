package customtable;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ColorPickerTableCell<S> extends TableCell<S, Color> {
    private ColorPicker colorPicker = null;
    private Pane pane = null;

    public ColorPickerTableCell() {
        setEditable(true);
    }

    @Override
    public void startEdit() {
        if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
        }

        if (colorPicker == null) {
            colorPicker = new ColorPicker();
            colorPicker.setOnAction(event -> {
                if (isEditing()) commitEdit(colorPicker.getValue());
            });
        }

        colorPicker.setValue(getItem());
        super.startEdit();
        setGraphic(colorPicker);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(pane);
    }

    @Override
    protected void updateItem(Color item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            if (pane == null) pane = new Pane();
            pane.setBackground(new Background(new BackgroundFill(item, null, null)));
            setGraphic(pane);
        }
    }
}
