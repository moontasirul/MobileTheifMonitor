package com.project.universityproject.theifmonitoring;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This is database helper class,
 * By this handler class we used to handle all dataBase operations
 */
public class DataBaseHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "user";

    private static final String TABLE_APP_INFO = "appInfo";


    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    private static final String COLUMN_ROW_ID = "row_id";
    private static final String COLUMN_VERIFY_CODE = "verify_code";
    private static final String COLUMN_PHONE_NUM = "phone_number";
    private static final String COLUMN_EMAIL_ID = "email";

    private static final String COLUMN_SIM_ID = "sim_id";
    private static final String COLUMN_IMEI = "imei";
    private static final String COLUMN_SIM_SERIAL_NUMBER = "sim_serial_number";


    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT" + ")";


    // create table sql query
    private String CREATE_APP_INFO_TABLE = "CREATE TABLE " + TABLE_APP_INFO + "("
            + COLUMN_USER_NAME + " TEXT PRIMARY KEY UNIQUE,"
            + COLUMN_VERIFY_CODE + " TEXT,"
            + COLUMN_PHONE_NUM + " TEXT,"
            + COLUMN_EMAIL_ID + " TEXT,"
            + COLUMN_SIM_ID + " TEXT,"
            + COLUMN_IMEI + " TEXT" + ")";


    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_APP_INFO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //Drop User Table if exist
        sqLiteDatabase.execSQL(DROP_USER_TABLE);

        // Create tables again
        onCreate(sqLiteDatabase);
    }


    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }


    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addAppInfo(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_VERIFY_CODE, user.getVerifyingCode());
        values.put(COLUMN_PHONE_NUM, user.getUserPhone());
        values.put(COLUMN_EMAIL_ID, user.getEmailId());
        values.put(COLUMN_SIM_ID, user.getUserSIMID());
        values.put(COLUMN_IMEI, user.getUserDeviceId());

        Log.i("cehckDBInsert",user.getUserName()+user.getVerifyingCode()+user.getUserPhone()+user.getEmailId()+user.getUserSIMID()+user.getUserDeviceId());

        // Inserting Row
        long data = db.insert(TABLE_APP_INFO, null, values);
        Log.i("InsertData ", "" + data);
        db.close();
    }

    // moon
    // 123456
    // 07171523980
    // moon@gmail.com
    // SIM ID is :470043009449877
    // IMEI is :356697071778628

    public User getData(String userName) {
        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_APP_INFO + " WHERE "
                + COLUMN_USER_NAME + " like " + "'"+userName+"';";

        //String selectQuery = "SELECT * FROM appInfo WHERE user_name Like 'moon1';";

        Cursor c = database.rawQuery(selectQuery, null);
        User user = new User();

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    user.setEmailId(c.getString(c.getColumnIndex(COLUMN_EMAIL_ID)));
                    user.setUserDeviceId(c.getString(c.getColumnIndex(COLUMN_IMEI)));
                    user.setUserSIMID(c.getString(c.getColumnIndex(COLUMN_SIM_ID)));
                    user.setUserPhone(c.getString(c.getColumnIndex(COLUMN_PHONE_NUM)));
                    user.setVerifyingCode(c.getString(c.getColumnIndex(COLUMN_VERIFY_CODE)));

                } while (c.moveToNext());
            }
        }


        return user;

    }


    /**
     * This method to check user exist or not
     *
     * @param userName
     * @return true/false
     */
    public boolean checkUser(String userName) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {userName};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_name = 'userName;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }


    /**
     * This method to check user exist or not
     *
     * @param userName
     * @param password
     * @return true/false
     */
    public boolean checkUser(String userName, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {userName, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_name = 'userName' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
}
