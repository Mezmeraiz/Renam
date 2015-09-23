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
import android.widget.AdapterView;
import android.widget.ListView;
import com.mezmeraiz.renam.R;
import com.mezmeraiz.renam.adapters.ReagentAdapter;
import com.mezmeraiz.renam.animation.AutoHideableLayout;
import com.mezmeraiz.renam.db.DB;
import com.mezmeraiz.renam.loaders.ReagentListCursorLoader;

/**
 * Фрагмент со списком реагентов
 */
public class ReagentListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView mListView;
    private DB mDB;
    private ReagentAdapter mReagentAdapter;
    private int [] to = {R.id.textView_reagent_name,R.id.textView_reagent_balance};
    private String [] from = {DB.REAGENT_NAME,DB.BALANCE};
    private final int REAGENT_LIST_LOADER = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.getToolbar().setTitle(R.string.reagent_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mDB = DB.getDB();
        mDB.open(getActivity());
        View view = inflater.inflate(R.layout.reagent_list_fragment,null);
        AutoHideableLayout animButton = (AutoHideableLayout) view.findViewById(R.id.anim_button_reagent); // Кнопка с анимацией, по нажатии ставим фрагмент с друзьями( будет с сообщениями когда доделают)
        animButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReagentActivity.class);
                intent.setAction(ReagentActivity.NEW_REAGENT_ACTIVITY_ACTION);
                startActivityForResult(intent,0);
            }
        });

        mListView = (ListView) view.findViewById(R.id.listView_list_reagent);
        mListView.setOnTouchListener(animButton);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ReagentActivity.class);
                intent.setAction(ReagentActivity.EDIT_REAGENT_ACTIVITY_ACTION);
                intent.putExtra(ReagentActivity.REAGENT_ID, id);
                startActivityForResult(intent,0);
            }
        });
        getActivity().getSupportLoaderManager().initLoader(REAGENT_LIST_LOADER, null, this);
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            getActivity().getSupportLoaderManager().getLoader(REAGENT_LIST_LOADER).forceLoad();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new ReagentListCursorLoader(getActivity(),mDB);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mReagentAdapter = new ReagentAdapter(getActivity(),R.layout.item_reagent_list_fragment,cursor,from,to,0);
        mListView.setAdapter(mReagentAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getActivity().getSupportLoaderManager().getLoader(REAGENT_LIST_LOADER) != null){
            getActivity().getSupportLoaderManager().destroyLoader(REAGENT_LIST_LOADER);
        }
    }
}
