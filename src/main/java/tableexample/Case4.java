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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Case4 extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        primaryStage.setScene(new Scene(root, 300, 120));
        primaryStage.show();

        TableView<String[]> tableView = new TableView<>();

        final String url = "jdbc:sqlite:C:/project/Snippets/build/resources/main/tableexample/posapp.db";
        try (Connection con = DriverManager.getConnection(url)) {

            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM admin LIMIT 10");

            for (int i=0; i<rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn<String[], String> column = new TableColumn<>(rs.getMetaData().getColumnName(j+1));
                column.setCellValueFactory(features -> new SimpleStringProperty(features.getValue()[j]));
                tableView.getColumns().add(column);
            }

            ObservableList<String[]> list = FXCollections.observableArrayList();
            while (rs.next()) {
                String[] data = new String[rs.getMetaData().getColumnCount()];
                for (int i=0; i<rs.getMetaData().getColumnCount(); i++) {
                    data[i] = rs.getString(i+1);
                }
                list.add(data);
            }
            tableView.setItems(list);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        root.setCenter(tableView);
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}