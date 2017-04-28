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
 * SelectionModel.setSelectionMode(SelectionMode.SINGLE), SelectionModel.setCellSelectionEnabled(true).
 *
 * <pre>
 * {@code
 * // usage
 * yourTableView.addEventHandler(KeyEvent.KEY_PRESSED, new PagenateColumnEventHandler(yourTableView));
 * }</pre>
 */
class PaginateColumnEventHandler implements EventHandler<KeyEvent> {
    private TableView<?> tableView;

    PaginateColumnEventHandler(TableView<?> tableView) {
        this.tableView = tableView;
    }

    @Override
    public void handle(KeyEvent event) {
        if (KeyCode.RIGHT.equals(event.getCode())) {
            if (!event.isControlDown()) tableView.getSelectionModel().selectRightCell();
            else tableView.getFocusModel().focusRightCell();
            if (!isInViewport(tableView, getFocusedColumn(tableView))) headToColumnImpl(tableView, getFocusedColumn(tableView));
            event.consume();
        }  else if (KeyCode.LEFT.equals(event.getCode())) {
            if (!event.isControlDown()) tableView.getSelectionModel().selectLeftCell();
            else tableView.getFocusModel().focusLeftCell();
            scrollToColumnImpl(tableView, getFocusedColumn(tableView));
            event.consume();
        }
    }

    private static TableColumn<?, ?> getFocusedColumn(TableView<?> tableView) {
        return tableView.getFocusModel().getFocusedCell().getTableColumn();
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

    private static boolean isInViewport(TableView<?> tableView, TableColumn<?, ?> column) {
        if (column == null) return false;

        Bounds scrollContentsBounds = tableView.lookup(".clipped-container").getLayoutBounds();
        double leftOffset = getLeftOffset(tableView, column);

        boolean[] returnValue = new boolean[]{false};
        getTableViewScrollBar(tableView, Orientation.HORIZONTAL).ifPresent(scroll ->
            returnValue[0] = scroll.getValue() <= leftOffset &&
                    leftOffset + column.getWidth() <= scroll.getValue() + scrollContentsBounds.getWidth()
        );
        return returnValue[0];
    }

    private static void headToColumnImpl(TableView<?> tableView, TableColumn<?, ?> column) {
        if (column == null) return;

        Bounds scrollContentsBounds = tableView.lookup(".clipped-container").getLayoutBounds();
        getTableViewScrollBar(tableView, Orientation.HORIZONTAL).ifPresent(scroll -> {
                double leftOffset = getLeftOffset(tableView, column);
                leftOffset = Math.min(leftOffset, scroll.getMax()); // Check for considering last page.
                scroll.setValue(leftOffset);
        });
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
