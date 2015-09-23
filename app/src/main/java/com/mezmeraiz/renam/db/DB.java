package com.mezmeraiz.renam.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mezmeraiz.renam.ui.EditPreparationFragment;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by pc on 04.06.2015.
 */
public class DB {

    public final static String REAGENT_NAME = "reagent_name";
    public final static String BALANCE = "balance";
    public final static String NORM = "norm";
    public final static String ID ="_id";

    public final static String PREPARATION_NAME = "preparation_name";

    public final static String PREPARATION_ID = "preparation_id";
    public final static String PREPARATION_DIMENSION = "preparation_dimension";
    public final static String REAGENT_ID = "reagent_id";
    public final static String EXPENSE = "expense";


    private SQLiteDatabase mSQLiteDatabase;
    private DBHelper mDBHelper;
    private static DB mDB;

    private DB (){

    }

    public static DB getDB(){
        if (mDB == null){
            mDB = new DB();
        }
        return mDB;
    }

    public void open(Context context){
        mDBHelper = new DBHelper(context);
        mSQLiteDatabase = mDBHelper.getWritableDatabase();
    }

    public void close(){
        if(mSQLiteDatabase!=null)
            mSQLiteDatabase.close();
        if(mDBHelper!=null)
            mDBHelper.close();
    }

    public Cursor getCursor(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        if (mSQLiteDatabase != null){
            return mSQLiteDatabase.query(table,columns,selection,selectionArgs,groupBy,having,orderBy);
        }else{
            return null;
        }
    }

    ////////////////////REAGENT TABLE/////////////////////////////////////////////////////////////////////////////////////

    public String[] getOneReagent(long id){
        String[] reagent = new String [3];
        String selection = "_id == ?";
        String [] selectionArgs = new String[] { String.valueOf(id) };
        Cursor cursor = mSQLiteDatabase.query(DBHelper.TABLE_NAME_REAGENT, null, selection, selectionArgs, null, null, null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                reagent[0] = cursor.getString(cursor.getColumnIndex(DB.REAGENT_NAME));
                reagent[1] = cursor.getString(cursor.getColumnIndex(DB.BALANCE));
                reagent[2] = cursor.getString(cursor.getColumnIndex(DB.NORM));
            }
        }

        return reagent;
    }

    public void updateReagent(long id, int balance, int norm){
        String selection = "_id == ?";
        String [] selectionArgs = new String[] { String.valueOf(id) };
        ContentValues cv = new ContentValues();
        cv.put(BALANCE,balance);
        cv.put(NORM,norm);
        mSQLiteDatabase.update(DBHelper.TABLE_NAME_REAGENT,cv,selection,selectionArgs);
    }

    public void deleteReagent(long id){
        mSQLiteDatabase.delete(DBHelper.TABLE_NAME_REAGENT,"_id = " + id, null);

    }

    public void addReagent(String name, int balance, int norm){
        ContentValues cv = new ContentValues();
        cv.put(REAGENT_NAME,name);
        cv.put(BALANCE,balance);
        cv.put(NORM,norm);
        mSQLiteDatabase.insert(DBHelper.TABLE_NAME_REAGENT,null,cv);
    }


    public void deductReagent(long reagentID, int expense){
        String raw = "UPDATE " + DBHelper.TABLE_NAME_REAGENT + " SET " + BALANCE + " = " + BALANCE + " - " + String.valueOf(expense) + " WHERE " + ID + " = " + String.valueOf(reagentID);
        Cursor cursor = mSQLiteDatabase.rawQuery(raw, null);
        cursor.moveToFirst();
    }

    /////////////////////PREPARATION TABLE/////////////////////////////////////////

    public long addPreparation(String preparationName, int preparationDimension){
        ContentValues cv = new ContentValues();
        cv.put(PREPARATION_NAME, preparationName);
        cv.put(PREPARATION_DIMENSION, preparationDimension);

        return mSQLiteDatabase.insert(DBHelper.TABLE_NAME_PREPARATION,null,cv);
    }

    public long deletePreparation(long id){
       return mSQLiteDatabase.delete(DBHelper.TABLE_NAME_PREPARATION,"_id = " + id, null);
    }


    //////////////////////EXPENSE TABLE/////////////////////////////////////////////

    public void addExpense(long preparationID,ArrayList<Map<String,Object>> reagentList, int dimension){
        for(int i = 0; i < reagentList.size(); i++){
            Map<String,Object> oneReagent = reagentList.get(i);
            long reagentID = (long) oneReagent.get(EditPreparationFragment.ID);
            int expense = (int) oneReagent.get(EditPreparationFragment.EXPENSE);
            String reagentName = (String) oneReagent.get(EditPreparationFragment.NAME);
            ContentValues cv = new ContentValues();
            cv.put(PREPARATION_ID,preparationID);
            cv.put(REAGENT_ID,reagentID);
            cv.put(EXPENSE,expense);
            cv.put(REAGENT_NAME,reagentName);
            cv.put(PREPARATION_DIMENSION,dimension);
            mSQLiteDatabase.insert(DBHelper.TABLE_NAME_EXPENSE, null,cv);
        }
    }

    public long deletePreparationExpense(long preparationID){
        return mSQLiteDatabase.delete(DBHelper.TABLE_NAME_EXPENSE,"preparation_id = " + preparationID, null);
    }

    public long deleteReagentExpense(long reagentID){
        return mSQLiteDatabase.delete(DBHelper.TABLE_NAME_EXPENSE,"reagent_id = " + reagentID, null);
    }

}
