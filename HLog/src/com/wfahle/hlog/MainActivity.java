package com.wfahle.hlog;

import java.util.ArrayList;

import com.wfahle.hlog.contentprovider.QSOContactProvider;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
// TODO: need to clean up the data structures - one callsign, not two
// TODO: get rid of config db altogether; store all data in thingy
// TODO: export ADIF file
// TODO: send ADIF file via e-mail
// TODO: go direct to edit screen when click on item in MainActivity, not EntryActivity
// TODO: get rid of "edit" button in EntryActivity - replace with cw skimmer on/off
// TODO: keep background telnet running when leaving EntryActivity; but
// TODO: accumulate about 20 or 30 items - then log off.
// TODO: when coming back, get those 20 or 30 items - wait, isn't that just a sh/dx?
// TODO: need sh/dx button?
// TODO: clean up UI to be consistent, etc.
// TODO: add context menu to EntryActivity - long-click lets you save spot
// TODO: forward/backward on spots - maybe in the area above
// TODO: larger type on text in spots list.
// TODO: load/save of text in spots list breaks some lines up instead of
// TODO: going all the way to the divider and keeping string intact

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
//	private static final int ACTIVITY_CREATE = 0;
//  	private static final int ACTIVITY_EDIT = 1;
	protected final static int config_request = 1; 
	protected final static int log_request = 2;
  	private static final int DELETE_ID = Menu.FIRST + 1;
  	// private Cursor cursor;
  	private SimpleCursorAdapter adapter;
    private void fillData() {

      // Fields from the database (projection)
      // Must include the _id column for the adapter to work
      String[] from = new String[] { QSOContactTable.KEY_CALL, QSOContactTable.KEY_RXFREQ, QSOContactTable.KEY_TIMEON };
      // Fields on the UI to which we map
      int[] to = new int[] { R.id.key_call, R.id.key_rxfreq, R.id.key_timeon };

      getLoaderManager().initLoader(0, null, this);
      adapter = new SimpleCursorAdapter(this, R.layout.log_row, null, from,
          to, 0);

      setListAdapter(adapter);
    }
    
    private ArrayList<String> getAdifData() {
    	ArrayList<String> ret = new ArrayList<String>();
        // Fields from the database (projection)
		String[] projection = {    QSOContactTable.KEY_ID, QSOContactTable.KEY_CALL, QSOContactTable.KEY_RXFREQ, QSOContactTable.KEY_TXFREQ,
				QSOContactTable.KEY_TIMEON, QSOContactTable.KEY_TIMEOFF, QSOContactTable.KEY_MODE, QSOContactTable.KEY_RRST,
				QSOContactTable.KEY_SRST, QSOContactTable.KEY_NAME, QSOContactTable.KEY_QTH, QSOContactTable.KEY_STATE, 
				QSOContactTable.KEY_COUNTRY, QSOContactTable.KEY_GRID };
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
			ret.add("<ProgramID:6>HLog00<eoh>\r\n");
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
		    	String time;
		    	String time2;
		    	if (dateTime.length == 2 && dateTime2.length == 2)
		    	{
		    		date = TextUtils.replace(dateTime[0], dashColon, rdashColon).toString();
		    		date = TextUtils.replace(date, dashColon, rdashColon).toString();
		    		time = TextUtils.replace(dateTime[1], dashColon, rdashColon).toString();
		    		time = TextUtils.replace(time, dashColon, rdashColon).toString();
		    		time2 = TextUtils.replace(dateTime2[1], dashColon, rdashColon).toString();
		    		time2 = TextUtils.replace(time2, dashColon, rdashColon).toString();
		    	}
		    	else
		    	{
		    		date = timeOn;
		    		time = "";
		    		time2 = timeOff;
		    	}
		    	String rxFreq = mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_RXFREQ));
		    	String call = mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_CALL));
		    	String rrst =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_RRST));
		    	String srst =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_SRST));
		    	String grid =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_GRID));
		    	String name =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_NAME));
		    	String qth =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_QTH));
		    	String state =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_STATE));
		    	String country =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_COUNTRY));
		    	String mode =  mCursor.getString(mCursor.getColumnIndex(QSOContactTable.KEY_MODE));
		    	
		    	String row = 
		        // Gets the value from the column.
		        "<QSO_DATE:8>" + date + 
		    	"<TIME_ON:6>" + time +
		    	"<TIME_OFF:6>" + time2 +
		    	"<FREQ:" +rxFreq.length() + ">" + rxFreq + 
		    	"<CALL:" +call.length() + ">" + call + 
		    	"<RST_RCVD:" +rrst.length() + ">" + rrst +
		    	"<RST_SENT:" +srst.length() + ">" + srst +
		    	"<GRIDSQUARE:"+grid.length() + ">" + grid +
		    	"<NAME:"+name.length() + ">" + name +
		    	"<QTH:"+qth.length() + ">" + qth +
		    	"<STATE:"+state.length() + ">" + state +
		    	"<COUNTRY:"+country.length() + ">" + country +
		    	"<MODE:"+mode.length() + ">" + mode +
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
		case (log_request) : {
	          if (resultCode == Activity.RESULT_OK) {
	          }			  
		}
        case (config_request) : {
          if (resultCode == Activity.RESULT_OK) {
//        		telnetServer = data.getStringExtra(ConfigActivity.SERVER_NAME);
//        		telnetPort = data.getIntExtra(ConfigActivity.PORT_NUMBER, 23);
//        		telnetLogon = data.getStringExtra(ConfigActivity.LOGON_CALL);
          }
          break;
        } 
      }
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.getListView().setDividerHeight(2);
		
		setContentView(R.layout.activity_main);
	    fillData();
	    registerForContextMenu(getListView());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.entry, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.capture_button){
			onCapture(null);
		}
		return super.onOptionsItemSelected(item);
	}
	
	// Opens the second activity if an entry is clicked
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    Intent i = new Intent(this, EntryActivity.class);
	    Uri todoUri = Uri.parse(QSOContactProvider.CONTENT_URI + "/" + id);
	    i.putExtra(QSOContactProvider.CONTENT_ITEM_TYPE, todoUri);
	
	    startActivity(i);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	      ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}
	
	public void onTools(View view) {
		// for now, just export as email, no other tools exist
		  getAdifData();
	}
	
    public void onCapture(View view) {
        Intent i = new Intent(this, EntryActivity.class);
        startActivityForResult(i, log_request);
    }
    
    public void onConfig(View view) {
    	Intent intent = new Intent(this, ConfigActivity.class);
    	startActivityForResult(intent, config_request);
    }

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		   String[] projection = { QSOContactTable.KEY_ID, QSOContactTable.KEY_CALL, QSOContactTable.KEY_RXFREQ, QSOContactTable.KEY_TIMEON};
		    CursorLoader cursorLoader = new CursorLoader(this,
		        QSOContactProvider.CONTENT_URI, projection, null, null, null);
		    return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
	    adapter.swapCursor(data);		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		   // data is not available anymore, delete reference
	    adapter.swapCursor(null);
	}
    @Override
	public boolean onContextItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case DELETE_ID:
	      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
	          .getMenuInfo();
	      Uri uri = Uri.parse(QSOContactProvider.CONTENT_URI + "/"
	          + info.id);
	      getContentResolver().delete(uri, null, null);
	      fillData();
	      return true;
	    }
	    return super.onContextItemSelected(item);
	}

}

