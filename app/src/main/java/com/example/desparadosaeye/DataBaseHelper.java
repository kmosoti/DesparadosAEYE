package com.example.desparadosaeye;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String USERS_TABLE = "Users_table";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_EMAILS = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE "+USERS_TABLE+"("+ COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAILS + " TEXT," + COLUMN_PASSWORD + " TEXT)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addUser(loginModel newLogin){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EMAILS, loginModel.getEmail());
        cv.put(COLUMN_PASSWORD, loginModel.getPassword());

        long insert = db.insert(USERS_TABLE,null,cv);
        if(insert == -1){
            return false;
        }
        else {
            return true;
        }
    }
}
