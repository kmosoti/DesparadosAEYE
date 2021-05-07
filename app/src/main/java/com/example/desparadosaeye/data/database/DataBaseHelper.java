package com.example.desparadosaeye.data.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;

import com.example.desparadosaeye.data.database.User;


import androidx.annotation.Nullable;

import java.util.Arrays;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String USERS_TABLE = "Users_table";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_EMAILS = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_FIRSTNAME = "FIRSTNAME";
    public static final String COLUMN_LASTNAME = "LASTNAME";
    public static final String COLUMN_VALID = "VALID";
    private final SQLiteDatabase db = this.getReadableDatabase();

    public DataBaseHelper(@Nullable Context context) {
        super(context, "users.db", null, 1);
    }
    //Specifically for Account Management
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE "+USERS_TABLE+"("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAILS + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_FIRSTNAME + " TEXT,"
                + COLUMN_LASTNAME + " TEXT,"
                + COLUMN_VALID + ", BOOLEAN)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users_Table");
    }

    public boolean addUser(User newLogin){
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EMAILS, newLogin.getUserEmail());
        cv.put(COLUMN_PASSWORD, newLogin.getUserPassword());
        cv.put(COLUMN_FIRSTNAME, newLogin.getUserFirstName());
        cv.put(COLUMN_LASTNAME, newLogin.getUserLastName());

        long insert = db.insert(USERS_TABLE,null,cv);
        return insert != -1;
    }

    public boolean check_valid_user(User loginAttempt){
        String query = "SELECT * FROM " + USERS_TABLE
                + " WHERE " + COLUMN_EMAILS + " = '" + loginAttempt.getUserEmail() + "'"
                + " AND " + COLUMN_PASSWORD + " = '" + loginAttempt.getUserPassword() + "'";
        @SuppressLint("Recycle")
        Cursor probe = db.rawQuery(query,null);
        Log.i("UserValidated", String.valueOf(probe.getCount()));
        boolean valid = probe.getCount() != 0;
        probe.close();
        return valid;
    }

    public boolean check_password(User loginAttempt, String passwordAttempt){
        String query = "SELECT * FROM "+ USERS_TABLE
                       + " WHERE " + COLUMN_EMAILS + " = " + "'" +loginAttempt.getUserEmail()+ "'"
                       + " AND " + COLUMN_PASSWORD  + " = " + "'" +passwordAttempt+ "'" ;
        @SuppressLint("Recycle")
        Cursor probe = db.rawQuery(query,null);
        boolean valid = probe.getCount() == 1;
        probe.close();
        return valid;
    }

    public boolean changeUser(String oldEmail, User userChanges) {
        // userChanges is a USER with NULL or empty attributes
        // except where changes (password, email, etc) will occur
        String query = "SELECT * FROM " + USERS_TABLE +
                " WHERE " + COLUMN_EMAILS + " = '" + oldEmail + "'";

        @SuppressLint("Recycle")
        Cursor probe = db.rawQuery(query, null);
        if(probe.getCount() >= 1) {
            probe.moveToFirst();
            User newUser = new User(userChanges);
            if (newUser.getUserEmail().trim().isEmpty()) {
                newUser.setEmail(probe.getString(probe.getColumnIndex(COLUMN_EMAILS)));
            }
            if (newUser.getUserPassword().trim().isEmpty()) {
                newUser.setPassword(probe.getString(probe.getColumnIndex(COLUMN_PASSWORD)));
            }
            if (newUser.getUserFirstName().trim().isEmpty()) {
                newUser.setUserFirstName(probe.getString(probe.getColumnIndex(COLUMN_FIRSTNAME)));
            }
            if (newUser.getUserLastName().trim().isEmpty()) {
                newUser.setUserLastName(probe.getString(probe.getColumnIndex(COLUMN_LASTNAME)));
            }
            probe.close();
            Log.i("changeUser", "deleting");
            deleteUser(userChanges.getUserEmail());
            Log.i("changeUser", "deleted. now adding");
            addUser(newUser);
            Log.i("changeUser", "added");
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @param email: email of user to be deleted
     * @return returns true if delete was sucessful
     */
    public boolean deleteUser(String email) {
        // application implementations should first call
        // `check_password` before deleting a user
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "DELETE FROM " + USERS_TABLE +
                " WHERE " + COLUMN_EMAILS + " = '" + email + "'";
        @SuppressLint("Recycle")
        Cursor probe = db.rawQuery(query, null);
        Log.i("Records deleted", String.valueOf(probe.getCount()));
        return probe.getCount() == 1;
    }

    public boolean change_password(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE "+USERS_TABLE+" SET "+COLUMN_PASSWORD+" = '"+password+"' WHERE "+COLUMN_EMAILS+" = '"+email+"'");
        User verifyAttempt = new User(email,"","",password);
        boolean isValid = check_password(verifyAttempt, password);
        return isValid;
    }
}