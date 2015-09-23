package com.mezmeraiz.renam.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.mezmeraiz.renam.R;
import com.mezmeraiz.renam.db.DB;

/**
 * Адаптер для ListView из PreparationListFragment
 */
public class PreparationAdapter extends SimpleCursorAdapter{

    private LayoutInflater mInflater;
    private Cursor mCursor;
    private OnClickDialogListener mOnClickDialogListener;

    public PreparationAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, OnClickDialogListener onClickDialogListener) {
        super(context, layout, c, from, to, flags);
        mOnClickDialogListener = onClickDialogListener;
        mCursor = c;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mCursor.moveToPosition(position);
        View view = convertView;
        if (view==null) {
            view = mInflater.inflate(R.layout.item_preparation_list_fragment, parent, false);
        }
        TextView textViewName = (TextView) view.findViewById(R.id.textView_preparation_name);
        textViewName.setText(mCursor.getString(mCursor.getColumnIndex(DB.PREPARATION_NAME)));
        FrameLayout layout = (FrameLayout) view.findViewById(R.id.deduct_layout);
        layout.setTag(mCursor.getLong(mCursor.getColumnIndex("_id")));
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickDialogListener.onClickDialog((long) v.getTag());
            }
        });

        return  view;
    }

    public interface OnClickDialogListener{
        public void onClickDialog(long preparationID);
    }

}
