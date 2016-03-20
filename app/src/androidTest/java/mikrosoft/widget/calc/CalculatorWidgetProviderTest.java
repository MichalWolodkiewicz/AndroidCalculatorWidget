package mikrosoft.widget.calc;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.test.AndroidTestCase;
import android.widget.RemoteViews;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static mikrosoft.widget.calc.CalculatorWidgetProvider.BUTTON_PRESS_ACTION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
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
    private static final Object[][] ADD_SIGN_TO_EXPRESSION_TEST_DATA = new Object[][]{
            {R.id.zero, "*0", "*0"},
            {R.id.zero, "/0", "/0"},
            {R.id.zero, "+0", "+0"},
            {R.id.zero, "-0", "-0"},
            {R.id.zero, "0.", "0.0"},
            {R.id.zero, "", "0"},
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
            {R.id.minus, "1-", "1-"},
            {R.id.minus, "1/", "1/"},
            {R.id.minus, "1+", "1+"},
            {R.id.minus, "1*", "1*"},
            {R.id.minus, "2", "2-"},
            {R.id.minus, "1.", "1."}
    };

    @InjectMocks
    private CalculatorWidgetProvider calculatorWidgetProvider = new CalculatorWidgetProvider();

    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath());
    }

    public void testShouldReadExtras() throws Exception {
        // given
        Intent intent = mock(Intent.class);
        when(intent.getAction()).thenReturn(BUTTON_PRESS_ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt(CalculatorWidgetProvider.BUTTON_ID, R.id.one);
        when(intent.getExtras()).thenReturn(bundle);

        // when
        calculatorWidgetProvider.onReceive(getContext(), intent);

        //then
        verify(intent, times(1)).getExtras();
        verify(intent, times(1)).getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
    }

    public void testCreatePendingIntentForButtons() {
        // given
        Intent intent = mock(Intent.class);
        when(intent.getAction()).thenReturn(BUTTON_PRESS_ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt(CalculatorWidgetProvider.BUTTON_ID, R.id.one);
        when(intent.getExtras()).thenReturn(bundle);
        RemoteViews remoteViews = mock(RemoteViews.class);
        calculatorWidgetProvider.setRemoteViews(remoteViews);

        // when
        calculatorWidgetProvider.onReceive(getContext(), intent);

        // then
        verify(remoteViews, times(NUMBER_OF_BUTTONS)).setOnClickPendingIntent(anyInt(), any(PendingIntent.class));
    }

    public void testEvaluateExpression() {
        // given
        Intent intent = mock(Intent.class);
        when(intent.getAction()).thenReturn(BUTTON_PRESS_ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt(CalculatorWidgetProvider.BUTTON_ID, R.id.equals);
        when(intent.getExtras()).thenReturn(bundle);
        final String SCREEN_ACTUAL_VALUE = "123+100";
        final String EVALUATED_VALUE = "223";
        ExpressionEvaluator expressionEvaluator = mock(ExpressionEvaluator.class);
        when(expressionEvaluator.evaluate(SCREEN_ACTUAL_VALUE)).thenReturn(EVALUATED_VALUE);
        calculatorWidgetProvider.setExpressionEvaluator(expressionEvaluator);
        CalculatorData calculatorData = mock(CalculatorData.class);
        when(calculatorData.readExpression(getContext())).thenReturn(SCREEN_ACTUAL_VALUE);
        calculatorWidgetProvider.setCalculatorData(calculatorData);

        // when
        calculatorWidgetProvider.onReceive(getContext(), intent);

        // then
        assertThat(calculatorWidgetProvider.getExpression(), equalTo(EVALUATED_VALUE));
        verify(expressionEvaluator, times(1)).evaluate(SCREEN_ACTUAL_VALUE);
    }

    public void testAddSignToExpression() {
        for (Object[] row : ADD_SIGN_TO_EXPRESSION_TEST_DATA) {
            verifyAddSymbolToExpression((int) row[BUTTON_ID], (String) row[SYMBOL], (String) row[EXPECTED]);
        }
    }

    private void verifyAddSymbolToExpression(int pressedButtonId, String actualExpression, String expectedExpression) {
        // given
        Intent intent = mock(Intent.class);
        CalculatorData calculatorData = Mockito.mock(CalculatorData.class);
        when(intent.getAction()).thenReturn(BUTTON_PRESS_ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt(CalculatorWidgetProvider.BUTTON_ID, pressedButtonId);
        when(intent.getExtras()).thenReturn(bundle);
        when(calculatorData.readExpression(getContext())).thenReturn(actualExpression);
        calculatorWidgetProvider.setCalculatorData(calculatorData);

        // when
        calculatorWidgetProvider.onReceive(getContext(), intent);

        // then
        assertThat(calculatorWidgetProvider.getExpression(), equalTo(expectedExpression));
    }
}