package tableexample;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Case3 extends Application {

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
        column2.setCellValueFactory(features -> new SimpleStringProperty(features.getValue()[1]));
        column3.setCellValueFactory(features -> new SimpleStringProperty(features.getValue()[2]));
        tableView.getColumns().addAll(column1, column2, column3);

        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.add(new String[]{"Value11", "Value12", "Value13"});
        data.add(new String[]{"Value21", "Value22", "Value23"});
        data.add(new String[]{"Value31", "Value32", "Value33"});
        tableView.setItems(data);

        root.setCenter(tableView);
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}