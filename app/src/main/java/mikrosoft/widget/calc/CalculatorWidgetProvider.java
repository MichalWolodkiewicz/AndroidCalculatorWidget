package mikrosoft.widget.calc;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import mikrosoft.widget.calc.dialog.DialogActivity;


public class CalculatorWidgetProvider extends AppWidgetProvider {

    public static final String BUTTON_PRESS_ACTION = "button.press.action";
    public static final String BUTTON_ID = "button.id";
    public static final String EVALUATE_ERROR = "Error";
    public static final String EXTRA_SAVED_RESULT = "extra.result";
    public static final String ACTION_SELECTED_SAVED_RESULT = "action.selected.saved.result";
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
        ComponentName componentName = new ComponentName(context, CalculatorWidgetProvider.class);
        appWidgetManager.updateAppWidget(componentName, remoteViews);
    }

    private void setPendingIntentsToButtons(Context context, RemoteViews remoteViews, int[] widgetIds) {
        for (Integer button : CalculatorButtons.getButtonsIds()) {
            remoteViews.setOnClickPendingIntent(button, getButtonPendingIntent(context, button, widgetIds));
        }
        remoteViews.setOnClickPendingIntent(R.id.calculator_screen, getScreenPendingIntent(context));
    }

    private PendingIntent getScreenPendingIntent(Context context) {
        Intent dialogActivityIntent = new Intent(context, DialogActivity.class);
        dialogActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, 0, dialogActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getButtonPendingIntent(Context context, int buttonId, int[] widgetIds) {
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
        } else if (intent.getAction().equals(ACTION_SELECTED_SAVED_RESULT)) {
            processSavedResult(context, intent);
        }
    }

    private void processButtonClick(Context context, Intent intent) {
        expression = calculatorData.readExpression(context);
        int buttonId = intent.getExtras().getInt(BUTTON_ID);
        if (buttonId == R.id.reset) {
            resetExpression(context);
        } else if (buttonId == R.id.clear_one) {
            clearOneSign(context);
        } else if (buttonId == R.id.equals && expression.length() > 0) {
            evaluateExpression();
            calculatorData.saveResult(context, expression);
        } else {
            addSymbolToExpression(buttonId);
        }
        saveExpression(context);
        updateWidgets(context, intent);
    }

    private void processSavedResult(Context context, Intent intent) {
        String savedResult = intent.getStringExtra(EXTRA_SAVED_RESULT);
        expression = calculatorData.readExpression(context);
        expression += savedResult;
        onUpdate(context, AppWidgetManager.getInstance(context), null);
        calculatorData.saveExpression(context, expression);
    }

    private void resetExpression(Context context) {
        expression = "";
        calculatorData.saveExpression(context, "");
    }

    private void clearOneSign(Context context) {
        if (expression.length() > 0) {
            expression = expression.substring(0, expression.length() - 1);
            if (expression.length() == 0) {
                calculatorData.saveExpression(context, "");
            }
        }
    }

    private void updateWidgets(Context context, Intent intent) {
        int[] widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        onUpdate(context, appWidgetManager, widgetIds);
    }

    private void saveExpression(Context context) {
        if (expression.equals(EVALUATE_ERROR)) {
            calculatorData.saveExpression(context, "");
        } else if (expression.length() > 0) {
            calculatorData.saveExpression(context, expression);
        }
    }

    private void evaluateExpression() {
        try {
            expression = expressionEvaluator.evaluate(expression);
        } catch (Exception e) {
            expression = EVALUATE_ERROR;
        }
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
