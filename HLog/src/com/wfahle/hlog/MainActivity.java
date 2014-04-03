package com.wfahle.hlog;

import com.wfahle.hlog.contentprovider.QSOContactProvider;

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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
//	private static final int ACTIVITY_CREATE = 0;
//  	private static final int ACTIVITY_EDIT = 1;
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
	
    public void onCapture(View view) {
        Intent i = new Intent(this, EntryActivity.class);
        startActivity(i);    	
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

}

