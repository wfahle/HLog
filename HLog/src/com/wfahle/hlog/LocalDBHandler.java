package com.wfahle.hlog;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDBHandler extends SQLiteOpenHelper {
	  // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "HLogConfig";

	public LocalDBHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    ConfigTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		ConfigTable.onUpgrade(db, oldVersion, newVersion);
	}

	// Getting All Configs
	public List<TelnetConfig> getAllConfigs() {
	   List<TelnetConfig> configList = new ArrayList<TelnetConfig>();
	    // Select All Query
	    String selectQuery = ConfigTable.SELECT_ALL;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	TelnetConfig config = new TelnetConfig(cursor.getString(0), 
	        			Integer.parseInt(cursor.getString(1)), 
	        			Integer.parseInt(cursor.getString(2))==0);
	            // Adding contact to list
	            configList.add(config);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return configList;
	}
	
	public void addConfig(TelnetConfig config) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
	    values.put(ConfigTable.KEY_SERVER, config.getServer());
	    values.put(ConfigTable.KEY_PORT, config.getPort());
	    values.put(ConfigTable.KEY_PREFERRED, config.getPreferred()?1:0);
//		what about id?
	    
	    // Inserting Row
	    db.insert(ConfigTable.TABLE_CONFIG, null, values);
	    db.close(); // Closing database connection		
	}

}
