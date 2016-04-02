package mikrosoft.widget.calc.dialog;

import android.app.Activity;
import android.os.Bundle;

import mikrosoft.widget.calc.R;

public class DialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);
        new SavedResultsDialog(this).open();
    }
}
