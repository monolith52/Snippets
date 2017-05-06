package columnexample;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Arrays;

public class Case1 extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        primaryStage.setScene(new Scene(root, 300, 120));
        primaryStage.show();

        TableView<String[]> tableView = new TableView<>();
        TableColumn<String[], String> column1 = new TableColumn<>("column1");
        TableColumn<String[], String> column2 = new TableColumn<>("column2");
        TableColumn<String[], String> column3 = new TableColumn<>("column3");
        column1.setCellValueFactory(features -> new SimpleStringProperty(features.getValue()[0]));
        column3.setCellValueFactory(features -> new SimpleStringProperty(features.getValue()[3]));
        tableView.getColumns().addAll(Arrays.asList(column1, column2, column3));

        TableColumn<String[], String> column2_1 = new TableColumn<>("column2-1");
        TableColumn<String[], String> column2_2 = new TableColumn<>("column2-2");
        column2_1.setCellValueFactory(features -> new SimpleStringProperty(features.getValue()[1]));
        column2_2.setCellValueFactory(features -> new SimpleStringProperty(features.getValue()[2]));
        column2.getColumns().addAll(Arrays.asList(column2_1, column2_2));

//        TableColumn<String[], String> column2_2_1 = new TableColumn<>("column2-1");
//        TableColumn<String[], String> column2_2_2 = new TableColumn<>("column2-2");
//        column2_1.setCellValueFactory(features -> new SimpleStringProperty(features.getValue()[1]));
//        column2_2.setCellValueFactory(features -> new SimpleStringProperty(features.getValue()[2]));
//        column2_2.getColumns().addAll(column2_2_1, column2_2_2);

        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.add(new String[]{"Value11", "Value12-2", "Value12-2", "Value13"});
        data.add(new String[]{"Value21", "Value22-2", "Value22-2", "Value23"});
        data.add(new String[]{"Value31", "Value32-2", "Value22-2", "Value33"});
        tableView.setItems(data);

        root.setCenter(tableView);
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}