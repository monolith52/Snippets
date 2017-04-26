package scrolltocolumn;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;

public class TableController {
    @FXML
    private TableView<String[]> tableView;

    @FXML
    private void initialize() {
        ObservableList<String[]> list = FXCollections.observableArrayList();
        for (int j=0; j<100; j++) {
            String[] value = new String[30];
            for (int i=0; i<30; i++) {
                value[i] = "value" + i + "_" + j;
            }
            list.add(value);
        }
        tableView.setItems(list);
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        for (int i=0; i<tableView.getColumns().size(); i++) {
            final int j = i;
            @SuppressWarnings("unchecked")
            TableColumn<String[], String> column = (TableColumn)tableView.getColumns().get(j);
            column.setCellValueFactory(feature -> new SimpleStringProperty(feature.getValue()[j]));
        }
//        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        tableView.setOnScrollToColumn(event -> {
//            System.out.println(event);
//        });
//        tableView.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
//            if (KeyCode.A.equals(event.getCode())) {
////                tableView.scrollToColumnIndex(10);
//                ScrollToColumnEventHandler.scrollToColumnIndex(tableView, 10);
//            }
//                });

        tableView.addEventHandler(KeyEvent.KEY_PRESSED, new ScrollToColumnEventHandler(tableView));
    }

}
