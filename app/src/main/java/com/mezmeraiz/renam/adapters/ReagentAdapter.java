package com.mezmeraiz.renam.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.mezmeraiz.renam.R;
import com.mezmeraiz.renam.db.DB;

/**
 * Адаптер для ListView из ReagentListFragment
 */
public class ReagentAdapter extends SimpleCursorAdapter {

    private LayoutInflater mInflater;
    private Cursor mCursor;
    private int mBalance;
    private int mNorm;

    public ReagentAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mCursor = c;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mCursor.moveToPosition(position);
        View view = convertView;
        if (view==null) {
            view = mInflater.inflate(R.layout.item_reagent_list_fragment, parent, false);
        }
        TextView textViewName = (TextView) view.findViewById(R.id.textView_reagent_name);
        TextView textViewBalance = (TextView) view.findViewById(R.id.textView_reagent_balance);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.reagentListLayout);
        mBalance = mCursor.getInt(mCursor.getColumnIndex(DB.BALANCE));
        mNorm = mCursor.getInt(mCursor.getColumnIndex(DB.NORM));
        textViewName.setText(mCursor.getString(mCursor.getColumnIndex(DB.REAGENT_NAME)));
        textViewBalance.setText(String.valueOf(mBalance));
        if (mBalance < mNorm)
            layout.setBackgroundColor(Color.parseColor("#FFC0CB"));
        if (mBalance <= 0){
            layout.setBackgroundColor(Color.parseColor("#FF0000"));
            textViewBalance.setText("0");
        }

        return  view;
    }
}
