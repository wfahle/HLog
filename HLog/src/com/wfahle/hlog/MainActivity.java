package com.wfahle.hlog;

import com.wfahle.hlog.contentprovider.QSOContactProvider;
import com.wfahle.hlog.contentprovider.QSOContactTable;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

// TODO: Filters and CW skimmer
// TODO: put cwskimmer setting in config - check skimmed items against frequency
// TODO: need buttons for tune, spot, etc.
// TODO: clean up UI to be consistent, etc.
// TODO: add context menu to EntryActivity - long-click lets you save spot
// TODO: forward/backward on spots - maybe in the area above
// TODO: larger type on text in spots list.
// TODO: save multiple configs in a db to let user choose saved operating filters - ListView
// TODO: consolidate entries in spots list - same call/freq should update? 
// TODO: track needed entities/bands and highlight appropriately/filter out
// TODO: latter task would involve a pseudo-import of ADIF - just gather band/mode/entity info and ignore the rest 

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	protected final static int config_request = 1; 
	protected final static int log_request = 2;
	protected final static int tools_request = 3;
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
      Cursor curse = adapter.getCursor();
      if (curse != null)
      {
	      int count = adapter.getCursor().getCount();
	      String conts = "Contacts(" + count + ")";
	      TextView tv = (TextView) findViewById(R.id.contacts);
	      tv.setText(conts);
      }
    }    
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	switch(requestCode) {
	      	case (tools_request) : {
	            if (resultCode == Activity.RESULT_OK) {
	            }
	            break;
	      	}
			case (log_request) : {
	          if (resultCode == Activity.RESULT_OK) { // see if we need to delete it - no timeon
	        	Uri uri = (Uri)data.getParcelableExtra(QSOContactProvider.CONTENT_ITEM_TYPE);
	      		if (uri != null) {
	    			String[] projection = {    QSOContactTable.KEY_ID, 
	    					QSOContactTable.KEY_TIMEON, QSOContactTable.KEY_TIMEOFF,
	    					QSOContactTable.KEY_COMPLETE
	    					 };
	    		    Cursor cursor = getContentResolver().query(uri, projection, null, null,
	    		        null);
	    		    if (cursor != null) {
	    		    	cursor.moveToFirst();
	    		    	String complete = cursor.getString(cursor
		    			          .getColumnIndexOrThrow(QSOContactTable.KEY_COMPLETE));
	    		      	String timeon = cursor.getString(cursor
	    			          .getColumnIndexOrThrow(QSOContactTable.KEY_TIMEON));
	    		      	String timeoff = cursor.getString(cursor
		    			          .getColumnIndexOrThrow(QSOContactTable.KEY_TIMEOFF));
	    		      	if ((timeon.length() == 0 && timeoff.length() == 0) || !complete.equals("Y")) {
	    		      		getContentResolver().delete(uri, null, null);
	    		      		fillData();
	    		      	}
	    		    }
	            }			  
		    }
			}
	        case (config_request) : {
	          if (resultCode == Activity.RESULT_OK) {
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
	    return super.onCreateOptionsMenu(menu);
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
	    Intent i = new Intent(this, LogActivity.class);
	    Uri qsoUri = Uri.parse(QSOContactProvider.CONTENT_URI + "/" + id);
	    i.putExtra(QSOContactProvider.CONTENT_ITEM_TYPE, qsoUri);
	
	    startActivity(i); // no result expected - just edit the entry.
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	      ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}
	
	
	public void onTools(View view) {
    	Intent intent = new Intent(this, ToolsActivity.class);
    	startActivityForResult(intent, tools_request);
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
	    if (data != null) {
		    int count = data.getCount();
		    String conts = "Contacts(" + count + ")";
		    TextView tv = (TextView) findViewById(R.id.contacts);
		    tv.setText(conts);
	    }
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

