package com.mezmeraiz.renam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mezmeraiz.renam.R;
import java.util.ArrayList;
import java.util.Map;

/**
 * Адаптер для ListView из NewPreparationFragment и EditPreparationFragment
 */
public class NewPreparationAdapter extends BaseAdapter{

    private ArrayList<Map<String,Object>> mReagentList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int[] mTo;

    public NewPreparationAdapter(Context context, ArrayList<Map<String,Object>> reagentList,int[] to){
        mContext = context;
        mReagentList = reagentList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTo = to;
    }

    @Override
    public int getCount() {
        return mReagentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mReagentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) mReagentList.get(position).get("ID");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view==null){
            view = mInflater.inflate(R.layout.item_new_preparation_fragment,parent,false);
        }
        ((TextView) view.findViewById(mTo[0])).setText(String.valueOf(mReagentList.get(position).get("NAME")));
        ((TextView) view.findViewById(mTo[1])).setText(String.valueOf(mReagentList.get(position).get("EXPENSE")));
        return view;
    }
}
