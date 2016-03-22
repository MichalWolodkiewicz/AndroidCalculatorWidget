package mikrosoft.widget.calc;

import com.udojava.evalex.Expression;

import java.math.RoundingMode;

public class ExpressionEvaluator {

    public String evaluate(String expression) {
        String result = new Expression(expression).setPrecision(10).setRoundingMode(RoundingMode.UNNECESSARY).eval().toPlainString();
        if(result.matches("[0.]*")) {
            result = "0";
        }
        return result;
    }
}
