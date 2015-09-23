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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import com.mezmeraiz.renam.R;
import com.mezmeraiz.renam.adapters.SpinnerAdapter;
import com.mezmeraiz.renam.db.DB;
import com.mezmeraiz.renam.loaders.SpinnerCursorLoader;


/**
 * Фрагмент для добавления нового реагента в EditPreparationFragment и NewPreparationFragment
 */
public class AddFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private ImageView mSaveButtonImageView;
    private EditText mEditTextExpense;
    private DB mDB;
    private Spinner mSpinner;
    private SpinnerAdapter mSpinnerAdapter;
    private final int SPINNER_LOADER = 1;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AddReagentActivity addReagentActivity = (AddReagentActivity) getActivity();
        addReagentActivity.getToolbar().setTitle(R.string.add_reagent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_fragment, null);
        mDB = DB.getDB();
        mDB.open(getActivity());
        mSaveButtonImageView = (ImageView) view.findViewById(R.id.imageViewSaveAddReagent);
        mEditTextExpense = (EditText) view.findViewById(R.id.editTextExpense);
        mSpinner = (Spinner) view.findViewById(R.id.spinner_add_reagent);
        getActivity().getSupportLoaderManager().initLoader(SPINNER_LOADER, null, this);
        mSaveButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = (Cursor) mSpinner.getSelectedItem();
                Intent intent = new Intent();
                intent.putExtra(NewPreparationFragment.ID,cursor.getLong(cursor.getColumnIndex(DB.ID)));
                intent.putExtra(NewPreparationFragment.NAME, cursor.getString(cursor.getColumnIndex(DB.REAGENT_NAME)));
                intent.putExtra(NewPreparationFragment.EXPENSE, Integer.valueOf(mEditTextExpense.getText().toString()));
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new SpinnerCursorLoader(getActivity(),mDB);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mSpinnerAdapter = new SpinnerAdapter(getActivity(),cursor);
        mSpinner.setAdapter(mSpinnerAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
