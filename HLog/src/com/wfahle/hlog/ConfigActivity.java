package com.wfahle.hlog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class ConfigActivity extends Activity {

	public static final String PREFS_NAME = "com.wfahle.hlog.PREFS_NAME";
	public static final String NONPREF_NAME = "com.wfahle.hlog.NONPREF_NAME";
	public static final String CLUSTER_SERVER = "com.wfahle.hlog.CLUSTER_SERVER";
	public static final String TELNET_PORT = "com.wfahle.hlog.TELNET_PORT";
	public static final String PIGLET_ID = "com.wfahle.hlog.PIGLET_ID";
	public static final String PIGLET_PORT = "com.wfahle.hlog.PIGLET_PORT";
	public static final String TELNET_LOGON = "com.wfahle.hlog.TELNET_LOGON";
	public static final String QRZ_USER = "com.wfahle.hlog.QRZ_USER";
	public static final String QRZ_PASSWORD = "com.wfahle.hlog.QRZ_PASSWORD";
	public static final String QRZ_AUTO = "com.wfahle.hlog.QRZ_AUTO";
	private boolean cancelled = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);

		cancelled = false;
	    SharedPreferences settings = getSharedPreferences(NONPREF_NAME, 0);
	    if (settings == null || !settings.contains("CLUSTER_SERVER"))
	    	settings = getSharedPreferences(PREFS_NAME, 0);
	    else {
	        final SharedPreferences.Editor editor = settings.edit();
	        editor.clear(); // clear out the non-preferred if they don't get saved
	        editor.apply();
	    }
	    	
		final EditText server_edit = (EditText) findViewById(R.id.cluster_edit);
		final EditText cport_edit = (EditText) findViewById(R.id.cport_edit);		
		final EditText logon_edit = (EditText) findViewById(R.id.yourcall_edit);
		final EditText rserver_edit = (EditText) findViewById(R.id.piglet_edit);
		final EditText pport_edit = (EditText) findViewById(R.id.pport_edit);
		final EditText qrzpassword_edit = (EditText) findViewById(R.id.qrzpassword_edit);
		final EditText qrzuser_edit = (EditText) findViewById(R.id.qrz_user_edit);
		final CheckBox checkBox = (CheckBox) findViewById(R.id.auto_check);
		server_edit.setText(settings.getString(CLUSTER_SERVER, null));
		cport_edit.setText(Integer.toString(settings.getInt(TELNET_PORT, 23)));
		logon_edit.setText(settings.getString(TELNET_LOGON, null));
		rserver_edit.setText(settings.getString(PIGLET_ID, null));
		pport_edit.setText(Integer.toString(settings.getInt(PIGLET_PORT, 7373)));
		qrzpassword_edit.setText(settings.getString(QRZ_PASSWORD, null));
		qrzuser_edit.setText(settings.getString(QRZ_USER, null));
        checkBox.setChecked(settings.getBoolean(QRZ_AUTO, true));

		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	protected void saveConfig(boolean preferred)
	{
    	final EditText logon_edit = (EditText) findViewById(R.id.yourcall_edit);
    	final EditText server_edit = (EditText) findViewById(R.id.cluster_edit);
    	final EditText cport_edit = (EditText) findViewById(R.id.cport_edit);
		final EditText rserver_edit = (EditText) findViewById(R.id.piglet_edit);
		final EditText pport_edit = (EditText) findViewById(R.id.pport_edit);
    	final EditText qrzon_edit = (EditText) findViewById(R.id.qrz_user_edit);
		final EditText qrzpassword_edit = (EditText) findViewById(R.id.qrzpassword_edit);
		final CheckBox checkBox = (CheckBox) findViewById(R.id.auto_check);
		
/* removing this telent config nonsense - maybe we can use it later for saving interchangable configs
		TelnetConfig cfg = new TelnetConfig(_id, call, server, port, rserver, rport, preferred);
		LocalDBHandler ldb = new LocalDBHandler(getBaseContext());
		if (_id == 0)
			ldb.addConfig(cfg);
		else
			ldb.updateConfig(cfg); */
        final SharedPreferences settings = preferred?getSharedPreferences(PREFS_NAME, 0):
        		getSharedPreferences(NONPREF_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();
        editor.putString(QRZ_USER, qrzon_edit.getEditableText().toString());
        editor.putString(QRZ_PASSWORD, qrzpassword_edit.getText().toString());
        editor.putString(TELNET_LOGON, logon_edit.getEditableText().toString());
        editor.putString(CLUSTER_SERVER, server_edit.getEditableText().toString());
        editor.putInt(TELNET_PORT, Integer.parseInt(cport_edit.getText().toString()));
        editor.putString(PIGLET_ID, rserver_edit.getEditableText().toString());
        editor.putInt(PIGLET_PORT, Integer.parseInt(pport_edit.getText().toString()));
        editor.putBoolean(QRZ_AUTO, checkBox.isChecked());
        
//        editor.putBoolean("lbsEnabled", false);
        editor.commit();

	}
	@Override
	protected void onPause()
	{
		super.onPause();
		if (!cancelled)
			saveConfig(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			done(null);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    public void done(View view) {
    	saveConfig(true);
    	Intent resultIntent = new Intent();
    	setResult(Activity.RESULT_OK, resultIntent);
    	finish();
    }
    public void onCancel(View view) {
    	cancelled = true;
//    	saveConfig(true);
    	Intent resultIntent = new Intent();
    	setResult(Activity.RESULT_CANCELED, resultIntent);
    	finish();
    }

}
