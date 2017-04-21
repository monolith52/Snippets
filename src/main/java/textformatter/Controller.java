package textformatter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.CurrencyStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.util.Locale;

public class Controller {

    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;

    @FXML
    private void initialize() {
        TextFormatter<Number> currencyFormatter = new TextFormatter<>(
                new CurrencyStringConverter(Locale.JAPAN), 0, change -> {
            // 選択範囲は1文字目より前にできない
            change.setAnchor(Math.max(1, change.getAnchor()));
            change.setCaretPosition(Math.max(1, change.getCaretPosition()));
            // テキスト変更範囲は1文字目より前にできない
            change.setRange(Math.max(1, change.getRangeStart()), Math.max(1, change.getRangeEnd()));
            return change;
        });
        textField1.setTextFormatter(currencyFormatter);

        TextFormatter<String> lowerFormatter = new TextFormatter<>(change -> {
            change.setText(change.getText().toLowerCase());
            return change;
        });
        textField2.setTextFormatter(lowerFormatter);
    }
}
