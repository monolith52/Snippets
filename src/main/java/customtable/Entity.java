package customtable;

import javafx.beans.property.*;
import javafx.scene.paint.Color;

public class Entity {
    private StringProperty value1;
    private StringProperty value2;
    private BooleanProperty value3;
    private ObjectProperty<Color> value4;

    Entity(String v1, String v2, Boolean v3, Color v4) {
        value1 = new SimpleStringProperty(v1);
        value2 = new SimpleStringProperty(v2);
        value3 = new SimpleBooleanProperty(v3);
        value4 = new SimpleObjectProperty<>(v4);
    }

    public String getValue1() {
        return value1.get();
    }

    public StringProperty value1Property() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1.set(value1);
    }

    public String getValue2() {
        return value2.get();
    }

    public StringProperty value2Property() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2.set(value2);
    }

    public boolean isValue3() {
        return value3.get();
    }

    public BooleanProperty value3Property() {
        return value3;
    }

    public void setValue3(boolean value3) {
        this.value3.set(value3);
    }

    public Color getValue4() {
        return value4.get();
    }

    public ObjectProperty<Color> value4Property() {
        return value4;
    }

    public void setValue4(Color value4) {
        this.value4.set(value4);
    }
}
