package columnexample;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Arrays;

public class Case2 extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        primaryStage.setScene(new Scene(root, 300, 120));
        primaryStage.show();

        TableView<Integer[]> tableView = new TableView<>();
        TableColumn<Integer[], Number> column1 = new TableColumn<>("column1");
        TableColumn<Integer[], Number> column2 = new TableColumn<>("column2");
        TableColumn<Integer[], Number> column3 = new TableColumn<>("column3");
        column1.setCellValueFactory(f -> new SimpleIntegerProperty(f.getValue()[0]));
        // column2の内容は同じ列の1行目の内容と常に同じ
        column2.setCellValueFactory(f -> column1.getCellObservableValue(f.getValue()));
        // column3の内容は同じ列の1行目と2行目を足した内容と常に同じ
        column3.setCellValueFactory(f -> Bindings.add(
                (ObservableNumberValue)column1.getCellObservableValue(f.getValue()),
                (ObservableNumberValue)column2.getCellObservableValue(f.getValue())
        ));
        tableView.getColumns().addAll(Arrays.asList(column1, column2, column3));

        ObservableList<Integer[]> data = FXCollections.observableArrayList();
        data.add(new Integer[]{100});
        data.add(new Integer[]{200});
        data.add(new Integer[]{300});
        tableView.setItems(data);

        root.setCenter(tableView);
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}