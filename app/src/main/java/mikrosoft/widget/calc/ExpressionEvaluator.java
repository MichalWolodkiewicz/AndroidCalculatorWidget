package mikrosoft.widget.calc;

import com.udojava.evalex.Expression;

public class ExpressionEvaluator {

    public String evaluate(String expression) {
        return new Expression(expression).eval().toString();
    }
}
