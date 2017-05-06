package customtable;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ColorPickerTableCell<S> extends TableCell<S, Color> {
    private ColorPicker colorPicker;
    private Pane pane;

    public ColorPickerTableCell() {
        setEditable(true);
        pane = new Pane();
        colorPicker = new ColorPicker();
        colorPicker.setOnAction(event -> {
            if (isEditing()) commitEdit(colorPicker.getValue());
        });

    }

    @Override
    public void startEdit() {
        if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
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
            pane.setBackground(new Background(new BackgroundFill(item, null, null)));
            setGraphic(pane);
        }
    }

    public static <S> Callback<TableColumn<S, Color>, TableCell<S, Color>> forTableColumn() {
        return column -> new ColorPickerTableCell<>();
    }
}
