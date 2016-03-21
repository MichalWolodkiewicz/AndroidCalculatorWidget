package mikrosoft.widget.calc;

import com.udojava.evalex.Expression;

public class ExpressionEvaluator {

    public String evaluate(String expression) {
        String result = new Expression(expression).eval().toPlainString();
        if(result.matches("[0.]*")) {
            result = "0";
        }
        return result;
    }
}
