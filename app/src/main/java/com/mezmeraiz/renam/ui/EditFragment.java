package com.mezmeraiz.renam.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.mezmeraiz.renam.R;
import com.mezmeraiz.renam.db.DB;


/**
 * Фрагмент для редактирования реагента из EditPreparationFragment и NewPreparationFragment
 */
public class EditFragment extends Fragment {

    private ImageView mSaveButtonImageView, mDeleteButtonImageView;
    private EditText mEditTextExpense;
    private long mReagentID;
    private String mReagentName;
    private int mReagentExpense;
    private TextView mReagentNameTextView;
    private DB mDB;
    private final String SAVE_INSTANCE_REAGENT_ID = "SAVE_INSTANCE_PREPARATION_ID";
    private final String SAVE_INSTANCE_REAGENT_NAME_KEY = "SAVE_INSTANCE_PREPARATION_NAME_KEY";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AddReagentActivity addReagentActivity = (AddReagentActivity) getActivity();
        addReagentActivity.getToolbar().setTitle(R.string.edit_reagent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDB = DB.getDB();
        mDB.open(getActivity());
        if(savedInstanceState != null && savedInstanceState.containsKey(SAVE_INSTANCE_REAGENT_ID) &&
                savedInstanceState.containsKey(SAVE_INSTANCE_REAGENT_NAME_KEY)){
            mReagentID = savedInstanceState.getLong(SAVE_INSTANCE_REAGENT_ID);
            mReagentName = savedInstanceState.getString(SAVE_INSTANCE_REAGENT_NAME_KEY);
        }
        View view = inflater.inflate(R.layout.edit_fragment, null);
        mSaveButtonImageView = (ImageView) view.findViewById(R.id.imageViewSaveEditReagent);
        mDeleteButtonImageView = (ImageView) view.findViewById(R.id.imageViewDeleteReagent);
        mEditTextExpense = (EditText) view.findViewById(R.id.editTextExpense);
        mReagentNameTextView = (TextView) view.findViewById(R.id.textView_reagent_name);
        mReagentNameTextView.setText(mReagentName);
        mEditTextExpense.setText(String.valueOf(mReagentExpense));
        mSaveButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(NewPreparationFragment.DELETE_STATE, false);
                intent.putExtra(NewPreparationFragment.ID, mReagentID);
                intent.putExtra(NewPreparationFragment.NAME, mReagentName);
                intent.putExtra(NewPreparationFragment.EXPENSE, Integer.valueOf(mEditTextExpense.getText().toString()));
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        mDeleteButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(NewPreparationFragment.DELETE_STATE, true);
                intent.putExtra(NewPreparationFragment.ID, mReagentID);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SAVE_INSTANCE_REAGENT_ID, mReagentID);
        outState.putString(SAVE_INSTANCE_REAGENT_NAME_KEY, mReagentNameTextView.getText().toString());
    }

    public void setData(long reagentID, int reagentExpense, String reagentName){
        mReagentID = reagentID;
        mReagentExpense = reagentExpense;
        mReagentName = reagentName;
    }
}
