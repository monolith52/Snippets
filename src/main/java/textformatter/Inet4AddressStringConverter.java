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
        return String.join(SEPARATOR, as);
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
            // "."をタイプすると次のバイトを選択する
            if (".".equals(c.getText())) {
                c.setText("");
                c.selectRange(c.getControlAnchor(), c.getControlCaretPosition());
                c.setRange(c.getRangeStart(), c.getRangeStart());
                selectNextSeparate(c);

                // 数字以外は入力を受け付けない
            } else if (!c.getText().isEmpty()) {
                String newStr = NOT_NUMBER_PATTERN.matcher(c.getText()).replaceAll("");
                int diffcount = c.getText().length() - newStr.length();
                c.selectRange(c.getAnchor() - diffcount, c.getCaretPosition() - diffcount);
                c.setText(newStr);

                // セパレータ部分は書き換えさせない
            } else if (c.isDeleted()) {
                if (SEPARATOR.contains(c.getControlText().substring(c.getRangeStart(), c.getRangeEnd()))) {
                    c.setRange(c.getRangeStart(), c.getRangeStart());
                    c.selectRange(c.getControlAnchor(), c.getControlCaretPosition());
                }

                // セパレータ部分は選択させない
            } else if (!c.isContentChange()) {
                if (c.getAnchor() > 0 && c.getControlText().charAt(c.getAnchor() - 1) == '.')
                    c.setAnchor(c.getAnchor() + 1);
                if (c.getAnchor() < c.getControlText().length() && c.getControlText().charAt(c.getAnchor()) == '.')
                    c.setAnchor(c.getAnchor() - 1);
                if (c.getAnchor() < c.getCaretPosition()) {
                    int spaceIndex = c.getControlText().indexOf(" ", c.getAnchor());
                    if (spaceIndex != -1) c.setCaretPosition(Math.min(spaceIndex, c.getCaretPosition()));
                }
                if (c.getAnchor() > c.getCaretPosition()) {
                    int spaceIndex = c.getControlText().lastIndexOf(" ", c.getAnchor() - 1);
                    if (spaceIndex != -1) c.setCaretPosition(Math.max(spaceIndex + 1, c.getCaretPosition()));
                }
            }

            return c;
        };
    }

    private static void selectNextSeparate(TextFormatter.Change c) {
        TextInputControl control = (TextInputControl) c.getControl();
        int currentIndex = getCurrentSeparateIndex(c);
        if (currentIndex < 3) {
            int nextBegin = getPositionOfSeparateIndex(c, currentIndex + 1);
            int spaceIndex = c.getControlText().indexOf(" ", nextBegin);
            c.setAnchor(nextBegin);
            c.setCaretPosition(spaceIndex == -1 ? c.getControlText().length() : spaceIndex);
        }
    }

    private static int getCurrentSeparateIndex(TextFormatter.Change c) {
        int count = 0, lastIndex = 0;
        String target = c.getControlText().substring(0, c.getControlCaretPosition());
        while (lastIndex != -1) {
            lastIndex = target.indexOf(SEPARATOR, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += SEPARATOR.length();
            }
        }
        return count;
    }

    private static int getPositionOfSeparateIndex(TextFormatter.Change c, int index) {
        int count = 0, lastIndex = 0;
        String target = c.getControlText();
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
