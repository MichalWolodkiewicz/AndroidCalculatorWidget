package mikrosoft.widget.calc;

import android.content.Context;
import android.content.SharedPreferences;

public class CalculatorData {

    public static final String CALCULATOR_PREFERENCES = "calc.pref";
    public static final String SCREEN_EXPRESSION_KEY = "screen.value";

    public void saveExpression(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(CALCULATOR_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(SCREEN_EXPRESSION_KEY, value);
        editor.commit();
    }

    public String readExpression(Context context) {
        return context.getSharedPreferences(CALCULATOR_PREFERENCES, Context.MODE_PRIVATE).getString(SCREEN_EXPRESSION_KEY, "");
    }
}
