package customtable;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.*;
import javafx.scene.paint.Color;
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

        tableView.getItems().add(new Entity("Name1", "Item1", true, Color.RED));
        tableView.getItems().add(new Entity("Name2", "Item2", true, Color.BLUE));
        tableView.getItems().add(new Entity("Name3", "Item3", false, Color.GREEN));
        tableView.getItems().add(new Entity("Name4", "FreeText", false, Color.ORANGE));

        tableView.setEditable(true);
        column1.setCellFactory(column -> new TextFieldTableCell<>(new DefaultStringConverter()));
        column2.setCellFactory(column -> new ChoiceBoxTableCell<>(new DefaultStringConverter(),
                "Item1", "Item2", "Item3"));
//        column2.setCellFactory(column -> new ComboBoxTableCell<>(new DefaultStringConverter(),
//               "Item1", "Item2", "Item3"));
        column3.setCellFactory(column -> new CheckBoxTableCell<>());
//        column4.setCellFactory(column -> new ColorPickerTableCell<>());
        column4.setCellFactory(column -> new DirectColorPickerTableCell<>());
    }
}
