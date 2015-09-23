package com.mezmeraiz.renam.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Создание трех таблиц:
 * 1)ReagentTable - название реагента, текущий баланс и норма
 * 2)PreparationTable - название и объем препарата
 * 3)ExpenseTable - id, название и объем препарата; id, название и расход реагента на этот препарат
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DBRenam";
    public static final String TABLE_NAME_REAGENT = "ReagentTable";
    public static final String TABLE_NAME_PREPARATION = "PreparationTable";
    public static final String TABLE_NAME_EXPENSE = "ExpenseTable";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_REAGENT_TABLE = "CREATE TABLE ReagentTable (_id integer primary key autoincrement,reagent_name text, balance integer, norm integer);";
    private static final String CREATE_PREPARATION_TABLE = "CREATE TABLE PreparationTable (_id integer primary key autoincrement,preparation_name text, preparation_dimension integer); ";
    private static final String CREATE_EXPENSE_TABLE = "CREATE TABLE ExpenseTable (_id integer primary key autoincrement,preparation_id integer, reagent_id integer, expense integer, reagent_name text, preparation_dimension text); ";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_REAGENT_TABLE);
        db.execSQL(CREATE_PREPARATION_TABLE);
        db.execSQL(CREATE_EXPENSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
