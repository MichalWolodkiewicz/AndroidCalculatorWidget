package mikrosoft.widget.calc;

import android.content.Context;
import android.content.SharedPreferences;

public class CalculatorData {

    public static final String CALCULATOR_PREFERENCES = "calc.pref";
    public static final String SCREEN_EXPRESSION_KEY = "screen.value";
    private static final String RESULTS_KEY = "result.key";
    private static final String SEPARATOR = "|";
    public static final int MAX_SAVED_RESULTS = 10;

    public void saveExpression(Context context, String value) {
        saveValue(context, SCREEN_EXPRESSION_KEY, value);
    }

    public String readExpression(Context context) {
        return getSharedPreferences(context).getString(SCREEN_EXPRESSION_KEY, "");
    }

    public void saveResult(Context context, String result) {
        String resultsSequence = getSharedPreferences(context).getString(RESULTS_KEY, "");
        if (resultsSequence.length() > 0) {
            if (resultsSequence.contains(SEPARATOR)) {
                resultsSequence = removeLastResultIfNeeded(resultsSequence);
            }
            resultsSequence = result + SEPARATOR + resultsSequence;
            saveValue(context, RESULTS_KEY, resultsSequence);
        } else {
            saveValue(context, RESULTS_KEY, result);
        }
    }

    private void saveValue(Context context, String resultsKey, String result) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(resultsKey, result);
        editor.commit();
    }

    private String removeLastResultIfNeeded(String resultsSequence) {
        int savedResultsCount = resultsSequence.length() - resultsSequence.replace(SEPARATOR, "").length() + 1;
        if (savedResultsCount == MAX_SAVED_RESULTS) {
            resultsSequence = resultsSequence.substring(0, resultsSequence.lastIndexOf(SEPARATOR));
        }
        return resultsSequence;
    }

    public String[] getSavedResults(Context context) {
        String resultsSequence = getSharedPreferences(context).getString(RESULTS_KEY, "");
        String[] results = new String[0];
        if (resultsSequence.length() > 0) {
            if (resultsSequence.contains(SEPARATOR)) {
                results = resultsSequence.split("\\"+SEPARATOR);
            } else {
                results = new String[]{resultsSequence};
            }
        }
        return results;
    }

    public void removeAllSavedResults(Context context) {
        saveValue(context, RESULTS_KEY, "");
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(CALCULATOR_PREFERENCES, Context.MODE_PRIVATE);
    }
}
