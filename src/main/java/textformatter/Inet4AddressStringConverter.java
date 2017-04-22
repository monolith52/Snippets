package textformatter;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.util.StringConverter;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Inet4AddressStringConverter extends StringConverter<Inet4Address> {
    final private static String SEPARATOR = " . ";
    final private static Pattern NOT_NUMBER_PATTERN = Pattern.compile("[^0-9]");

    @Override
    public String toString(Inet4Address addr) {
        List<String> as = Arrays.asList("0", "0", "0", "0");
        if (addr != null) {
            as = IntStream.range(0, 4).mapToObj(i -> Integer.toString(addr.getAddress()[i] & 0xFF)).collect(Collectors.toList());
        }
        return String.join(" . ", as);
    }

    @Override
    public Inet4Address fromString(String string) {
        byte[] bytes = new byte[]{0, 0, 0, 0};
        String[] numbers = string.split(SEPARATOR);
        for (int i = 0; i < bytes.length && i < numbers.length; i++) {
            bytes[i] = (byte) Math.max(0, Math.min(255, Integer.parseInt(numbers[i])));
        }

        try {
            return (Inet4Address) InetAddress.getByAddress(bytes);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    static UnaryOperator<TextFormatter.Change> textFormatterFilter() {
        return c -> {
            TextInputControl control = (TextInputControl) c.getControl();
            if (".".equals(c.getText())) {
                c.setText("");
                c.setAnchor((c.getAnchor() - 1));
                c.setCaretPosition(c.getCaretPosition() - 1);
                int currentIndex = getCurrentIndex(control);
                if (currentIndex < 3) {
                    int nextPosition = getPositionOfIndex(control, currentIndex + 1);
                    c.setAnchor(nextPosition);
                    c.setCaretPosition(nextPosition);
                }
            } else if (!c.getText().isEmpty()) {
                String newStr = NOT_NUMBER_PATTERN.matcher(c.getText()).replaceAll("");
                int diffcount = c.getText().length() - newStr.length();
                c.setAnchor(c.getAnchor() - diffcount);
                c.setCaretPosition(c.getCaretPosition() - diffcount);
                c.setText(newStr);
            }
            if (!c.isContentChange()) {
                if (c.getAnchor() > 0 && control.getText().charAt(c.getAnchor() - 1) == '.')
                    c.setAnchor(c.getAnchor() + 1);
                if (c.getAnchor() < control.getText().length() && control.getText().charAt(c.getAnchor()) == '.')
                    c.setAnchor(c.getAnchor() - 1);
                if (c.getAnchor() < c.getCaretPosition()) {
                    int spaceIndex = control.getText().indexOf(" ", c.getAnchor());
                    if (spaceIndex != -1) c.setCaretPosition(Math.min(spaceIndex, c.getCaretPosition()));
                }
                if (c.getAnchor() > c.getCaretPosition()) {
                    int spaceIndex = control.getText().lastIndexOf(" ", c.getAnchor()-1);
                    if (spaceIndex != -1) c.setCaretPosition(Math.max(spaceIndex + 1, c.getCaretPosition()));
                }
            }
            return c;
        };
    }

    private static int getCurrentIndex(TextInputControl control) {
        int count = 0, lastIndex = 0;
        String target = control.getText().substring(0, control.getCaretPosition());
        while (lastIndex != -1) {
            lastIndex = target.indexOf(SEPARATOR, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += SEPARATOR.length();
            }
        }
        return count;
    }

    private static int getPositionOfIndex(TextInputControl control, int index) {
        int count = 0, lastIndex = 0;
        String target = control.getText();
        while (lastIndex != -1) {
            if (count >= index) return lastIndex;
            lastIndex = target.indexOf(SEPARATOR, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += SEPARATOR.length();
            }
        }
        return 0;
    }
}
