package com.mezmeraiz.renam.ui;

import android.app.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.mezmeraiz.renam.R;
import com.mezmeraiz.renam.adapters.NewPreparationAdapter;
import com.mezmeraiz.renam.db.DB;
import com.mezmeraiz.renam.loaders.ExpenseCursorLoader;
import com.mezmeraiz.renam.loaders.PreparationCursorLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;



/**
 * Фрагмент для редактирования препарата из PreparationListFragment
 */
public class EditPreparationFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,View.OnClickListener, AdapterView.OnItemClickListener{

    private long mPreparationID;
    private DB mDB;
    private ImageView mSaveButtonImageView, mAddButtonImageView, mDeleteButtonImageView;
    private EditText mEditTextPreparationDimension;
    private TextView mTextViewPreparationName;
    private ListView mListView;
    private int[] to = {R.id.textView_new_reagent_name,R.id.textView_new_reagent_expense};
    private ArrayList<Map<String,Object>> mReagentList;
    private NewPreparationAdapter mNewPreparationAdapter;
    public final static String NAME = "NAME";
    public final static String ID = "ID";
    public final static String EXPENSE = "EXPENSE";
    private final String SAVE_INSTANCE_PREPARATION_ID = "SAVE_INSTANCE_PREPARATION_ID";
    private final String SAVE_INSTANCE_PREPARATION_NAME_KEY = "SAVE_INSTANCE_PREPARATION_NAME_KEY";
    private final String SAVE_INSTANCE_PREPARATION_DIMENSION_KEY = "SAVE_INSTANCE_PREPARATION_DIMENSION_KEY";
    private final String SAVE_INSTANCE_REAGENT_LIST_KEY = "SAVE_INSTANCE_REAGENT_LIST_KEY";
    private String mPreparationName;
    private String mPreparationDimension;
    private final int EXPENSE_LOADER = 5;
    private final int PREPARATION_LOADER = 4;


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
        preparationActivity.getToolbar().setTitle(R.string.edit_preparation);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_preparation_fragment, null);
        mDB = DB.getDB();
        mDB.open(getActivity());
        mSaveButtonImageView = (ImageView) view.findViewById(R.id.imageView_save_edit_preparation);
        mAddButtonImageView = (ImageView) view.findViewById(R.id.imageView_add_new_reagent);
        mDeleteButtonImageView = (ImageView) view.findViewById(R.id.imageView_delete_preparation);
        mTextViewPreparationName = (TextView) view.findViewById(R.id.textView_edit_preparation_name);
        mEditTextPreparationDimension = (EditText) view.findViewById(R.id.editText_dimension);
        mListView = (ListView) view.findViewById(R.id.listView_edit_reagent);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mNewPreparationAdapter);
        registerForContextMenu(mListView);
        mAddButtonImageView.setOnClickListener(this);
        mSaveButtonImageView.setOnClickListener(this);
        mDeleteButtonImageView.setOnClickListener(this);
        getActivity().getSupportLoaderManager().initLoader(PREPARATION_LOADER, null, this);
        if (savedInstanceState == null){
            getActivity().getSupportLoaderManager().initLoader(EXPENSE_LOADER, null, this);
        }else{
            if (savedInstanceState.containsKey(SAVE_INSTANCE_PREPARATION_DIMENSION_KEY) &&
                    savedInstanceState.containsKey(SAVE_INSTANCE_PREPARATION_NAME_KEY) &&
                    savedInstanceState.containsKey(SAVE_INSTANCE_REAGENT_LIST_KEY) &&
                    savedInstanceState.containsKey(SAVE_INSTANCE_PREPARATION_ID)){
                mPreparationID = savedInstanceState.getLong(SAVE_INSTANCE_PREPARATION_ID);
                mTextViewPreparationName.setText(savedInstanceState.getString(SAVE_INSTANCE_PREPARATION_NAME_KEY));
                mEditTextPreparationDimension.setText(savedInstanceState.getString(SAVE_INSTANCE_PREPARATION_DIMENSION_KEY));
                mReagentList = (ArrayList<Map<String, Object>>) savedInstanceState.getSerializable(SAVE_INSTANCE_REAGENT_LIST_KEY);
                mNewPreparationAdapter = new NewPreparationAdapter(getActivity(),mReagentList,to);
                mListView.setAdapter(mNewPreparationAdapter);
            }
        }
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == NewPreparationFragment.ADD_FRAGMENT_REQUEST_CODE){
            addNewReagent(data.getStringExtra(NAME), data.getLongExtra(ID, 0), data.getIntExtra(EXPENSE, 0));
        }else if(resultCode == Activity.RESULT_OK && requestCode == NewPreparationFragment.EDIT_FRAGMENT_REQUEST_CODE){
            if (!data.getBooleanExtra(NewPreparationFragment.DELETE_STATE, false)){
                editOneReagent(data.getStringExtra(NAME), data.getLongExtra(ID, 0), data.getIntExtra(EXPENSE, 0));
            }else{
                removeOneReagent(data.getLongExtra(ID, 0));
            }
        }
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

    public void setData(long preparationID){
        mPreparationID = preparationID;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView_save_edit_preparation:
                mDB.deletePreparation(mPreparationID);
                mDB.deletePreparationExpense(mPreparationID);
                long id =  mDB.addPreparation(mTextViewPreparationName.getText().toString(),
                        Integer.valueOf(mEditTextPreparationDimension.getText().toString()));
                mDB.addExpense(id, mReagentList, Integer.valueOf(mEditTextPreparationDimension.getText().toString()));
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;
            case R.id.imageView_add_new_reagent:
                Intent intent = new Intent(getActivity(), AddReagentActivity.class);
                intent.setAction(AddReagentActivity.NEW_ADD_REAGENT_ACTIVITY_ACTION);
                startActivityForResult(intent, NewPreparationFragment.ADD_FRAGMENT_REQUEST_CODE);
                break;
            case R.id.imageView_delete_preparation:
                mDB.deletePreparation(mPreparationID);
                mDB.deletePreparationExpense(mPreparationID);
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();

                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (i == PREPARATION_LOADER){
            return new PreparationCursorLoader(getActivity(),mDB,mPreparationID);
        }else if(i == EXPENSE_LOADER){
            return new ExpenseCursorLoader(getActivity(),mDB,mPreparationID);
        }else{
            return null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SAVE_INSTANCE_PREPARATION_ID, mPreparationID);
        outState.putString(SAVE_INSTANCE_PREPARATION_NAME_KEY, mTextViewPreparationName.getText().toString());
        outState.putString(SAVE_INSTANCE_PREPARATION_DIMENSION_KEY, mEditTextPreparationDimension.getText().toString());
        outState.putSerializable(SAVE_INSTANCE_REAGENT_LIST_KEY, mReagentList);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        switch(cursorLoader.getId()){
            case PREPARATION_LOADER:
                if(cursor.moveToFirst()){
                    mPreparationName = cursor.getString(cursor.getColumnIndex(DB.PREPARATION_NAME));
                    mPreparationDimension = String.valueOf(cursor.getInt(cursor.getColumnIndex(DB.PREPARATION_DIMENSION)));
                    mTextViewPreparationName.setText(mPreparationName);
                    mEditTextPreparationDimension.setText(mPreparationDimension);
                }
                break;
            case EXPENSE_LOADER:
                if(cursor.moveToFirst()){
                    do{
                        Map<String,Object> oneReagentMap = new HashMap<String,Object>();
                        oneReagentMap.put(NAME,cursor.getString(cursor.getColumnIndex(DB.REAGENT_NAME)));
                        oneReagentMap.put(ID,cursor.getLong(cursor.getColumnIndex(DB.REAGENT_ID)));
                        oneReagentMap.put(EXPENSE,cursor.getInt(cursor.getColumnIndex(DB.EXPENSE)));
                        mReagentList.add(oneReagentMap);
                    }while(cursor.moveToNext());
                }
                mNewPreparationAdapter.notifyDataSetChanged();
                break;

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), AddReagentActivity.class);
        intent.setAction(AddReagentActivity.EDIT_ADD_REAGENT_ACTIVITY_ACTION);
        intent.putExtra(AddReagentActivity.REAGENT_ID, (long) mReagentList.get(position).get(ID));
        intent.putExtra(AddReagentActivity.REAGENT_NAME, (String) mReagentList.get(position).get(NAME));
        intent.putExtra(AddReagentActivity.REAGENT_EXPENSE, (int) mReagentList.get(position).get(EXPENSE));
        startActivityForResult(intent, NewPreparationFragment.EDIT_FRAGMENT_REQUEST_CODE);
    }
}
