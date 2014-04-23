package com.wfahle.hlog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	  // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
 
    // Database Name
    private static final String DATABASE_NAME = "qsoManager";
 
 
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	       QSOContactTable.onCreate(db);	 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		QSOContactTable.onUpgrade(db, oldVersion, newVersion);
	}
	
	/*
	// Adding new contact
	public void addContact(QSOContact contact) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
	    values.put(KEY_CALL, contact.getCall());
	    values.put(KEY_RXFREQ, contact.getrxFreq());
	    values.put(KEY_TXFREQ, contact.gettxFreq());
	    values.put(KEY_TIMEON, contact.getTimeon());
	    values.put(KEY_TIMEOFF, contact.getTimeoff());
	    values.put(KEY_MODE, contact.getMode());
	    values.put(KEY_RRST, contact.getRRST());
	    values.put(KEY_SRST, contact.getSRST());
	    values.put(KEY_NAME, contact.getName());
	    values.put(KEY_QTH, contact.getQTH());
	    values.put(KEY_STATE, contact.getState());
	    values.put(KEY_COUNTRY, contact.getCountry());
	    values.put(KEY_GRID, contact.getGrid());
//		what about id?
	    
	    // Inserting Row
	    db.insert(TABLE_CONTACTS, null, values);
	    db.close(); // Closing database connection		
	}
	 
	// Getting single contact
	public QSOContact getContact(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    
	    Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
	    		KEY_CALL, KEY_RXFREQ, KEY_TXFREQ, KEY_TIMEON, KEY_TIMEOFF, KEY_MODE, KEY_RRST, KEY_SRST, 
	    		KEY_NAME, KEY_QTH, KEY_STATE, KEY_COUNTRY, KEY_GRID }, KEY_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    QSOContact contact = new QSOContact(Integer.parseInt(cursor.getString(0)),
	            cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5),
	            cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10),
	            cursor.getString(11), cursor.getString(12), cursor.getString(13));
	    // return contact
	    return contact;
	}
	 
	// Getting All Contacts
	public List<QSOContact> getAllContacts() {
	   List<QSOContact> contactList = new ArrayList<QSOContact>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            QSOContact contact = new QSOContact();
	            contact.setId(Integer.parseInt(cursor.getString(0)));
	            contact.setCall(cursor.getString(1));
	            contact.setrxFreq(cursor.getString(2));
	            contact.settxFreq(cursor.getString(3));
	            contact.setTimeon(cursor.getString(4));
	            contact.setTimeoff(cursor.getString(5));
	            contact.setMode(cursor.getString(6));
	            contact.setRRST(cursor.getString(7));
	            contact.setSRST(cursor.getString(8));
	            contact.setName(cursor.getString(9));
	            contact.setQTH(cursor.getString(10));
	            contact.setState(cursor.getString(11));
	            contact.setCountry(cursor.getString(12));
	            contact.setGrid(cursor.getString(13));
	            // Adding contact to list
	            contactList.add(contact);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return contactList;
	}
	 
	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
	}
	
	// Updating single contact
	public int updateContact(QSOContact contact) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
	    values.put(KEY_CALL, contact.getCall());
	    values.put(KEY_RXFREQ, contact.getrxFreq());
	    values.put(KEY_TXFREQ, contact.gettxFreq());
	    values.put(KEY_TIMEON, contact.getTimeon());
	    values.put(KEY_TIMEOFF, contact.getTimeoff());
	    values.put(KEY_MODE, contact.getMode());
	    values.put(KEY_RRST, contact.getRRST());
	    values.put(KEY_SRST, contact.getSRST());
	    values.put(KEY_NAME, contact.getName());
	    values.put(KEY_QTH, contact.getQTH());
	    values.put(KEY_STATE, contact.getState());
	    values.put(KEY_COUNTRY, contact.getCountry());
	    values.put(KEY_GRID, contact.getGrid());
	 
	    // updating row
	    return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(contact.getId()) });
	}
	 
	// Deleting single contact
	public void deleteContact(QSOContact contact) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
	            new String[] { String.valueOf(contact.getId()) });
	    db.close();	
	} */
}
