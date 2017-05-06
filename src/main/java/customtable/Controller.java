package customtable;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.*;
import javafx.scene.paint.Color;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.util.Arrays;

public class Controller {

    @FXML
    private TableView<Entity> tableView;
    @FXML
    private TableColumn<Entity, String> column1;
    @FXML
    private TableColumn<Entity, String> column2;
    @FXML
    private TableColumn<Entity, Boolean> column3;
    @FXML
    private TableColumn<Entity, Color> column4;

    @FXML
    private void initialize() {
        column1.setCellValueFactory(new PropertyValueFactory<>("value1"));
        column2.setCellValueFactory(new PropertyValueFactory<>("value2"));
        column3.setCellValueFactory(new PropertyValueFactory<>("value3"));
        column4.setCellValueFactory(new PropertyValueFactory<>("value4"));

        tableView.getItems().addAll(
                new Entity("Name1", "Item1", true, Color.RED),
                new Entity("Name2", "Item2", true, Color.BLUE),
                new Entity("Name3", "Item3", false, Color.GREEN),
                new Entity("Name4", "FreeText", false, Color.ORANGE));

        tableView.setEditable(true);
        column1.setCellFactory(TextFieldTableCell.forTableColumn());

//        column2.setCellFactory(ChoiceBoxTableCell.forTableColumn("Item1", "Item2", "Item3"));
        column2.setCellFactory(ComboBoxTableCell.forTableColumn("Item1", "Item2", "Item3"));
        column3.setCellFactory(CheckBoxTableCell.forTableColumn(column3));
//        column3.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
//        column4.setCellFactory(ColorPickerTableCell.forTableColumn());
        column4.setCellFactory(DirectColorPickerTableCell.forTableColumn());
    }
}
