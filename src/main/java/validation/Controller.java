package validation;

import static org.controlsfx.validation.Validator.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.decoration.CompoundValidationDecoration;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class Controller {
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private TextField textField3;
    @FXML
    private CheckBox checkBox;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Slider slider;
    @FXML
    private ListView<ValidationMessage> listView;
    @FXML
    private Button buttonValidated;

    @FXML
    private void initialize() {
        colorPicker.setValue(Color.GREEN);

        ValidationSupport support = new ValidationSupport();
//        support.setValidationDecorator(new StyleClassValidationDecoration());
        support.setValidationDecorator(new CompoundValidationDecoration(
                new GraphicValidationDecoration(),
                new StyleClassValidationDecoration()));
        support.registerValidator(textField1, false, createEmptyValidator(
                "Text is required",
                Severity.WARNING));
        support.registerValidator(textField2, createEqualsValidator(
                "A or B or C is required",
                Arrays.asList("A", "B", "C")));
        support.registerValidator(textField3, createRegexValidator(
                "Number is required",
                Pattern.compile("^[0-9]+$"), Severity.ERROR));
        support.registerValidator(checkBox, createEqualsValidator(
                "Check is required",
                Collections.singletonList(true)));
        support.registerValidator(comboBox, createEqualsValidator(
                "First item is required",
                Collections.singletonList(comboBox.getItems().get(0))));
        support.registerValidator(colorPicker, createEqualsValidator(
                "WHITE or BLACK is required",
                Arrays.asList(Color.WHITE, Color.BLACK)));
        support.registerValidator(slider, createPredicateValidator(
                (Double value) ->  value > 0.0,
                "Positive number is required"));

        buttonValidated.disableProperty().bind(support.invalidProperty());
        support.validationResultProperty().addListener((o, oldValue, newValue) ->
                listView.getItems().setAll(newValue.getMessages()));
    }
}
