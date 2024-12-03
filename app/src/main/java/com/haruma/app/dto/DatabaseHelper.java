package com.haruma.app.dto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.haruma.app.model.Diary;
import com.haruma.app.model.User;
import com.haruma.app.model.UserDetail;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "school_diary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "User";
    private static final String TABLE_USER_DETAIL = "UserDetail";
    private static final String TABLE_DIARY = "Diary";

    private static final String USER_ID = "userId";
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";

    private static final String USERDETAIL_FULLNAME = "fullName";
    private static final String USERDETAIL_CLASS = "class";
    private static final String USERDETAIL_PHONE = "phone";

    private static final String DIARY_ID = "diaryId";
    private static final String DIARY_NAME = "name";
    private static final String DIARY_DAY = "day";
    private static final String DIARY_NOTE = "note";
    private static final String DIARY_STARTTIME = "startTime";
    private static final String DIARY_ENDTIME = "endTime";

    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
            + USER_ID + " INTEGER PRIMARY KEY,"
            + USER_EMAIL + " VARCHAR(255),"
            + USER_PASSWORD + " VARCHAR(255)" + ")";

    private static final String CREATE_TABLE_USER_DETAIL = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_DETAIL + "("
            + USER_ID + " INTEGER PRIMARY KEY,"
            + USERDETAIL_FULLNAME + " VARCHAR(255),"
            + USERDETAIL_CLASS + " VARCHAR(50),"
            + USERDETAIL_PHONE + " VARCHAR(20),"
            + "FOREIGN KEY(" + USER_ID + ") REFERENCES " + TABLE_USER + "(" + USER_ID + ") ON DELETE CASCADE" + ")";

    private static final String CREATE_TABLE_DIARY = "CREATE TABLE IF NOT EXISTS " + TABLE_DIARY + "("
            + DIARY_ID + " INTEGER PRIMARY KEY,"
            + DIARY_NAME + " VARCHAR(50),"
            + DIARY_DAY + " TIME,"
            + DIARY_NOTE + " VARCHAR(255),"
            + DIARY_STARTTIME + " TIME,"
            + DIARY_ENDTIME + " TIME,"
            + USER_ID + " INTEGER,"
            + "FOREIGN KEY(" + USER_ID + ") REFERENCES " + TABLE_USER + "(" + USER_ID + ")" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_USER_DETAIL);
        db.execSQL(CREATE_TABLE_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIARY);
        onCreate(db);
    }

    public void addUser(String email, String password, String fullName, String className, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(USER_EMAIL, email);
            values.put(USER_PASSWORD, password);
            long userId = db.insert(TABLE_USER, null, values);
            ContentValues newValues = new ContentValues();
            newValues.put(USER_ID, userId);
            newValues.put(USERDETAIL_FULLNAME, fullName);
            newValues.put(USERDETAIL_CLASS, className);
            newValues.put(USERDETAIL_PHONE, phoneNumber);
            db.insert(TABLE_USER_DETAIL, null, newValues);
        }
        catch (Exception e) {
            throw e;
        } finally {
            db.close();
        }
    }

    public void updateUser(int userId, String email, String password, String fullName, String className, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(USER_EMAIL, email);
            values.put(USER_PASSWORD, password);
            db.update(TABLE_USER, values, "userId=?", new String[]{String.valueOf(userId)});
            ContentValues newValues = new ContentValues();
            newValues.put(USERDETAIL_FULLNAME, fullName);
            newValues.put(USERDETAIL_CLASS, className);
            newValues.put(USERDETAIL_PHONE, phoneNumber);
            db.update(TABLE_USER_DETAIL, newValues, "userId=?", new String[]{String.valueOf(userId)});
        }
        catch (Exception e) {
            throw e;
        } finally {
            db.close();
        }
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, "userId = ?", new String[]{String.valueOf(userId)});
        db.delete(TABLE_USER_DETAIL, "userId = ?", new String[]{String.valueOf(userId)});
        db.close();
    }

    public void addDiary(String name, String day, String note, String startTime, String endTime, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("day", day);
        values.put("note", note);
        values.put("startTime", startTime);
        values.put("endTime", endTime);
        values.put("userId", userId);  // Link to User

        db.insert(TABLE_DIARY, null, values);
        db.close();
    }

    public void updateDiary(int diaryId, String name, String day, String note, String startTime, String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("day", day);
        values.put("note", note);
        values.put("startTime", startTime);
        values.put("endTime", endTime);
        db.update(TABLE_DIARY, values, "diaryId = ?", new String[]{String.valueOf(diaryId)});
        db.close();
    }

    public void deleteDiary(int diaryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DIARY, "diaryId = ?", new String[]{String.valueOf(diaryId)});
        db.close();
    }

    public List<Diary> getAllDiaries() {
        List<Diary> diaryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DIARY, null);
        if (cursor.moveToFirst()) {
            do {
                Diary diary = new Diary(
                        cursor.getInt(cursor.getColumnIndexOrThrow("diaryId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("day")),
                        cursor.getString(cursor.getColumnIndexOrThrow("note")),
                        cursor.getString(cursor.getColumnIndexOrThrow("startTime")),
                        cursor.getString(cursor.getColumnIndexOrThrow("endTime")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("userId"))
                );
                diaryList.add(diary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return diaryList;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        String query = "SELECT u." + USER_ID + ", u." + USER_EMAIL + ", u." + USER_PASSWORD + ", "
                + "ud." + USERDETAIL_FULLNAME + ", ud." + USERDETAIL_CLASS + ", ud." + USERDETAIL_PHONE
                + " FROM " + TABLE_USER + " u "
                + " JOIN " + TABLE_USER_DETAIL + " ud ON u." + USER_ID + " = ud." + USER_ID
                + " WHERE u." + USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(USER_PASSWORD))
            );
            UserDetail userDetail = new UserDetail(
                    cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(USERDETAIL_FULLNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(USERDETAIL_CLASS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(USERDETAIL_PHONE))
            );
            user.setUserDetail(userDetail);
        }
        cursor.close();
        db.close();
        return user;
    }

    public boolean registerUser(String email, String password, String fullName, String className, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues userValues = new ContentValues();
        userValues.put(USER_EMAIL, email);
        userValues.put(USER_PASSWORD, password);
        long userId = db.insert(TABLE_USER, null, userValues);
        if (userId == -1) {
            db.close();
            return false;
        }
        ContentValues userDetailValues = new ContentValues();
        userDetailValues.put(USER_ID, userId); // Foreign key
        userDetailValues.put(USERDETAIL_FULLNAME, fullName);
        userDetailValues.put(USERDETAIL_CLASS, className);
        userDetailValues.put(USERDETAIL_PHONE, phoneNumber);
        long result = db.insert(TABLE_USER_DETAIL, null, userDetailValues);
        db.close();
        return result != -1;
    }

    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u." + USER_ID + ", u." + USER_EMAIL + ", u." + USER_PASSWORD + ", "
                + "ud." + USERDETAIL_FULLNAME + ", ud." + USERDETAIL_CLASS + ", ud." + USERDETAIL_PHONE
                + " FROM " + TABLE_USER + " u "
                + " JOIN " + TABLE_USER_DETAIL + " ud ON u." + USER_ID + " = ud." + USER_ID
                + " WHERE u." + USER_EMAIL + " = ? AND u." + USER_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(USER_PASSWORD))
            );
            UserDetail userDetail = new UserDetail(
                    cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(USERDETAIL_FULLNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(USERDETAIL_CLASS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(USERDETAIL_PHONE))
            );
            user.setUserDetail(userDetail);
        }
        cursor.close();
        db.close();
        return user;
    }



}

