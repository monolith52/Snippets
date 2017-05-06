package columnexample;

import javafx.application.Application;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;

public class Case4 extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        primaryStage.setScene(new Scene(root, 300, 120));
        primaryStage.show();

        TableView<File> tableView = new TableView<>();
        TableColumn<File, String> column1 = new TableColumn<>("filename");
        TableColumn<File, Number> column2 = new TableColumn<>("filesize");
        column1.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getName()));
        column2.setCellValueFactory(f -> new SimpleLongProperty(f.getValue().length()));
        tableView.getColumns().addAll(Arrays.asList(column1, column2));

        tableView.setEditable(true);
        column1.setEditable(true);
        column1.setCellFactory(TextFieldTableCell.forTableColumn());
        column1.setOnEditCommit(event -> {
            if (!event.getRowValue().renameTo(new File(event.getNewValue()))) {
                event.consume();
            }
        });

        ObservableList<File> data = FXCollections.observableArrayList();
        data.add(new File("test1.txt"));
        data.add(new File("test2.txt"));
        data.add(new File("test3.txt"));
        tableView.setItems(data);

        root.setCenter(tableView);
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}