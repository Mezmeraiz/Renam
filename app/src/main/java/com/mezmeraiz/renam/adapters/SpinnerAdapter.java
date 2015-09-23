package com.mezmeraiz.renam.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mezmeraiz.renam.R;
import com.mezmeraiz.renam.db.DB;


/**
 * Адаптер для Spinner Из AddFragment
 */
public class SpinnerAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Cursor mCursor;

    public SpinnerAdapter(Context context, Cursor cursor){
        mCursor = cursor;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        mCursor.moveToPosition(position);
        return mCursor;
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getLong(mCursor.getColumnIndex(DB.ID));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mCursor.moveToPosition(position);
        View view = convertView;
        if (view==null){
            view = mInflater.inflate(R.layout.item_spinner,parent,false);
        }
        ((TextView) view.findViewById(R.id.textView_spinner)).setText(mCursor.getString(mCursor.getColumnIndex(DB.REAGENT_NAME)));

        return view;
    }

}
