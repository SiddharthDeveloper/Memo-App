package com.siddharth.memoapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.siddharth.memoapp.Model.Memo;
import com.siddharth.memoapp.Util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context ctx;


    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MEMO_TABLE = " CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY ," + Constants.KEY_MEMO_NAME + " TEXT ,"
                + Constants.KEY_DATE_ADD + " LONG);";

        db.execSQL(CREATE_MEMO_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(db);
    }

    /**
     * TODO: CRUD OPERATIONS: Create, Read, Update, Delete Methods...
     */
    public void addMemo(Memo memo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.KEY_MEMO_NAME, memo.getTask());
        values.put(Constants.KEY_DATE_ADD, java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME, null, values);
    }


    public Memo getMemo(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID,
                        Constants.KEY_MEMO_NAME, Constants.KEY_DATE_ADD},
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)

            cursor.moveToFirst();


        Memo memo = new Memo();
        memo.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        memo.setTask(cursor.getString(cursor.getColumnIndex(Constants.KEY_MEMO_NAME)));

        //convert timestamp to something readable
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_ADD)))
                .getTime());

        memo.setDateAdded(formatedDate);


        return memo;
    }


    public List<Memo> getAllMemo() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Memo> memoList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
                Constants.KEY_ID, Constants.KEY_MEMO_NAME,
                Constants.KEY_DATE_ADD}, null, null, null, null, Constants.KEY_DATE_ADD + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Memo memo = new Memo();
                memo.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                memo.setTask(cursor.getString(cursor.getColumnIndex(Constants.KEY_MEMO_NAME)));

                //convert timestamp to something readable
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_ADD)))
                        .getTime());

                memo.setDateAdded(formatedDate);

                // Add to the memoList
                memoList.add(memo);

            } while (cursor.moveToNext());
        }

        return memoList;
    }

    public int updateMemo(Memo memo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_MEMO_NAME, memo.getTask());
        values.put(Constants.KEY_DATE_ADD, java.lang.System.currentTimeMillis());//get system time


        //update row
        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[]{String.valueOf(memo.getId())});
    }


    public void deleteMemo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.close();

    }


    public int getMemoCount() {
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }


}
