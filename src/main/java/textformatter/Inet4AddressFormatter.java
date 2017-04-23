package textformatter;

import javafx.scene.control.TextFormatter;
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

class Inet4AddressFormatter extends TextFormatter<Inet4Address> {
    final private static String SEPARATOR = " . ";
    final private static Pattern NOT_NUMBER_PATTERN = Pattern.compile("[^0-9]");

    Inet4AddressFormatter(Inet4Address defaultValue) {
        super(new Inet4AddressStringConverter(),
                defaultValue,
                Inet4AddressFormatter::textFormatterFilter);
    }


    static public Change textFormatterFilter(Change c) {
        return UnaryOperator.<TextFormatter.Change>identity()
                .andThen(Inet4AddressFormatter::nextSeparateFilter)
                .andThen(Inet4AddressFormatter::disableNotNumberFilter)
                .andThen(Inet4AddressFormatter::lockSeparatorFilter)
                .apply(c);
    }

    static private TextFormatter.Change nextSeparateFilter(TextFormatter.Change c) {
        // "."をタイプすると次のバイトを選択する
        if (".".equals(c.getText())) {
            restrictText(c, text -> "");
            c.setRange(c.getRangeStart(), c.getRangeStart());

            int currentIndex = getCurrentSeparateIndex(c);
            if (currentIndex < 3) {
                int nextBegin = getPositionOfSeparateIndex(c, currentIndex + 1);
                int spaceIndex = c.getControlText().indexOf(" ", nextBegin);
                c.setAnchor(nextBegin);
                c.setCaretPosition(spaceIndex == -1 ? c.getControlText().length() : spaceIndex);
            }
        }
        return c;
    }

    static private TextFormatter.Change disableNotNumberFilter(TextFormatter.Change c) {
        // 数字以外は入力を受け付けない
        if (!c.getText().isEmpty()) {
            restrictText(c, text -> NOT_NUMBER_PATTERN.matcher(text).replaceAll(""));
        }
        return c;
    }

    static private TextFormatter.Change lockSeparatorFilter(TextFormatter.Change c) {
        // セパレータ部分は書き換えさせない
        if (c.isDeleted()) {
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
    }

    static private void restrictText(TextFormatter.Change c, UnaryOperator<String> restriction) {
        String newStr = restriction.apply(c.getText());
        int diffcount = c.getText().length() - newStr.length();
        c.selectRange(c.getAnchor() - diffcount, c.getCaretPosition() - diffcount);
        c.setText(newStr);
    }

    static private int getCurrentSeparateIndex(TextFormatter.Change c) {
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

    static private int getPositionOfSeparateIndex(TextFormatter.Change c, int index) {
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

    public static class Inet4AddressStringConverter extends StringConverter<Inet4Address> {
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

            if (string != null) {
                String[] numbers = string.split(SEPARATOR);
                for (int i = 0; i < bytes.length && i < numbers.length; i++) {
                    bytes[i] = (byte) Math.max(0, Math.min(255, Integer.parseInt(numbers[i])));
                }
            }

            try {
                return (Inet4Address) InetAddress.getByAddress(bytes);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
