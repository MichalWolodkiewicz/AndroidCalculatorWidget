package mikrosoft.widget.calc;

import java.util.HashMap;

public class CalculatorButtons {

    private static final String ZERO = "0";
    private static final String ONE = "1";
    private static final String TWO = "2";
    private static final String THREE = "3";
    private static final String FOUR = "4";
    private static final String FIVE = "5";
    private static final String SIX = "6";
    private static final String SEVEN = "7";
    private static final String EIGHT = "8";
    private static final String NINE = "9";
    private static final String DOT = ".";
    private static final String MULTIPLY = "*";
    private static final String MINUS = "-";
    private static final String PLUS = "+";
    private static final String DIVIDE = "/";

    private static HashMap<Integer, Object[]> BUTTONS = new HashMap() {{
        put(R.id.zero, new Object[]{"0", new String[]{"[+-/*][0]$"}});
        put(R.id.one, new Object[]{"1", null});
        put(R.id.two, new Object[]{"2", null});
        put(R.id.three, new Object[]{"3", null});
        put(R.id.four, new Object[]{"4", null});
        put(R.id.five, new Object[]{"5", null});
        put(R.id.six, new Object[]{"6", null});
        put(R.id.seven, new Object[]{"7", null});
        put(R.id.eight, new Object[]{"8", null});
        put(R.id.nine, new Object[]{"9", null});
        put(R.id.dot, new Object[]{".", new String[]{""}});
        put(R.id.multiply, new Object[]{"*", new String[]{"^.*[-+/*.]+$"}});
        put(R.id.clear_one, new Object[]{null, null});
        put(R.id.reset, new Object[]{null, null});
        put(R.id.minus, new Object[]{"-", new String[]{"^.*[-+/*.]+$"}});
        put(R.id.plus, new Object[]{"+", new String[]{"^.*[-+/*.]+$"}});
        put(R.id.divide, new Object[]{"/", new String[]{"^.*[-+/*.]+$"}});
        put(R.id.equals, new Object[]{"=", null});
    }};

    public static String getButtonSymbol(int buttonId) {
        return (String) BUTTONS.get(buttonId)[0];
    }

    public static String[] getButtonConstraints(int buttonId) {
        return (String[]) BUTTONS.get(buttonId)[1];
    }

    public static Iterable<Integer> getButtonsIds() {
        return BUTTONS.keySet();
    }
}
