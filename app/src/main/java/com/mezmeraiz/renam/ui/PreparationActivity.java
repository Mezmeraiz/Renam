package com.mezmeraiz.renam.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.mezmeraiz.renam.R;
import com.mezmeraiz.renam.db.DB;

/**
 * Активность-хост для фрагментов NewPreparationFragment и EditPreparationFragment
 */
public class PreparationActivity extends ToolbarActivity {

    private NewPreparationFragment mNewPreparationFragment;
    private EditPreparationFragment mEditPreparationFragment;
    private long mPreparationID;
    public static final String EDIT_PREPARATION_ACTIVITY_ACTION = "EDIT_REAGENT_ACTIVITY_ACTION";
    public static final String NEW_PREPARATION_ACTIVITY_ACTION = "NEW_REAGENT_ACTIVITY_ACTION";
    public static final String PREPARATION_ID = "PREPARATION_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.preparation_actvity);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState != null)
            return;
        Intent intent = getIntent();
        switch (intent.getAction()){
            case NEW_PREPARATION_ACTIVITY_ACTION:
                setNewPreparationFragment();
                break;
            case EDIT_PREPARATION_ACTIVITY_ACTION:
                mPreparationID = intent.getLongExtra(PREPARATION_ID, 0);
                setEditPreparationFragment(mPreparationID);
                break;
        }
    }

    private void setNewPreparationFragment(){
         mNewPreparationFragment = new NewPreparationFragment();
        if (mNewPreparationFragment.isAdded())
            return;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,mNewPreparationFragment)
                .commit();
    }


    private void setEditPreparationFragment(long preparationID){
        mEditPreparationFragment = new EditPreparationFragment();
        mEditPreparationFragment.setData(preparationID);
        if (mEditPreparationFragment.isAdded())
            return;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mEditPreparationFragment)
                .commit();
    }
}
