package com.wfahle.hlog;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class ConfigActivity extends Activity {

	public static final String SERVER_NAME = "com.wfahle.hlog.SERVER_NAME";
	public static final String PORT_NUMBER = "com.wfahle.hlog.PORT_NUMBER";
	public static final String LOGON_CALL = "com.wfahle.hlog.LOGON_CALL";
	private int _id = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		Context con = getBaseContext();
		LocalDBHandler ldb = new LocalDBHandler(con);
		List<TelnetConfig> configs = ldb.getAllConfigs();
    	EditText server_edit = (EditText) findViewById(R.id.cluster_edit);
    	String server = configs.get(0).getServer();
    	server_edit.setText(server);
    	EditText logon_edit = (EditText) findViewById(R.id.yourcall_edit);
    	String call = configs.get(0).getCall();
    	logon_edit.setText(call);
    	_id = configs.get(0).getId();
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
	protected void onPause()
	{
    	EditText logon_edit = (EditText) findViewById(R.id.yourcall_edit);
		String call = logon_edit.getEditableText().toString();
    	EditText server_edit = (EditText) findViewById(R.id.cluster_edit);
		String server = server_edit.getEditableText().toString();
		int port = 23;
		boolean preferred = true;
		
		TelnetConfig cfg = new TelnetConfig(_id, call, server, port, preferred);
		LocalDBHandler ldb = new LocalDBHandler(getBaseContext());
		if (_id == 0)
			ldb.addConfig(cfg);
		else
			ldb.updateConfig(cfg);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config, menu);
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
			done(null);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    public void done(View view) {    	
    	Intent resultIntent = new Intent();
    	EditText server_edit = (EditText) findViewById(R.id.cluster_edit);
    	String server = server_edit.getEditableText().toString();
    	EditText logon_edit = (EditText) findViewById(R.id.yourcall_edit);
    	String logon = logon_edit.getEditableText().toString()+"\r\n";
    	int port=23; // TODO: add ability to set port
    	resultIntent.putExtra(SERVER_NAME, server);
    	resultIntent.putExtra(PORT_NUMBER, port);
    	resultIntent.putExtra(LOGON_CALL, logon);
    	// TODO Add extras or a data URI to this intent as appropriate.
    	setResult(Activity.RESULT_OK, resultIntent);
    	finish();
    }

}
