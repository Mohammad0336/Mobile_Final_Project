package com.example.mobile_final_proj;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "UserDB";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (email TEXT PRIMARY KEY, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", user.getEmail());
        contentValues.put("password", user.getPassword());
        long result = db.insert("users", null, contentValues);
        return result != -1;
    }

    // Inside DatabaseHelper.java
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"email", "password"};
        String selection = "email = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query("users", projection, selection, selectionArgs, null, null, null);
        User user = null;

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String userEmail = cursor.getString(cursor.getColumnIndex("email"));
            @SuppressLint("Range") String userPassword = cursor.getString(cursor.getColumnIndex("password"));
            user = new User(userEmail, userPassword);
            Log.d("DatabaseHelper", "User retrieved successfully: " + userEmail);
        } else {
            Log.e("DatabaseHelper", "User not found in the database");
        }

        cursor.close();
        return user;
    }


    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"email"};
        String selection = "email = ? AND password = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query("users", projection, selection, selectionArgs, null, null, null);
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }
}
