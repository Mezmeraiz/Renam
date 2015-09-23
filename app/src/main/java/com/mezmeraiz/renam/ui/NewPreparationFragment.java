package com.mezmeraiz.renam.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.mezmeraiz.renam.R;
import com.mezmeraiz.renam.adapters.NewPreparationAdapter;
import com.mezmeraiz.renam.db.DB;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * Фрагмент для добавления нового препарата в PreparationListFragment
 */
public class NewPreparationFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ImageView mSaveButtonImageView, mAddButtonImageView;
    private EditText mEditTextPreparationName, mEditTextPreparationDimension;
    private ListView mListView;
    private DB mDB;
    private ArrayList<Map<String,Object>> mReagentList; // <<String name, long id, int expense>>
    private NewPreparationAdapter mNewPreparationAdapter;
    public final static String NAME = "NAME";
    public final static String ID = "ID";
    public final static String EXPENSE = "EXPENSE";
    public final static String DELETE_STATE = "DELETE_STATE";
    private final String SAVE_INSTANCE_REAGENT_LIST_KEY = "SAVE_INSTANCE_REAGENT_LIST_KEY";
    public final static int EDIT_FRAGMENT_REQUEST_CODE = 1;
    public final static int ADD_FRAGMENT_REQUEST_CODE = 2;
    private int[] to = {R.id.textView_new_reagent_name,R.id.textView_new_reagent_expense};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReagentList = new ArrayList<Map<String,Object>>();
        mNewPreparationAdapter = new NewPreparationAdapter(getActivity(),mReagentList,to);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PreparationActivity preparationActivity = (PreparationActivity) getActivity();
        preparationActivity.getToolbar().setTitle(R.string.new_preparation);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == ADD_FRAGMENT_REQUEST_CODE){
            addNewReagent(data.getStringExtra(NAME), data.getLongExtra(ID, 0), data.getIntExtra(EXPENSE, 0));
        }else if(resultCode == Activity.RESULT_OK && requestCode == EDIT_FRAGMENT_REQUEST_CODE){
            if (!data.getBooleanExtra(DELETE_STATE, false)){
                editOneReagent(data.getStringExtra(NAME), data.getLongExtra(ID, 0), data.getIntExtra(EXPENSE, 0));
            }else{
                removeOneReagent(data.getLongExtra(ID, 0));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_preparation_fragment, null);
        mDB = DB.getDB();
        mDB.open(getActivity());
        mSaveButtonImageView = (ImageView) view.findViewById(R.id.imageView_save_preparation);
        mAddButtonImageView = (ImageView) view.findViewById(R.id.imageView_add_new_reagent);
        mEditTextPreparationName = (EditText) view.findViewById(R.id.editText_preparation_name);
        mEditTextPreparationDimension = (EditText) view.findViewById(R.id.editText_dimension);
        mListView = (ListView) view.findViewById(R.id.listView_add_reagent);
        mListView.setAdapter(mNewPreparationAdapter);
        mListView.setOnItemClickListener(this);
        mAddButtonImageView.setOnClickListener(this);
        mSaveButtonImageView.setOnClickListener(this);
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVE_INSTANCE_REAGENT_LIST_KEY)){
            mReagentList = (ArrayList<Map<String, Object>>) savedInstanceState.getSerializable(SAVE_INSTANCE_REAGENT_LIST_KEY);
            mNewPreparationAdapter = new NewPreparationAdapter(getActivity(),mReagentList,to);
            mListView.setAdapter(mNewPreparationAdapter);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVE_INSTANCE_REAGENT_LIST_KEY, mReagentList);
    }

    public void addNewReagent(String name, long id, int expense){
        // Добавление нового реагента к списку
        Map<String,Object> oneReagent = new HashMap<String,Object>();
        oneReagent.put(NAME,name);
        oneReagent.put(ID,id);
        oneReagent.put(EXPENSE,expense);
        mReagentList.add(oneReagent);
        mNewPreparationAdapter.notifyDataSetChanged();
    }

    private void removeOneReagent(long id){
        ListIterator<Map<String,Object>> iterator = mReagentList.listIterator();
        while (iterator.hasNext()){
            Map<String,Object> oneReagentMap = iterator.next();
            if (oneReagentMap.get(ID) == id){
                iterator.remove();
            }
        }
        mNewPreparationAdapter.notifyDataSetChanged();
    }

    private void editOneReagent(String name, long id, int expense){
        ListIterator<Map<String,Object>> iterator = mReagentList.listIterator();
        while (iterator.hasNext()){
            Map<String,Object> oneReagentMap = iterator.next();
            if (oneReagentMap.get(ID) == id){
                oneReagentMap.clear();
                oneReagentMap.put(NAME, name);
                oneReagentMap.put(ID, id);
                oneReagentMap.put(EXPENSE, expense);
                iterator.set(oneReagentMap);
            }
        }
        mNewPreparationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView_save_preparation:
                long id =  mDB.addPreparation(mEditTextPreparationName.getText().toString(),
                        Integer.valueOf(mEditTextPreparationDimension.getText().toString()));
                mDB.addExpense(id, mReagentList, Integer.valueOf(mEditTextPreparationDimension.getText().toString()));
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;
            case R.id.imageView_add_new_reagent:
                Intent intent = new Intent(getActivity(), AddReagentActivity.class);
                intent.setAction(AddReagentActivity.NEW_ADD_REAGENT_ACTIVITY_ACTION);
                startActivityForResult(intent, ADD_FRAGMENT_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), AddReagentActivity.class);
        intent.setAction(AddReagentActivity.EDIT_ADD_REAGENT_ACTIVITY_ACTION);
        intent.putExtra(AddReagentActivity.REAGENT_ID, (long) mReagentList.get(position).get(ID));
        intent.putExtra(AddReagentActivity.REAGENT_NAME, (String) mReagentList.get(position).get(NAME));
        intent.putExtra(AddReagentActivity.REAGENT_EXPENSE, (int) mReagentList.get(position).get(EXPENSE));
        startActivityForResult(intent, EDIT_FRAGMENT_REQUEST_CODE);
    }


}
