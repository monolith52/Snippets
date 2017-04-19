package decorator;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.decoration.Decorator;
import org.controlsfx.control.decoration.GraphicDecoration;
import org.controlsfx.control.decoration.StyleClassDecoration;
import org.w3c.dom.css.CSSStyleDeclaration;


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
        Decorator.addDecoration(textField1, new GraphicDecoration(createDecoratorNode(Color.RED),Pos.TOP_LEFT));
        Decorator.addDecoration(textField1, new GraphicDecoration(createDecoratorNode(Color.RED),Pos.TOP_CENTER));
        Decorator.addDecoration(textField1, new GraphicDecoration(createDecoratorNode(Color.RED),Pos.TOP_RIGHT));
        Decorator.addDecoration(textField1, new GraphicDecoration(createDecoratorNode(Color.GREEN),Pos.CENTER_LEFT));
        Decorator.addDecoration(textField1, new GraphicDecoration(createDecoratorNode(Color.GREEN),Pos.CENTER));
        Decorator.addDecoration(textField1, new GraphicDecoration(createDecoratorNode(Color.GREEN), Pos.CENTER_RIGHT));
        Decorator.addDecoration(textField1, new GraphicDecoration(createDecoratorNode(Color.BLUE),Pos.BOTTOM_LEFT));
        Decorator.addDecoration(textField1, new GraphicDecoration(createDecoratorNode(Color.BLUE),Pos.BOTTOM_CENTER));
        Decorator.addDecoration(textField1, new GraphicDecoration(createDecoratorNode(Color.BLUE),Pos.BOTTOM_RIGHT));

        Decorator.addDecoration(textField2, new GraphicDecoration(createDecoratorNode(Color.ORANGE),Pos.BASELINE_LEFT));
        Decorator.addDecoration(textField2, new GraphicDecoration(createDecoratorNode(Color.ORANGE),Pos.BASELINE_CENTER));
        Decorator.addDecoration(textField2, new GraphicDecoration(createDecoratorNode(Color.ORANGE),Pos.BASELINE_RIGHT));

        Decorator.addDecoration(textField3, new StyleClassDecoration("warning"));
    }

    private Node createDecoratorNode(Color color) {
        Rectangle d = new Rectangle(7, 7);
        d.setFill(color);
        return d;
    }
}
