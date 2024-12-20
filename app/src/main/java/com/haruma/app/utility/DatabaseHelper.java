package com.haruma.app.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.haruma.app.model.Timetable;
import com.haruma.app.model.User;
import com.haruma.app.model.UserDetail;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "school_timetable.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USER = "User";
    private static final String TABLE_USER_DETAIL = "UserDetail";
    private static final String TABLE_TIMETABLE = "Timetable";

    private static final String USER_ID = "userId";
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";

    private static final String USERDETAIL_FULLNAME = "fullName";
    private static final String USERDETAIL_CLASS = "class";
    private static final String USERDETAIL_PHONE = "phone";

    private static final String TIMETABLE_ID = "timeTableId";
    private static final String TIMETABLE_NAME = "name";
    private static final String TIMETABLE_DAY = "day";
    private static final String TIMETABLE_NOTE = "note";
    private static final String TIMETABLE_STARTTIME = "startTime";
    private static final String TIMETABLE_ENDTIME = "endTime";
    private static final String STATUS = "status";

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

    private static final String CREATE_TABLE_TIMETABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TIMETABLE + "("
            + TIMETABLE_ID + " INTEGER PRIMARY KEY,"
            + TIMETABLE_NAME + " VARCHAR(50),"
            + TIMETABLE_DAY + " TIME,"
            + TIMETABLE_NOTE + " VARCHAR(255),"
            + TIMETABLE_STARTTIME + " TIME,"
            + TIMETABLE_ENDTIME + " TIME,"
            + STATUS + " INTEGER,"
            + USER_ID + " INTEGER,"
            + "FOREIGN KEY(" + USER_ID + ") REFERENCES " + TABLE_USER + "(" + USER_ID + ")" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_USER_DETAIL);
        db.execSQL(CREATE_TABLE_TIMETABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMETABLE);
        onCreate(db);
    }

    public void addTimeTable(String name, String day, String note, String startTime, String endTime, boolean status, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("day", day);
        values.put("note", note);
        values.put("startTime", startTime);
        values.put("endTime", endTime);
        values.put("userId", userId);
        values.put("status", status ? 1 : 0);
        db.insert(TABLE_TIMETABLE, null, values);
        db.close();
    }

    public void updateTimeTable(int timeTableId, String name, String day, String note, String startTime, String endTime, boolean status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("day", day);
        values.put("note", note);
        values.put("startTime", startTime);
        values.put("endTime", endTime);
        values.put("status", status ? 1 : 0);
        db.update(TABLE_TIMETABLE, values, "timeTableId = ?", new String[]{String.valueOf(timeTableId)});
        db.close();
    }

    public void deleteTimetable(int timeTableId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TIMETABLE, "timeTableId = ?", new String[]{String.valueOf(timeTableId)});
        db.close();
    }

    public List<Timetable> getAllTimeTable() {
        List<Timetable> timetableList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIMETABLE, null);
        if (cursor.moveToFirst()) {
            do {
                Timetable timetable = new Timetable(
                        cursor.getInt(cursor.getColumnIndexOrThrow("timeTableId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("day")),
                        cursor.getString(cursor.getColumnIndexOrThrow("note")),
                        cursor.getString(cursor.getColumnIndexOrThrow("startTime")),
                        cursor.getString(cursor.getColumnIndexOrThrow("endTime")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                );
                timetableList.add(timetable);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return timetableList;
    }

    public Timetable findTimeTableById(int timeTableId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Timetable timetable = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIMETABLE + " WHERE timeTableId = ?",
                new String[]{String.valueOf(timeTableId)});
        if (cursor.moveToFirst()) {
            timetable = new Timetable(
                    cursor.getInt(cursor.getColumnIndexOrThrow("timeTableId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("day")),
                    cursor.getString(cursor.getColumnIndexOrThrow("note")),
                    cursor.getString(cursor.getColumnIndexOrThrow("startTime")),
                    cursor.getString(cursor.getColumnIndexOrThrow("endTime")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("status"))
            );
        }
        cursor.close();
        db.close();
        return timetable;
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
        userDetailValues.put(USER_ID, userId);
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

    public void clearTimeTable(){
        List<Timetable> data = this.getAllTimeTable();
        for (Timetable d : data) {
            this.deleteTimetable(d.getTimeTableId());
        }
    }

}

