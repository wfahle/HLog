package com.wfahle.unused;
import com.wfahle.hlog.contentprovider.QSOContactTable;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ConfigTable {
    // Configuration table name
    public static final String TABLE_CONFIG = "config";
 
    // Contacts Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_CALL = "call";
    public static final String KEY_SERVER = "server";
    public static final String KEY_PORT = "port";
    public static final String KEY_RSERVER = "rserver";
    public static final String KEY_RPORT = "rport";
    public static final String KEY_PREFERRED = "preferred";
    public static final String SELECT_ALL = "SELECT  * FROM " + TABLE_CONFIG;
    private static final String CREATE_CONFIG_TABLE = "CREATE TABLE " + TABLE_CONFIG + "("
            + KEY_ID + " INTEGER PRIMARY KEY," 
            + KEY_CALL+ " TEXT,"
    		+ KEY_SERVER + " TEXT,"
            + KEY_PORT + " INTEGER,"
            + KEY_RSERVER + " TEXT,"
            + KEY_RPORT + " INTEGER,"
            + KEY_PREFERRED + " INTEGER" 
            + ");";
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_CONFIG_TABLE);
    }
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(QSOContactTable.class.getName(), "Upgrading database from version "
	            + oldVersion + " to " + newVersion
	            + ", which will destroy all old data");
	    
	     db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG);
	        // Create tables again
	     onCreate(db);
	}
}
