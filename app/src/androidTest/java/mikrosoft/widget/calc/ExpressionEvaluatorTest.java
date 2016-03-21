package mikrosoft.widget.calc;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class ExpressionEvaluatorTest extends TestCase{

    ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    @Test
    public void testRemoveFractalPartWhenResultIsZero() {
        // when
        String result = expressionEvaluator.evaluate("1.2-1.2");

        // then
        assertThat(result, Matchers.equalTo("0"));
    }
}