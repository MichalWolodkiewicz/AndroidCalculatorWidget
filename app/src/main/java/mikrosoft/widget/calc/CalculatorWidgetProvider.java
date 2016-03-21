package mikrosoft.widget.calc;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


public class CalculatorWidgetProvider extends AppWidgetProvider {

    public static final String BUTTON_PRESS_ACTION = "button.press.action";
    public static final String BUTTON_ID = "button.id";
    private final String PACKAGE_NAME = "calc.widget.android.mikrosoft.calculatorwidget";
    private static final String EMPTY_EXPRESSION = "";
    private static String expression = "";
    private RemoteViews remoteViews;
    private CalculatorData calculatorData;
    private ExpressionEvaluator expressionEvaluator;

    public CalculatorWidgetProvider() {
        remoteViews = new RemoteViews(PACKAGE_NAME, R.layout.widget_layout);
        calculatorData = new CalculatorData();
        expressionEvaluator = new ExpressionEvaluator();
    }

    @Override
    public void onEnabled(Context context) {
        calculatorData.saveExpression(context, EMPTY_EXPRESSION);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        setPendingIntentsToButtons(context, remoteViews, appWidgetIds);
        remoteViews.setTextViewText(R.id.calculator_screen, expression);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    private void setPendingIntentsToButtons(Context context, RemoteViews remoteViews, int[] widgetIds) {
        for (Integer button : CalculatorButtons.getButtonsIds()) {
            remoteViews.setOnClickPendingIntent(button, getPendingIntent(context, button, widgetIds));
        }
    }

    private PendingIntent getPendingIntent(Context context, int buttonId, int[] widgetIds) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(BUTTON_PRESS_ACTION);
        intent.putExtra(BUTTON_ID, buttonId);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
        return PendingIntent.getBroadcast(context, buttonId, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(BUTTON_PRESS_ACTION)) {
            processButtonClick(context, intent);
        }
    }

    private void processButtonClick(Context context, Intent intent) {
        expression = calculatorData.readExpression(context);
        int buttonId = intent.getExtras().getInt(BUTTON_ID);
        if (buttonId == R.id.reset) {
            expression = "";
        } else if (buttonId == R.id.equals) {
            expression = expressionEvaluator.evaluate(expression);
        } else {
            addSymbolToExpression(buttonId);
        }
        calculatorData.saveExpression(context, expression);
        int[] widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        onUpdate(context, appWidgetManager, widgetIds);
    }

    private void addSymbolToExpression(int buttonId) {
        expression += getSymbolIfShouldBeAdded(buttonId);
    }

    private String getSymbolIfShouldBeAdded(int buttonId) {
        String[] buttonConstraints = CalculatorButtons.getButtonConstraints(buttonId);
        String symbol = "";
        if (buttonConstraints == null || symbolShouldBeAddedToExpression(buttonConstraints)) {
            symbol = CalculatorButtons.getButtonSymbol(buttonId);
        }
        return symbol;
    }

    private boolean symbolShouldBeAddedToExpression(String[] illegalEndings) {
        for (String illegalEnding : illegalEndings) {
            if (expression.matches(illegalEnding)) {
                return false;
            }
        }
        return true;
    }

    public void setRemoteViews(RemoteViews remoteViews) {
        this.remoteViews = remoteViews;
    }

    public String getExpression() {
        return expression;
    }

    public void setCalculatorData(CalculatorData calculatorData) {
        this.calculatorData = calculatorData;
    }

    public void setExpressionEvaluator(ExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }
}
