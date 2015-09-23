package com.mezmeraiz.renam.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.mezmeraiz.renam.db.DB;
import com.mezmeraiz.renam.db.DBHelper;

/**
 * Loader для загрузки данных из таблицы ReagentTable.
 * Используется в AddFragment для заполнения Spinner со списком реагентов
 */
public class SpinnerCursorLoader extends CursorLoader {

    private DB mDB;

    public SpinnerCursorLoader(Context context, DB db) {
        super(context);
        mDB = db;
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = mDB.getCursor(DBHelper.TABLE_NAME_REAGENT,null,null,null,null,null,null);
        return cursor;
    }
}
