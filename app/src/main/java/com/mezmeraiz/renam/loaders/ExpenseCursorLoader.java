package com.mezmeraiz.renam.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.mezmeraiz.renam.db.DB;
import com.mezmeraiz.renam.db.DBHelper;

/**
 * Loader для загрузки данных из таблицы ExpenseTable.
 * Используется в EditPreparationFragment для заполнения ListView реагентами, которые содержит данный препарат
 * и в PreparationListFragment для списания реагентов
 */
public class ExpenseCursorLoader extends CursorLoader{

    private DB mDB;
    private String selection = "preparation_id = ?";
    private String [] selectionArgs;

    public ExpenseCursorLoader(Context context, DB db, long preparationID) {
        super(context);
        mDB = db;

        selectionArgs = new String[] { String.valueOf(preparationID) };
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = mDB.getCursor(DBHelper.TABLE_NAME_EXPENSE,null,selection,selectionArgs,null,null,DB.REAGENT_NAME);
        return cursor;
    }

}
