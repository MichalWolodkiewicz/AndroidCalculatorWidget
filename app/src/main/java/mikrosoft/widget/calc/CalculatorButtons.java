package mikrosoft.widget.calc;

import java.util.HashMap;

public class CalculatorButtons {

    private static final String MAX_NUMBER_CONSTRAINT = "^.*([0-9]{10}|[0-9.]{11})$";
    private static final String ONLY_ZERO_CONSTRAINT = "^[0]$";
    private static final String ZERO_AT_THE_END = "^.*[+-/*][0]$";
    private static final String[] NUMBER_CONSTRAINT_ARRAY = {MAX_NUMBER_CONSTRAINT, ONLY_ZERO_CONSTRAINT, ZERO_AT_THE_END};

    private static HashMap<Integer, Object[]> BUTTONS = new HashMap() {{
        put(R.id.zero, new Object[]{"0", NUMBER_CONSTRAINT_ARRAY});
        put(R.id.one, new Object[]{"1", NUMBER_CONSTRAINT_ARRAY});
        put(R.id.two, new Object[]{"2", NUMBER_CONSTRAINT_ARRAY});
        put(R.id.three, new Object[]{"3", NUMBER_CONSTRAINT_ARRAY});
        put(R.id.four, new Object[]{"4", NUMBER_CONSTRAINT_ARRAY});
        put(R.id.five, new Object[]{"5", NUMBER_CONSTRAINT_ARRAY});
        put(R.id.six, new Object[]{"6", NUMBER_CONSTRAINT_ARRAY});
        put(R.id.seven, new Object[]{"7", NUMBER_CONSTRAINT_ARRAY});
        put(R.id.eight, new Object[]{"8", NUMBER_CONSTRAINT_ARRAY});
        put(R.id.nine, new Object[]{"9", NUMBER_CONSTRAINT_ARRAY});
        put(R.id.dot, new Object[]{".", new String[]{"^.*[-+/*.]$|^.*[0-9]*[.][0-9]*$|^$"}});
        put(R.id.multiply, new Object[]{"*", new String[]{"^$|^.*[-+/*.]+$"}});
        put(R.id.clear_one, new Object[]{null, null});
        put(R.id.reset, new Object[]{null, null});
        put(R.id.minus, new Object[]{"-", new String[]{"^.*[-+/*.]+$"}});
        put(R.id.plus, new Object[]{"+", new String[]{"^$|^.*[-+/*.]+$"}});
        put(R.id.divide, new Object[]{"/", new String[]{"^$|^.*[-+/*.]+$"}});
        put(R.id.equals, new Object[]{"", null});
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
