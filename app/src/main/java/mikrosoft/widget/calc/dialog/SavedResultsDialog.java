package mikrosoft.widget.calc.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;

import mikrosoft.widget.calc.CalculatorWidgetProvider;
import mikrosoft.widget.calc.R;

public class SavedResultsDialog {

    Context context = null;
    Dialog dialog;
    SavedResultsAdapter savedResultsAdapter = null;
    String header = "";

    public SavedResultsDialog(Context context) {
        this.context = context;
    }

    public void open() {
        dialog = new Dialog(context);
        View dialogView = null;
        try {
            dialogView = createAndInitDialogView();
        }
        catch(IOException e) {
            return;
        }
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                ((Activity)context).finish();
            }
        });
        dialog.setContentView(dialogView);
        dialog.setTitle(header);
        dialog.show();
    }

    private View createAndInitDialogView() throws IOException{
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.saved_results_dialog, null);
        initList(dialogView);
        return dialogView;
    }

    public void initList(View dialogView) throws IOException {
        ListView savedResultsList = (ListView) dialogView.findViewById(R.id.dialog_list);
        savedResultsAdapter = new SavedResultsAdapter(context);
        savedResultsList.setAdapter(savedResultsAdapter);
        savedResultsList.setOnItemClickListener(onListItemClickListener);
    }

    private AdapterView.OnItemClickListener onListItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String selectedValue = (String)savedResultsAdapter.getItem(position);
            Intent sendValueToCalc = new Intent(context, CalculatorWidgetProvider.class);
            sendValueToCalc.setAction(CalculatorWidgetProvider.ACTION_SELECTED_SAVED_RESULT);
            sendValueToCalc.putExtra(CalculatorWidgetProvider.EXTRA_SAVED_RESULT, selectedValue);
            context.sendBroadcast(sendValueToCalc);
            dialog.dismiss();
            ((Activity)context).finish();
        }
    };

    public void setHeader(String header) {
        this.header = header;
    }
}