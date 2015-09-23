package com.mezmeraiz.renam.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.mezmeraiz.renam.R;
import com.mezmeraiz.renam.db.DB;

/**
 * Фрагмент для добавления нового реагента в ReagentListFragment
 */
public class NewReagentFragment extends Fragment {

    private ImageView mSaveButtonImageView;
    private EditText mEditTextName, mEditTextBalance, mEditTextNorm;
    private DB mDB;

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ReagentActivity reagentActivity = (ReagentActivity) getActivity();
        reagentActivity.getToolbar().setTitle(R.string.new_reagent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_reagent_fragment, null);
        mSaveButtonImageView = (ImageView) view.findViewById(R.id.imageViewSaveReagent);
        mEditTextName = (EditText) view.findViewById(R.id.editTextName);
        mEditTextBalance = (EditText) view.findViewById(R.id.editTextBalance);
        mEditTextNorm = (EditText) view.findViewById(R.id.editTextNorm);
        mDB = DB.getDB();
        mDB.open(getActivity());
        mSaveButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDB.addReagent(mEditTextName.getText().toString(),
                        Integer.valueOf((mEditTextBalance.getText().toString())),
                        Integer.valueOf(mEditTextNorm.getText().toString()));
                Toast.makeText(getActivity(), R.string.saved, Toast.LENGTH_SHORT).show();
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        return view;
    }


}
