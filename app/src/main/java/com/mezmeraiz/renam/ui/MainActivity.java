package com.mezmeraiz.renam.ui;

import android.os.Bundle;
import com.mezmeraiz.renam.R;
import com.mezmeraiz.renam.db.DB;
/**
 * Активность-хост для фрагментов ReagentListFragment и PreparationListFragment
 */

public class MainActivity extends NavigationDrawerActivity  {

    private ReagentListFragment mReagentListFragment;
    private PreparationListFragment mPreparationListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState != null)
            return;
        setPreparationListFragment();
    }

    private void setReagentListFragment(){
        mReagentListFragment = new ReagentListFragment();
        if (mReagentListFragment.isAdded())
            return;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,mReagentListFragment)
                .commit();
    }

    private void setPreparationListFragment(){
        mPreparationListFragment = new PreparationListFragment();
        if (mPreparationListFragment.isAdded())
            return;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,mPreparationListFragment)
                .commit();
    }

    @Override
    public void onClickReagent() {
        setReagentListFragment();
    }

    @Override
    public void onClickPreparation() {
        setPreparationListFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DB.getDB().close();
    }
}
