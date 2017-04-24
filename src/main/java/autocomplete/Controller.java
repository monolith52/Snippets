package autocomplete;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {

    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private TextField textField3;
    @FXML
    private TextField textField4;

    @FXML
    private void initialize() {
        List<String> suggestions = Arrays.asList("able", "about", "above",
                "across", "act", "afraid",
                "after", "afternoon", "again", "against");
        AutoCompletionBinding<String> completion = TextFields.bindAutoCompletion(textField1, suggestions);
        completion.setDelay(1000L);
        completion.setVisibleRowCount(5);
        textField1.setOnKeyPressed(event -> {
            if (KeyCode.DOWN.equals(event.getCode())) completion.setUserInput(textField1.getText());
        });
        completion.setOnAutoCompleted(event ->
            System.out.println("Completion text: " + event.getCompletion()));

        Object suggestoins2Lock = new Object();
        List<String> suggestions2 = new ArrayList<>(Arrays.asList(
                "able", "about", "above",
                "across", "act", "afraid",
                "after", "afternoon", "again", "against"));
        AutoCompletionBinding<String> completion2 = TextFields.bindAutoCompletion(textField2, request -> {
            synchronized (suggestoins2Lock) {
                return suggestions2.stream()
                        .filter(v -> v.indexOf(request.getUserText()) == 0)
                        .collect(Collectors.toList());
            }
        });
        textField2.setOnKeyPressed(event -> {
            if (KeyCode.DOWN.equals(event.getCode())) completion2.setUserInput(textField2.getText());
            if (KeyCode.UP.equals(event.getCode())) suggestions2.add(textField2.getText());
        });
        completion2.setOnAutoCompleted(event ->
            System.out.println("Completion text: " + event.getCompletion()));
        synchronized (suggestoins2Lock) {
            suggestions2.add("new keyword");
            Collections.sort(suggestions2);
        }

        AutoCompletionBinding<String> completion3 = TextFields.bindAutoCompletion(textField3, request -> {
            System.out.println("Start sleep");
            System.out.println("isCanceled: " + request.isCancelled());
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                Thread.interrupted();
                System.out.println("Interrupted sleep");
                return Collections.emptyList();
            }
            synchronized (suggestoins2Lock) {
                System.out.println("Start in lock");
                return suggestions2.stream()
                        .filter(v -> v.indexOf(request.getUserText()) == 0)
                        .collect(Collectors.toList());
            }
        });

        AutoCompletionBinding<String> completion4 = TextFields.bindAutoCompletion(textField4, request -> {
            System.out.println("Start loop");
            for (int i=0; i<10000; i++) {
                try (InputStream in = getClass().getResource("Main.fxml").openStream()) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    int d;
                    while ((d = in.read()) != -1) {
                        out.write(d);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    return Collections.emptyList();
                }
                if (Thread.interrupted()) {
                    System.out.println("Interrupted loop");
                    return Collections.emptyList();
                }
            }
            synchronized (suggestoins2Lock) {
                System.out.println("Start in lock");
                return suggestions2.stream()
                        .filter(v -> v.indexOf(request.getUserText()) == 0)
                        .collect(Collectors.toList());
            }
        });
    }
}
