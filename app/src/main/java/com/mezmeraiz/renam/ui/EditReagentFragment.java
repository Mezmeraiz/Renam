package com.mezmeraiz.renam.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.mezmeraiz.renam.R;
import com.mezmeraiz.renam.db.DB;

/**
 * Фрагмент для редактирования реагента из ReagentListFragment
 */
public class EditReagentFragment extends Fragment implements View.OnClickListener{

    private ImageView mSaveButtonImageView,mDeleteButtonImageView;
    private EditText  mEditTextBalance, mEditTextNorm;
    private TextView mTextViewName;
    private DB mDB;
    private long mReagentID;
    private final String ID = "ID";

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ReagentActivity reagentActivity = (ReagentActivity) getActivity();
        reagentActivity.getToolbar().setTitle(R.string.edit_reagent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState!=null && savedInstanceState.containsKey(ID)){
            mReagentID = savedInstanceState.getLong(ID);
        }
        mDB = DB.getDB();
        mDB.open(getActivity());
        View view = inflater.inflate(R.layout.edit_reagent_fragment, null);
        mSaveButtonImageView = (ImageView) view.findViewById(R.id.imageViewEditSave);
        mDeleteButtonImageView = (ImageView) view.findViewById(R.id.imageViewEditDelete);
        mTextViewName = (TextView) view.findViewById(R.id.editTextNameEdit);
        mEditTextBalance = (EditText) view.findViewById(R.id.editTextBalanceEdit);
        mEditTextNorm = (EditText) view.findViewById(R.id.editTextNormEdit);
        String[] oneReagent = mDB.getOneReagent(mReagentID);
        mTextViewName.setText(oneReagent[0]);
        mEditTextBalance.setText(oneReagent[1]);
        mEditTextNorm.setText(oneReagent[2]);
        mDeleteButtonImageView.setOnClickListener(this);
        mSaveButtonImageView.setOnClickListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ID, mReagentID);
    }

    public void setData(long reagentID){
        mReagentID = reagentID;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewEditSave:
                mDB.updateReagent(mReagentID, Integer.valueOf((mEditTextBalance.getText().toString())),
                        Integer.valueOf(mEditTextNorm.getText().toString()));
                Toast.makeText(getActivity(), R.string.reagent_saved, Toast.LENGTH_SHORT).show();
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;
            case R.id.imageViewEditDelete:
                mDB.deleteReagent(mReagentID);
                mDB.deleteReagentExpense(mReagentID);
                Toast.makeText(getActivity(), R.string.reagent_deleted, Toast.LENGTH_SHORT).show();
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;
        }
    }
}
