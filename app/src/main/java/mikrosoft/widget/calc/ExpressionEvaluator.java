package mikrosoft.widget.calc;

import com.udojava.evalex.Expression;

public class ExpressionEvaluator {

    public static final int MAX_NUMBER_LENGTH = 10;

    public String evaluate(String expression) {
        String result = new Expression(expression).setPrecision(MAX_NUMBER_LENGTH).eval().toPlainString();
        if (result.matches("[0.]*")) {
            result = "0";
        }
        return result;
    }
}
