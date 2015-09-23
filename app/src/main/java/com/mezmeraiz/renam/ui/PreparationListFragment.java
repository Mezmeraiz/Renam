package com.mezmeraiz.renam.ui;


import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.mezmeraiz.renam.R;
import com.mezmeraiz.renam.adapters.PreparationAdapter;
import com.mezmeraiz.renam.animation.AutoHideableLayout;
import com.mezmeraiz.renam.db.DB;
import com.mezmeraiz.renam.loaders.ExpenseCursorLoader;
import com.mezmeraiz.renam.loaders.PreparationListCursorLoader;
import java.util.ArrayList;
import java.util.Map;

/**
 * Фрагмент со списком препаратов
 */
public class PreparationListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, PreparationAdapter.OnClickDialogListener, View.OnClickListener{

    private DB mDB;
    private PreparationAdapter mPreparationAdapter;
    private int [] to = {R.id.textView_preparation_name};
    private String [] from = {DB.PREPARATION_NAME};
    private ListView mListView;
    private EditText mDialogEditText;
    private Button mOKDialogButton, mCancelDialogButton;
    private Dialog mDialog;
    private ExpenseCursorLoader mExpenseCursorLoader;
    private long mCurrentPreparationID;
    private float mCurrentDimension;
    private final int PREPARATION_LIST_LOADER = 3;
    private final int EXPENSE_LOADER = 2;


    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.getToolbar().setTitle(R.string.preparation_list);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            getActivity().getSupportLoaderManager().getLoader(PREPARATION_LIST_LOADER).forceLoad();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.preparation_list_fragment,null);
        AutoHideableLayout animButton = (AutoHideableLayout) view.findViewById(R.id.anim_button_preparation); // Кнопка с анимацией, по нажатии ставим фрагмент с друзьями( будет с сообщениями когда доделают)
        animButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PreparationActivity.class);
                intent.setAction(PreparationActivity.NEW_PREPARATION_ACTIVITY_ACTION);
                startActivityForResult(intent, 0);
            }
        });

        mListView = (ListView) view.findViewById(R.id.listView_list_preparation);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PreparationActivity.class);
                intent.setAction(PreparationActivity.EDIT_PREPARATION_ACTIVITY_ACTION);
                intent.putExtra(PreparationActivity.PREPARATION_ID, id);
                startActivityForResult(intent, 0);
            }
        });
        mListView.setOnTouchListener(animButton);
        getActivity().getSupportLoaderManager().initLoader(PREPARATION_LIST_LOADER, null, this);

        return view;

    }

    public void initLoader(long preparationID, float currentDimension){
        //Запуск загрузчика для списания реагентов данного препарата
        mCurrentPreparationID = preparationID;
        mCurrentDimension = currentDimension;
        getActivity().getSupportLoaderManager().initLoader(EXPENSE_LOADER, null, this);
    }

    public void deduct( Cursor expenseCursor){
        // Списание реагентов выбранного препарата пропорционально введенному объему из
        // таблицы ReagentTable
        if (expenseCursor.moveToFirst()){
            float dimensionIndex = mCurrentDimension / expenseCursor.getFloat(expenseCursor.getColumnIndex(DB.PREPARATION_DIMENSION));
            do{
                float expense = expenseCursor.getFloat(expenseCursor.getColumnIndex(DB.EXPENSE)) * dimensionIndex;
                long reagentID = expenseCursor.getLong(expenseCursor.getColumnIndex(DB.REAGENT_ID));
                mDB = DB.getDB();
                mDB.deductReagent(reagentID,(int) expense);
            }while(expenseCursor.moveToNext());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        mDB = DB.getDB();
        mDB.open(getActivity());
        if (i == PREPARATION_LIST_LOADER){
            return new PreparationListCursorLoader(getActivity(),mDB);
        }else if(i == EXPENSE_LOADER){
            mExpenseCursorLoader = new ExpenseCursorLoader(getActivity(),mDB, mCurrentPreparationID);
            return mExpenseCursorLoader;
        }else{
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        switch (cursorLoader.getId()){
            case PREPARATION_LIST_LOADER:
                mPreparationAdapter = new PreparationAdapter(getActivity(), R.layout.item_preparation_list_fragment, cursor, from, to, 0, this);
                mListView.setAdapter(mPreparationAdapter);
                break;
            case EXPENSE_LOADER:
                deduct(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onClickDialog(long preparationID) {
        mDialog = new Dialog(getActivity());
        mDialog.setTitle(R.string.deduct_reagents);
        mDialog.setContentView(R.layout.dimension_dialog);
        mDialogEditText = (EditText) mDialog.findViewById(R.id.editText_dimension);
        mOKDialogButton = (Button) mDialog.findViewById(R.id.ok_button);
        mOKDialogButton.setOnClickListener(this);
        mOKDialogButton.setTag(preparationID);
        mCancelDialogButton = (Button) mDialog.findViewById(R.id.cancel_button);
        mCancelDialogButton.setOnClickListener(this);
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok_button:
                initLoader((long) v.getTag(), Float.valueOf(mDialogEditText.getText().toString()));
                mDialog.cancel();
                Toast.makeText(getActivity(),R.string.reagents_deducted,Toast.LENGTH_SHORT).show();
                break;
            case R.id.cancel_button:
                mDialog.cancel();
                break;
        }
    }


}













