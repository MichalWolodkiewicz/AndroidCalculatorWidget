package mikrosoft.widget.calc.dialog;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.IOException;

import mikrosoft.widget.calc.CalculatorData;

public class SavedResultsAdapter implements ListAdapter {

    String[] savedResults = null;
    Context context = null;

    public SavedResultsAdapter(Context context) throws IOException {
        this.context = context;
        savedResults = new CalculatorData().getSavedResults(context);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return savedResults.length;
    }

    @Override
    public Object getItem(int position) {
        return savedResults[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView != null ? convertView : createListViewElement(position);
    }

    private View createListViewElement(int position) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView listViewElement = (TextView)layoutInflater.inflate(android.R.layout.simple_dropdown_item_1line, null);
        listViewElement.setTextColor(Color.WHITE);
        listViewElement.setText(savedResults[position]);
        return listViewElement;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
