package tableexample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Case2 extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        primaryStage.setScene(new Scene(root, 300, 120));
        primaryStage.show();

        TableView<File> tableView = new TableView<>();
        TableColumn<File, String> column1 = new TableColumn<>("filename");
        TableColumn<File, Number> column2 = new TableColumn<>("filesize");
        TableColumn<File, LocalDate> column3 = new TableColumn<>("updatetime");
        column1.setCellValueFactory(features -> new SimpleStringProperty(features.getValue().getName()));
        column2.setCellValueFactory(features -> new SimpleLongProperty(features.getValue().length()));
        column3.setCellValueFactory(features -> new SimpleObjectProperty<>(
                Instant.ofEpochMilli(features.getValue().lastModified()).atZone(ZoneId.systemDefault()).toLocalDate()));
        tableView.getColumns().addAll(column1, column2, column3);

        ObservableList<File> data = FXCollections.observableArrayList();
        data.add(new File(getClass().getResource("photo1.jpg").getPath()));
        data.add(new File(getClass().getResource("photo2.jpg").getPath()));
        data.add(new File(getClass().getResource("photo3.jpg").getPath()));
        tableView.setItems(data);

        tableView.setEditable(true);
        column1.setEditable(true);
        column1.setCellFactory(column -> new TextFieldTableCell<File, String>(new DefaultStringConverter()){
            @Override
            public void commitEdit(String newValue) {
                super.commitEdit(newValue);
                File file = column.getTableView().getItems().get(getIndex());
                // ここで列に対応したItemに好きな値をセットできます
                // file.setSomeValue( newValue )
            }
        });


        root.setCenter(tableView);
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}