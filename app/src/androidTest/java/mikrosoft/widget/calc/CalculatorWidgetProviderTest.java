package mikrosoft.widget.calc;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.test.AndroidTestCase;
import android.widget.RemoteViews;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static mikrosoft.widget.calc.CalculatorWidgetProvider.BUTTON_PRESS_ACTION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CalculatorWidgetProviderTest extends AndroidTestCase {

    private static final int NUMBER_OF_BUTTONS = 18;
    private static final int BUTTON_ID = 0;
    private static final int SYMBOL = 1;
    private static final int EXPECTED = 2;
    private static final boolean INSERT_DOT = true;
    private static final boolean NO_DOT = false;

    private static String[] MAX_DATA_TEST_SET = {
            getMaxNumber(NO_DOT),
            getMaxNumber(INSERT_DOT)
    };
    private static final Object[][] ADD_SIGN_TO_EXPRESSION_TEST_DATA = new Object[][]{
            {R.id.zero, "*0", "*0"},
            {R.id.zero, "/0", "/0"},
            {R.id.zero, "+0", "+0"},
            {R.id.zero, "-0", "-0"},
            {R.id.zero, "0.", "0.0"},
            {R.id.zero, getMaxNumber(NO_DOT), getMaxNumber(NO_DOT)},
            {R.id.zero, "", "0"},
            {R.id.zero, "0", "0"},
            {R.id.zero, "10", "100"},
            {R.id.zero, "10+0.", "10+0.0"},
            {R.id.zero, "10+10", "10+100"},
            {R.id.zero, "1*", "1*0"},
            {R.id.zero, "1234", "12340"},
            {R.id.reset, "12", ""},
            {R.id.one, "", "1"},
            {R.id.one, "123", "1231"},
            {R.id.one, "3*", "3*1"},
            {R.id.one, "3/", "3/1"},
            {R.id.one, "3-", "3-1"},
            {R.id.one, "3+", "3+1"},
            {R.id.one, "3.", "3.1"},
            {R.id.one, "0", "0"},
            {R.id.one, "10", "101"},
            {R.id.one, "10.", "10.1"},
            {R.id.one, "10+0", "10+0"},
            {R.id.one, "10+", "10+1"},
            {R.id.minus, "1-", "1-"},
            {R.id.minus, "1/", "1/"},
            {R.id.minus, "1+", "1+"},
            {R.id.minus, "1*", "1*"},
            {R.id.minus, "2", "2-"},
            {R.id.minus, "1.", "1."},
            {R.id.minus, "", "-"},
            {R.id.plus, "1-", "1-"},
            {R.id.plus, "1/", "1/"},
            {R.id.plus, "1+", "1+"},
            {R.id.plus, "1*", "1*"},
            {R.id.plus, "2", "2+"},
            {R.id.plus, "1.", "1."},
            {R.id.plus, "", ""},
            {R.id.divide, "1-", "1-"},
            {R.id.divide, "1/", "1/"},
            {R.id.divide, "1+", "1+"},
            {R.id.divide, "1*", "1*"},
            {R.id.divide, "2", "2/"},
            {R.id.divide, "1.", "1."},
            {R.id.divide, "", ""},
            {R.id.multiply, "1-", "1-"},
            {R.id.multiply, "1/", "1/"},
            {R.id.multiply, "1+", "1+"},
            {R.id.multiply, "1*", "1*"},
            {R.id.multiply, "2", "2*"},
            {R.id.multiply, "1.", "1."},
            {R.id.multiply, "", ""},
            {R.id.clear_one, "", ""},
            {R.id.clear_one, "2", ""},
            {R.id.clear_one, "123+", "123"},
            {R.id.equals, "", ""},
            {R.id.dot, "", ""},
            {R.id.dot, "1.", "1."},
            {R.id.dot, "1.5", "1.5"},
            {R.id.dot, "123+", "123+"},
            {R.id.dot, "123+5", "123+5."},
            {R.id.dot, "-10.6/", "-10.6/"}
    };

    @InjectMocks
    private CalculatorWidgetProvider calculatorWidgetProvider = new CalculatorWidgetProvider();

    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath());
    }

    public void testShouldReadExtras() throws Exception {
        // given
        Intent intent = mockIntent(R.id.one);

        // when
        calculatorWidgetProvider.onReceive(getContext(), intent);

        //then
        verify(intent, times(1)).getExtras();
        verify(intent, times(1)).getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
    }

    public void testCreatePendingIntentForButtons() {
        // given
        Intent intent = mockIntent(R.id.one);
        RemoteViews remoteViews = mock(RemoteViews.class);
        calculatorWidgetProvider.setRemoteViews(remoteViews);

        // when
        calculatorWidgetProvider.onReceive(getContext(), intent);

        // then
        verify(remoteViews, times(NUMBER_OF_BUTTONS)).setOnClickPendingIntent(anyInt(), any(PendingIntent.class));
    }

    public void testEvaluateExpression() {
        // given
        Intent intent = mockIntent(R.id.equals);
        final String SCREEN_ACTUAL_VALUE = "123+100";
        final String EVALUATED_VALUE = "223";
        ExpressionEvaluator expressionEvaluator = mockExpressionEvaluator(SCREEN_ACTUAL_VALUE, EVALUATED_VALUE);
        CalculatorData calculatorData = mockCalculatorData(SCREEN_ACTUAL_VALUE);

        // when
        calculatorWidgetProvider.onReceive(getContext(), intent);

        // then
        assertThat(calculatorWidgetProvider.getExpression(), equalTo(EVALUATED_VALUE));
        verify(expressionEvaluator, times(1)).evaluate(SCREEN_ACTUAL_VALUE);
        verify(calculatorData, times(1)).saveExpression(getContext(), EVALUATED_VALUE);
    }

    @NonNull
    private ExpressionEvaluator mockExpressionEvaluator(String screenActualValue, String evaluatedValue) {
        ExpressionEvaluator expressionEvaluator = mock(ExpressionEvaluator.class);
        when(expressionEvaluator.evaluate(screenActualValue)).thenReturn(evaluatedValue);
        calculatorWidgetProvider.setExpressionEvaluator(expressionEvaluator);
        return expressionEvaluator;
    }

    @NonNull
    private CalculatorData mockCalculatorData(String screenActualValue) {
        CalculatorData calculatorData = mock(CalculatorData.class);
        when(calculatorData.readExpression(getContext())).thenReturn(screenActualValue);
        calculatorWidgetProvider.setCalculatorData(calculatorData);
        return calculatorData;
    }

    public void testAddSignToExpression() {
        for (Object[] row : ADD_SIGN_TO_EXPRESSION_TEST_DATA) {
            verifyAddSymbolToExpression((int) row[BUTTON_ID], (String) row[SYMBOL], (String) row[EXPECTED]);
        }
    }

    private void verifyAddSymbolToExpression(int pressedButtonId, String actualExpression, String expectedExpression) {
        // given
        Intent intent = mockIntent(pressedButtonId);
        CalculatorData calculatorData = mockCalculatorData(actualExpression);

        // when
        calculatorWidgetProvider.onReceive(getContext(), intent);

        // then
        assertThat(calculatorWidgetProvider.getExpression(), equalTo(expectedExpression));
        if (expectedExpression.length() > 0) {
            verify(calculatorData, times(1)).saveExpression(getContext(), expectedExpression);
        }
    }

    public void testResetExpressionOnEnabled() {
        // given
        CalculatorData calculatorData = mockCalculatorData("123+6787.78");

        // when
        calculatorWidgetProvider.onEnabled(getContext());

        // then
        verify(calculatorData, times(1)).saveExpression(getContext(), "");
    }

    public void testDoNotEvaluateWhenExpressionEmpty() {
        // given
        Intent intent = mockIntent(R.id.equals);
        mockCalculatorData("");
        ExpressionEvaluator expressionEvaluator = mockExpressionEvaluator(null, null);

        // when
        calculatorWidgetProvider.onReceive(getContext(), intent);

        // then
        verify(expressionEvaluator, times(0)).evaluate("");
    }

    @NonNull
    private Intent mockIntent(int buttonId) {
        Intent intent = mock(Intent.class);
        when(intent.getAction()).thenReturn(BUTTON_PRESS_ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt(CalculatorWidgetProvider.BUTTON_ID, buttonId);
        when(intent.getExtras()).thenReturn(bundle);
        return intent;
    }

    public void testEvaluateException() {
        // given
        Intent intent = mockIntent(R.id.equals);
        ExpressionEvaluator expressionEvaluator = mock(ExpressionEvaluator.class);
        when(expressionEvaluator.evaluate(anyString())).thenThrow(new RuntimeException());
        calculatorWidgetProvider.setExpressionEvaluator(expressionEvaluator);
        CalculatorData calculatorData = mockCalculatorData("bad expression");

        // when
        calculatorWidgetProvider.onReceive(getContext(), intent);

        // then
        assertThat(calculatorWidgetProvider.getExpression(), equalTo(CalculatorWidgetProvider.EVALUATE_ERROR));
        verify(calculatorData, times(1)).saveExpression(getContext(), "");
    }

    public void testSaveEmptyExpressionWhenReset() {
        // given
        Intent intent = mockIntent(R.id.reset);
        CalculatorData calculatorData = mockCalculatorData("12+457");

        // when
        calculatorWidgetProvider.onReceive(getContext(), intent);

        // then
        assertThat(calculatorWidgetProvider.getExpression(), equalTo(""));
        verify(calculatorData, times(1)).saveExpression(getContext(), "");
    }

    public void testSaveEmptyExpressionWhenClearOne() {
        // given
        Intent intent = mockIntent(R.id.clear_one);
        CalculatorData calculatorData = mockCalculatorData("1");

        // when
        calculatorWidgetProvider.onReceive(getContext(), intent);

        // then
        assertThat(calculatorWidgetProvider.getExpression(), equalTo(""));
        verify(calculatorData, times(1)).saveExpression(getContext(), "");
    }

    public void testMaxNumberLengthConstraint() {
        for(String testData: MAX_DATA_TEST_SET) {
            verifyCannotAddSignToExpression(testData);
        }
    }

    private void verifyCannotAddSignToExpression(String testData) {
        // given
        Intent intent = mockIntent(R.id.three);
        mockCalculatorData(testData);

        // when
        calculatorWidgetProvider.onReceive(getContext(), intent);

        // then
        assertThat(calculatorWidgetProvider.getExpression(), equalTo(testData));
    }

    private static String getMaxNumber(boolean insertDot) {
        String result = "1";
        for (int i = 0; i < ExpressionEvaluator.MAX_NUMBER_LENGTH-1; i++) {
            result += "0";
            if(insertDot == true && ExpressionEvaluator.MAX_NUMBER_LENGTH /2 == i) {
                result += ".";
            }
        }
        return result;
    }
}