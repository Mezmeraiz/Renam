package com.mezmeraiz.renam.ui;

import android.content.Intent;
import android.os.Bundle;
import com.mezmeraiz.renam.R;
import com.mezmeraiz.renam.db.DB;

/**
 * Активность-хост для фрагментов AddFragment и EditFragment
 */
public class AddReagentActivity extends ToolbarActivity {

    private AddFragment mAddFragment;
    private EditFragment mEditFragment;
    public static final String EDIT_ADD_REAGENT_ACTIVITY_ACTION = "EDIT_REAGENT_ACTIVITY_ACTION";
    public static final String NEW_ADD_REAGENT_ACTIVITY_ACTION = "NEW_REAGENT_ACTIVITY_ACTION";
    public static final String REAGENT_ID = "REAGENT_ID";
    public static final String REAGENT_EXPENSE = "REAGENT_EXPENSE";
    public static final String REAGENT_NAME = "REAGENT_NAME";
    private long mReagentID;
    private int mReagentExpense;
    private String mReagentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.add_reagent_activity);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState!=null)
            return;
        Intent intent = getIntent();
        switch (intent.getAction()){
            case NEW_ADD_REAGENT_ACTIVITY_ACTION:
                setAddFragment();
                break;
            case EDIT_ADD_REAGENT_ACTIVITY_ACTION:
                mReagentID = intent.getLongExtra(REAGENT_ID, 0);
                mReagentExpense = intent.getIntExtra(REAGENT_EXPENSE, 0);
                mReagentName = intent.getStringExtra(REAGENT_NAME);
                setEditFragment(mReagentID, mReagentExpense, mReagentName);
                break;
        }
    }

    private void setAddFragment(){
        mAddFragment = new AddFragment();
        if (mAddFragment.isAdded())
            return;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,mAddFragment)
                .commit();
    }

    private void setEditFragment(long reagentID, int reagentExpense, String reagentName){
        mEditFragment = new EditFragment();
        mEditFragment.setData(reagentID, reagentExpense, reagentName);
        if (mEditFragment.isAdded())
            return;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,mEditFragment)
                .commit();
    }

}
