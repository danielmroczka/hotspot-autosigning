package com.labs.dm.wifi.autosigning;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daniel Mroczka on 2015-07-06.
 */
public class DBManager extends SQLiteOpenHelper {

    private final SQLiteDatabase writableDatabase;
    private final SQLiteDatabase readableDatabase;
    public final static String DB_NAME = "wifi.db";

    public DBManager(Context context, String name) {
        super(context, name, null, 1);
        writableDatabase = getWritableDatabase();
        readableDatabase = getReadableDatabase();
    }

    public DBManager(Context context) {
        this(context, DB_NAME);
    }


    @Override
    public synchronized void close() {
        writableDatabase.close();
        readableDatabase.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE
        db.execSQL("create table NET(id INTEGER PRIMARY KEY, ssid TEXT, bssid TEXT UNIQUE NOT NULL, url TEXT, path TEXT, added DATETIME DEFAULT CURRENT_TIMESTAMP )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addNetwork(String ssid, String bssid, String url, String path) {
        ContentValues content = new ContentValues();
        content.put("ssid", ssid);
        content.put("bssid", bssid);
        content.put("url", url);
        content.put("path", path);
        long rowId;

        try {
            rowId = writableDatabase.insertOrThrow("network", null, content);
        } finally {
        }

        return rowId;
    }

}
