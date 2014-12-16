package com.wfahle.hlog.contentprovider;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class QSOContactTable {
    // Contacts table name
    public static final String TABLE_CONTACTS = "qsos";
 
    // Contacts Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_CALL = "call";
    public static final String KEY_RXFREQ = "rxfreq";
    public static final String KEY_TXFREQ = "txfreq";
    public static final String KEY_TIMEON = "timeon";
    public static final String KEY_TIMEOFF = "timeoff";
    public static final String KEY_MODE = "mode";
    public static final String KEY_RRST = "rrst";
    public static final String KEY_SRST = "srst";
    public static final String KEY_NAME = "name";
    public static final String KEY_QTH = "qth";
    public static final String KEY_STATE = "state";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_GRID = "grid";
    public static final String KEY_TXPWR = "power";
    public static final String KEY_COMPLETE = "complete";
    private static final String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CALL + " TEXT,"
            + KEY_RXFREQ + " TEXT," 
            + KEY_TXFREQ + " TEXT," 
            + KEY_TIMEON + " TEXT," 
            + KEY_TIMEOFF + " TEXT," 
            + KEY_MODE + " TEXT," 
            + KEY_RRST + " TEXT," 
            + KEY_SRST + " TEXT," 
            + KEY_NAME + " TEXT," 
            + KEY_QTH + " TEXT," 
            + KEY_STATE + " TEXT," 
            + KEY_COUNTRY + " TEXT," 
            + KEY_GRID + " TEXT, "
            + KEY_TXPWR + " TEXT, "
            + KEY_COMPLETE + " TEXT"
            + ");";
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_CONTACTS_TABLE);
    }
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(QSOContactTable.class.getName(), "Upgrading database from version "
	            + oldVersion + " to " + newVersion
	            + ", which will destroy all old data");
	    
	     db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
	        // Create tables again
	     onCreate(db);
	}
}
