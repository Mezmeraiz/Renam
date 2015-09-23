package com.mezmeraiz.renam.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.mezmeraiz.renam.R;

/**
 * Абстрактная активность с NavigationDrawer
 */
public abstract class NavigationDrawerActivity extends ToolbarActivity {

        protected DrawerLayout mDrawerLayout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            String[] mDrawerArray = getResources().getStringArray(R.array.drawer_array);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.setDrawerListener(new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.nd_open,R.string.nd_close ));
            ListView mDrawerListView = (ListView) findViewById(R.id.left_drawer);
            mDrawerListView.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mDrawerArray));
            mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            onClickReagent();
                            mDrawerLayout.closeDrawers();
                            break;
                        case 1:
                            onClickPreparation();
                            mDrawerLayout.closeDrawers();
                            break;
                    }
                }
            });
        }

    public abstract void onClickReagent();

    public abstract void onClickPreparation();
}

