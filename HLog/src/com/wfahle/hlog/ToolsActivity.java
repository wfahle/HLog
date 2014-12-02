package com.wfahle.hlog;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.wfahle.hlog.contentprovider.QSOContactProvider;
import com.wfahle.hlog.contentprovider.QSOContactTable;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ToolsActivity extends Activity {
  	public static final String LOG_TAG = "HLog";
  	public static final String TEMP_ADIF_DIR = "EmailADIF";
  	public static final String TEMP_ADIF_FILE = "HLog.adi";
	protected final static int email_request = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tools);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tools, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	//file i/o methods
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public File getTempAdifDir(Context context, String folderName) {
	    // Get the directory for the app's private documents directory. 
	    File file = new File(context.getExternalFilesDir(
	            Environment.DIRECTORY_DOWNLOADS), folderName);
	    if (!file.mkdirs()) {
	        Log.e(LOG_TAG, "Directory not created");
	    }
	    return file;
	}
	
    private ArrayList<String> getAdifData() {
    	ArrayList<String> ret = new ArrayList<String>();
        // Fields from the database (projection)
		String[] projection = {    QSOContactTable.KEY_ID, QSOContactTable.KEY_CALL, QSOContactTable.KEY_RXFREQ, QSOContactTable.KEY_TXFREQ,
				QSOContactTable.KEY_TIMEON, QSOContactTable.KEY_TIMEOFF, QSOContactTable.KEY_MODE, QSOContactTable.KEY_RRST,
				QSOContactTable.KEY_SRST, QSOContactTable.KEY_NAME, QSOContactTable.KEY_QTH, QSOContactTable.KEY_STATE, 
				QSOContactTable.KEY_COUNTRY, QSOContactTable.KEY_GRID, QSOContactTable.KEY_TXPWR, QSOContactTable.KEY_COMPLETE };
		String selectionClause = null; // string if selecting
		String[] selectionArgs = null; // args to clause
		String sortOrder = QSOContactTable.KEY_TIMEON; // in order of qso start
		// Does a query against the table and returns a Cursor object
		Cursor mCursor = getContentResolver().query(
				QSOContactProvider.CONTENT_URI,  // The content URI of the qso table
		    projection,                       // The columns to return for each row
		    selectionClause,                   // Either null, or the word the user entered
		    selectionArgs,                    // Either empty, or the string the user entered
		    sortOrder);                       // The sort order for the returned rows
		// Some providers return null if an error occurs, others throw an exception
		if (null == mCursor) {
		    /*
		     * Insert code here to handle the error. Be sure not to use the cursor! You may want to
		     * call android.util.Log.e() to log this error.
		     *
		     */
		// If the Cursor is empty, the provider found no matches
		} else if (mCursor.getCount() < 1) {

		    /*
		     * Insert code here to notify the user that the search was unsuccessful. This isn't necessarily
		     * an error. You may want to offer the user the option to insert a new row, or re-type the
		     * search term.
		     */

		} else {
			ret.add("<ProgramID:4>HLog<eoh>\r\n");
		    // Insert code here to do something with the results
			//<QSO_DATE:8>20140415<TIME_ON:6>234909<TIME_OFF:6>234942<FREQ:6>14.218<CALL:6>W1AW/1<RST_RCVD:3>599
			//<RST_SENT:3>599<GRIDSQUARE:0><NAME:0><QTH:0><STATE:2>MA<COUNTRY:0><MODE:3>USB<TX_PWR:0><QSL_VIA:0><QSL_SENT:1>N<QSL_RCVD:1>N<STATION_CALLSIGN:0><NOTES:0><eor>

		    while (mCursor.moveToNext()) {
		    	String timeOn = mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_TIMEON));
		    	String timeOff = mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_TIMEOFF));
		    	String[] dateTime = TextUtils.split(timeOn, " ");
		    	String[] dateTime2 = TextUtils.split(timeOff, " ");
		    	String[] dashColon = {"-", ":", "/"};
		    	String[] rdashColon = {"","",""};
		    	String date;
		    	String date2;
		    	String time;
		    	String time2;
		    	if (dateTime.length == 2 && dateTime2.length == 2)
		    	{
		    		date = TextUtils.replace(dateTime[0], dashColon, rdashColon).toString();
		    		date = TextUtils.replace(date, dashColon, rdashColon).toString();
		    		time = TextUtils.replace(dateTime[1], dashColon, rdashColon).toString();
		    		time = TextUtils.replace(time, dashColon, rdashColon).toString();
		    		date2 = TextUtils.replace(dateTime2[0], dashColon, rdashColon).toString();
		    		date2 = TextUtils.replace(date2, dashColon, rdashColon).toString();
		    		time2 = TextUtils.replace(dateTime2[1], dashColon, rdashColon).toString();
		    		time2 = TextUtils.replace(time2, dashColon, rdashColon).toString();
		    	}
		    	else
		    	{
		    		date = timeOn;
		    		date2= timeOn; // won't get written if same
		    		time = "";
		    		time2 = timeOff;
		    	}
		    	String rxFreq = mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_RXFREQ));
		    	String txFreq = mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_TXFREQ));
		    	String call = mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_CALL));
		    	String rrst =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_RRST));
		    	String srst =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_SRST));
		    	String grid =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_GRID));
		    	String name =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_NAME));
		    	String qth =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_QTH));
		    	String state =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_STATE));
		    	String country =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_COUNTRY));
		    	String mode =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_MODE));
		    	String txpwr = mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_TXPWR));
		    	String complete = mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_COMPLETE));
		    	String date2str = "";
		    	if (!date2.equals(date))
		    		date2str = "<QSO_DATE_OFF:"+date2.length()+">" + date2;

		    	if (txpwr== null || txpwr.length() == 0)
		    		txpwr = "100";
		    	String row = 
		        // Gets the value from the column.
		        "<QSO_DATE:" + date.length() + ">" + date + 
		    	"<TIME_ON:" + time.length() + ">" + time +
		    	date2str+
		    	"<TIME_OFF:" + time2.length() + ">" + time2 +
		    	"<FREQ:" +rxFreq.length() + ">" + rxFreq + 
		    	"<FREQ_RX:" + txFreq.length() + ">" + txFreq +
		    	"<CALL:" +call.length() + ">" + call + 
		    	"<RST_RCVD:" +rrst.length() + ">" + rrst +
		    	"<RST_SENT:" +srst.length() + ">" + srst +
		    	"<GRIDSQUARE:"+grid.length() + ">" + grid +
		    	"<NAME:"+name.length() + ">" + name +
		    	"<QTH:"+qth.length() + ">" + qth +
		    	"<STATE:"+state.length() + ">" + state +
		    	"<COUNTRY:"+country.length() + ">" + country +
		    	"<MODE:"+mode.length() + ">" + mode +
		    	"<TX_PWR:"+txpwr.length() + ">" + txpwr + 
		    	"<QSO_COMPLETE:"+complete.length() + ">" + complete + 
		        "<eor>\r\n";

		        // Insert code here to process the retrieved word.
		    	ret.add(row);
		    	Log.d("ADIF", row);
		        // end of while loop
		    }
		}
		return ret;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      switch(requestCode) {
    
		case (email_request) : { 
		    if (resultCode == Activity.RESULT_OK) {
		    }			  
		}
      }
    }
	public void onMail(View view) {
		// for now, just export as email, no other tools exist
		ArrayList<String> adif = getAdifData();
		if (isExternalStorageWritable()) {
			Context localContext = getBaseContext();
			 File dir = getTempAdifDir(localContext, TEMP_ADIF_DIR);

			 File fil = new File(dir, TEMP_ADIF_FILE);
			 
			 FileOutputStream outputStream;

			 try {
				 String recipient = "", 
					  subject = "HLog ADIF file", 
					  message = "";

				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("message/rfc822");

				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{recipient});
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
				 
			    outputStream = new FileOutputStream(fil);
			    for (int i=0; i<adif.size(); i++) {
				   outputStream.write(adif.get(i).getBytes());				   
			    }
			    outputStream.close();
			    if (!fil.exists() || !fil.canRead()) {
				   Toast.makeText(this, "Problem creating adif", 
						      Toast.LENGTH_SHORT).show();
						  return;
			    }
			    Uri uri = Uri.parse("file://" + fil.getAbsolutePath());
			    emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
			    startActivityForResult(Intent.createChooser(emailIntent, 
			            "Email adif using..."), 
			            email_request);
			 } catch (Exception e) {
			   e.printStackTrace();
			 }
		}
		
	}
	
	public void onImport(View view) {
		
	}

}
