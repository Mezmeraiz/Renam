package com.mezmeraiz.renam.ui;

import android.content.Intent;
import android.os.Bundle;
import com.mezmeraiz.renam.R;

/**
 * Активность-хост для фргментов NewReagentFragment и EditReagentFragment
 */
public class ReagentActivity extends ToolbarActivity {

    private NewReagentFragment mNewReagentFragment;
    private EditReagentFragment mEditReagentFragment;
    public static final String EDIT_REAGENT_ACTIVITY_ACTION = "EDIT_REAGENT_ACTIVITY_ACTION";
    public static final String NEW_REAGENT_ACTIVITY_ACTION = "NEW_REAGENT_ACTIVITY_ACTION";
    public static final String REAGENT_ID = "REAGENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.reagent_activity);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState != null)
            return;
        Intent intent = getIntent();
        switch (intent.getAction()){
            case NEW_REAGENT_ACTIVITY_ACTION:
                setNewReagentFragment();
                break;
            case EDIT_REAGENT_ACTIVITY_ACTION:
                setEditReagentFragment(intent.getLongExtra(REAGENT_ID, 0));
                break;
        }
    }

    private void setNewReagentFragment(){
        mNewReagentFragment = new NewReagentFragment();
        if (mNewReagentFragment.isAdded())
            return;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,mNewReagentFragment)
                .commit();
    }


    private void setEditReagentFragment(long reagentID){
        mEditReagentFragment = new EditReagentFragment();
        mEditReagentFragment.setData(reagentID);
        if (mEditReagentFragment.isAdded())
            return;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,mEditReagentFragment)
                .commit();
    }
}
