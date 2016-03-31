package mikrosoft.widget.calc;

import com.udojava.evalex.Expression;

public class ExpressionEvaluator {

    public static final int MAX_NUMBER_LENGTH = 10;
    private static final String LAST_SIGN_IS_OPERATOR = "^.*[-/+*]$";

    public String evaluate(String expression) {
        expression = cleanExpression(expression);
        String result = new Expression(expression).setPrecision(MAX_NUMBER_LENGTH).eval().toPlainString();
        if (result.matches("[0.]*")) {
            result = "0";
        }
        return result;
    }

    private String cleanExpression(String expression) {
        if(expression.matches(LAST_SIGN_IS_OPERATOR)) {
            expression = expression.substring(0, expression.length()-1);
        }
        return expression;
    }


}
