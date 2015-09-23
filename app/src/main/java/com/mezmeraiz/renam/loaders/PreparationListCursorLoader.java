package com.mezmeraiz.renam.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import com.mezmeraiz.renam.db.DB;
import com.mezmeraiz.renam.db.DBHelper;

/**
 * Loader для загрузки данных из таблицы PreparationTable.
 * Используется в PreparationListFragment для заполнения ListView со списком препаратов
 */
public class PreparationListCursorLoader extends CursorLoader{

    private DB mDB;

    public PreparationListCursorLoader(Context context, DB db) {
        super(context);
        mDB = db;
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = mDB.getCursor(DBHelper.TABLE_NAME_PREPARATION,null,null,null,null,null,DB.PREPARATION_NAME);
        return cursor;
    }
}
