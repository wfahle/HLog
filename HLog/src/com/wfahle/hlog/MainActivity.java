package com.wfahle.hlog;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	private static final int ACTIVITY_CREATE = 0;
  	private static final int ACTIVITY_EDIT = 1;
  	private static final int DELETE_ID = Menu.FIRST + 1;
  	// private Cursor cursor;
  	private SimpleCursorAdapter adapter;
    private void fillData() {

      // Fields from the database (projection)
      // Must include the _id column for the adapter to work
      String[] from = new String[] { QSOContactTable.KEY_CALL, QSOContactTable.KEY_RXFREQ, QSOContactTable.KEY_TIMEOFF };
      // Fields on the UI to which we map
      int[] to = new int[] { R.id.key_call, R.id.key_rxfreq, R.id.key_timeoff };

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
		return super.onOptionsItemSelected(item);
	}
    public void onCapture(View view) {
    	
    }

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}

}

