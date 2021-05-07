package com.example.desparadosaeye.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import com.example.desparadosaeye.data.database.User;


import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String USERS_TABLE = "Users_table";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_EMAILS = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_FIRSTNAME = "FIRSTNAME";
    public static final String COLUMN_LASTNAME = "LASTNAME";
    public static final String COLUMN_VALID = "VALID";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "users.db", null, 1);
    }
    //Specifically for Account Management
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE "+USERS_TABLE+"("+ COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAILS + " TEXT," + COLUMN_PASSWORD + " TEXT," + COLUMN_FIRSTNAME + " TEXT," + COLUMN_LASTNAME + " TEXT," + COLUMN_VALID + ", BOOLEAN)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users_Table");
    }

    public boolean addUser(User newLogin){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EMAILS, newLogin.getUserEmail());
        cv.put(COLUMN_PASSWORD, newLogin.getUserPassword());
        cv.put(COLUMN_FIRSTNAME, newLogin.getUserFirstName());
        cv.put(COLUMN_LASTNAME, newLogin.getUserLastName());

        long insert = db.insert(USERS_TABLE,null,cv);
        return insert != -1;
    }

    public boolean check_valid_user(User loginAttempt){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+USERS_TABLE + " WHERE " + COLUMN_EMAILS + " = " + "'" +loginAttempt.getUserEmail()+ "'";
        Cursor probe = db.rawQuery(query,null);

        return probe.getCount() != 0;
    }
    public boolean check_password(User loginAttempt, String passwordAttempt){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ USERS_TABLE +
                       " WHERE " + COLUMN_EMAILS + " = " + "'" +loginAttempt.getUserEmail()+ "'" +
                       "AND "+ COLUMN_PASSWORD  + " = " + "'" +passwordAttempt+ "'" ;
        Cursor probe = db.rawQuery(query,null);
        return probe.getCount() != 0;
    }

    public boolean change_password(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE "+USERS_TABLE+" SET "+COLUMN_PASSWORD+" = '"+password+"' WHERE "+COLUMN_EMAILS+" = '"+email+"'");
        User verifyAttempt = new User(email,"","",password);
        boolean isValid = check_password(verifyAttempt, password);
        return isValid;
    }
}