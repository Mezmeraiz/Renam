package com.mezmeraiz.renam.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.mezmeraiz.renam.db.DB;
import com.mezmeraiz.renam.db.DBHelper;

/**
 * Loader для загрузки данных из таблицы PreparationTable.
 * Используется в EditPreparationFragment для получения названия и объема препарата по его id
 */
public class PreparationCursorLoader extends CursorLoader{

    private DB mDB;
    private String selection = "_id == ?";
    private String [] selectionArgs;

    public PreparationCursorLoader(Context context, DB db, long preparationID) {

        super(context);
        mDB = db;
        selectionArgs  = new String[] { String.valueOf(preparationID) };
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = mDB.getCursor(DBHelper.TABLE_NAME_PREPARATION,null,selection,selectionArgs,null,null,null);
        return cursor;
    }

}
