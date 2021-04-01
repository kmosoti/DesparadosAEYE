package com.example.desparadosaeye.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String USERS_TABLE = "Users_table";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_EMAILS = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_VALID = "VALID";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "users.db", null, 1);
    }
    //Specifically for Account Management
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE "+USERS_TABLE+"("+ COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAILS + " TEXT," + COLUMN_PASSWORD + " TEXT," + COLUMN_VALID + ", BOOLEAN)";

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

    public boolean check_valid_user(loginModel loginAttempt){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+COLUMN_VALID+" FROM "+USERS_TABLE + " WHERE " + COLUMN_EMAILS + " = " + "'" +loginAttempt.getEmail()+ "'" ;
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.getCount() == 0){
            return false;
        }
        else{
            return true;
        }
    }
}
