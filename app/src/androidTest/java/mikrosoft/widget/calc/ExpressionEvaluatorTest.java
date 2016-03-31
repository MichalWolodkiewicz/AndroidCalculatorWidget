package mikrosoft.widget.calc;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ExpressionEvaluatorTest extends TestCase {

    ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    public void testRemoveFractalPartWhenResultIsZero() {
        // when
        String result = expressionEvaluator.evaluate("1.2-1.2");

        // then
        assertThat(result, equalTo("0"));
    }

    private static final String[] OPERATOR_REMOVE_TEST_DATA = {
            "100+", "100/", "100-", "100+"
    };

    public void testRemoveOperatorBeforeEvaluate() {
        for (String testData : OPERATOR_REMOVE_TEST_DATA) {
            // when
            String result = expressionEvaluator.evaluate(testData);
            // then
            String expectedResult = testData.substring(0, testData.length()-1);
            assertThat(result, equalTo(expectedResult));
        }
    }
}