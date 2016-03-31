package mikrosoft.widget.calc;

import android.content.Intent;
import android.test.AndroidTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CalculationTest extends AndroidTestCase {

    private static final Object[][] CALCULATION_TEST_DATA = new Object[][]{
            {new int[]{R.id.one, R.id.plus, R.id.nine}, "10"},
            {new int[]{R.id.one, R.id.zero, R.id.zero, R.id.multiply, R.id.zero}, "0"},
            {new int[]{R.id.one, R.id.zero, R.id.zero, R.id.multiply, R.id.zero}, "0"}
    };

    CalculatorWidgetProvider widgetProvider = new CalculatorWidgetProvider();

    public void testCalculations() {
        for (Object[] testRow : CALCULATION_TEST_DATA) {
            testCalculations(testRow);
        }
    }

    private void testCalculations(Object[] testRow) {
        int[] buttonsSequence = (int[]) testRow[0];
        String expectedResult = (String) testRow[1];
        widgetProvider.onReceive(getContext(), createIntent(R.id.reset));
        for (int buttonId : buttonsSequence) {
            widgetProvider.onReceive(getContext(), createIntent(buttonId));
        }
        widgetProvider.onReceive(getContext(), createIntent(R.id.equals));
        assertThat(widgetProvider.getExpression(), equalTo(expectedResult));
    }

    private Intent createIntent(int buttonId) {
        Intent intent = new Intent(CalculatorWidgetProvider.BUTTON_PRESS_ACTION);
        intent.putExtra(CalculatorWidgetProvider.BUTTON_ID, buttonId);
        return intent;
    }
}
