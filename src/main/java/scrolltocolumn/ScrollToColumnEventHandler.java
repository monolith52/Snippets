package scrolltocolumn;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Optional;

/**
 * EventHandler for changing behavior on right or left key for TableView has
 * SelectionModel.setSelectionMode(SelectionMode.SINGLE), SelectionMode.setCellSelectionEnabled(true).
 *
 * <pre>
 * {@code
 * yourTableView.addEventHandler(KeyEvent.KEY_PRESSED, new ScrollToColumnEventHandler(tableView));
 * }</pre>
 */
class ScrollToColumnEventHandler implements EventHandler<KeyEvent> {
    private TableView<?> tableView;

    ScrollToColumnEventHandler(TableView<?> tableView) {
        this.tableView = tableView;
    }

    @Override
    public void handle(KeyEvent event) {
        if (KeyCode.RIGHT.equals(event.getCode())) {
            if (!event.isControlDown()) tableView.getSelectionModel().selectRightCell();
            else tableView.getFocusModel().focusRightCell();
            scrollToColumnImpl(tableView, tableView.getFocusModel().getFocusedCell().getTableColumn());
            event.consume();
        }  else if (KeyCode.LEFT.equals(event.getCode())) {
            if (!event.isControlDown()) tableView.getSelectionModel().selectLeftCell();
            else tableView.getFocusModel().focusLeftCell();
            scrollToColumnImpl(tableView, tableView.getFocusModel().getFocusedCell().getTableColumn());
            event.consume();
        }
    }

    public static <T,S> void scrollToColumnIndex(TableView<T> tableView, int index) {
        scrollToColumn(tableView, tableView.getColumns().get(index));
    }

    public static <T,S> void scrollToColumn(TableView<T> tableView, TableColumn<T,S> column) {
        if ( column == null) return;

        if (tableView.getOnScrollToColumn() != null) {
            tableView.getOnScrollToColumn().handle(new ScrollToEvent<>(tableView, tableView, ScrollToEvent.scrollToColumn(), column));
        }

        scrollToColumnImpl(tableView, column);
    }

    private static void scrollToColumnImpl(TableView<?> tableView, TableColumn<?, ?> column) {
        if (column == null) return;

        Bounds scrollContentsBounds = tableView.lookup(".clipped-container").getLayoutBounds();
        getTableViewScrollBar(tableView, Orientation.HORIZONTAL).ifPresent(scroll -> {
            double offset = getLeftOffset(tableView, column);
            double minOffset = offset - scrollContentsBounds.getWidth() + column.getWidth();
            scroll.setValue( Math.max(minOffset, Math.min(scroll.getValue(), offset)) );
        });
    }

    private static double getLeftOffset(TableView<?> tableView, TableColumn<?, ?> column) {
        if (column == null) return 0.0d;

        double offset = 0.0d;
        for (int i=0; i<tableView.getColumns().size(); i++) {
            if (tableView.getColumns().get(i) == column) return offset;
            offset += tableView.getColumns().get(i).getWidth();
        }
        return offset;
    }

    private static Optional<ScrollBar> getTableViewScrollBar(TableView<?> tableView, Orientation orientation) {
        return tableView.lookupAll(".scroll-bar")
                .stream()
                .filter(node -> node instanceof ScrollBar && ((ScrollBar)node).getOrientation() == orientation)
                .map(node -> ((ScrollBar)node))
                .findFirst();
    }
}
