package com.wfahle.hlog;

import java.util.Calendar;
import java.util.TimeZone;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class LogActivity extends Activity {

	public static final String QSO_FREQ = "com.wfahle.hlog.QSO_FREQ";
	public static final String QSO_CALL = "com.wfahle.hlog.QSO_CALL";
	public static final String QSO_RRST = "com.wfahle.hlog.QSO_RRST";
	public static final String QSO_SRST = "com.wfahle.hlog.QSO_SRST";
	public static final String QSO_MODE = "com.wfahle.hlog.QSO_MODE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		// Show the Up button in the action bar.
		setupActionBar();
    	// TODO: do lookup of contact in QRZ		
    	EditText edit = (EditText) findViewById(R.id.timeon_edit);
    	TimeZone tz = TimeZone.getTimeZone("GMT+0");
    	Calendar cal = Calendar.getInstance(tz);
    	
    	CharSequence text = ""+cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+
    			cal.get(Calendar.DATE)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
    	edit.setText(text); 
    	Intent intent = getIntent();
    	String call = intent.getStringExtra(LogActivity.QSO_CALL);
    	String freq = intent.getStringExtra(LogActivity.QSO_FREQ);
    	String mode = intent.getStringExtra(LogActivity.QSO_MODE);
    	String rrst = intent.getStringExtra(LogActivity.QSO_RRST);
    	String srst = intent.getStringExtra(LogActivity.QSO_SRST);
    	edit = (EditText) findViewById(R.id.callt_edit);
    	edit.setText(call);
    	edit = (EditText) findViewById(R.id.rxfreqt_edit);
    	edit.setText(freq);
    	edit = (EditText) findViewById(R.id.modet_edit);
    	edit.setText(mode);
    	edit = (EditText) findViewById(R.id.rrstt_edit);
    	edit.setText(rrst);
    	edit = (EditText) findViewById(R.id.srstt_edit);
    	edit.setText(srst);
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
		getMenuInflater().inflate(R.menu.log, menu);
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
			clickSave(null);
//			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void clickCancel(View view)
	{
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, resultIntent);
		finish();
	}
	public void clickSave(View view)
	{
        /* write you handling code like...
        String st = "sdcard/";
        File f = new File(st+o.toString());
        // do whatever u want to do with 'f' File object
        */ 
    	// on config screen? do export of adif to file and/or email
    	Intent resultIntent = new Intent();
    	EditText call_edit = (EditText) findViewById(R.id.callt_edit);
    	String call = call_edit.getEditableText().toString();
    	EditText freq_edit = (EditText) findViewById(R.id.rxfreqt_edit);
    	String freq = freq_edit.getEditableText().toString();
    	EditText rrst_edit = (EditText) findViewById(R.id.rrstt_edit);
    	String rrst = rrst_edit.getEditableText().toString();
    	EditText srst_edit = (EditText) findViewById(R.id.srstt_edit);
    	String srst = srst_edit.getEditableText().toString();
    	EditText mode_edit = (EditText) findViewById(R.id.modet_edit);
    	String mode = mode_edit.getEditableText().toString();
    	resultIntent.putExtra(QSO_FREQ, freq);
    	resultIntent.putExtra(QSO_CALL, call);
    	resultIntent.putExtra(QSO_RRST, rrst);
    	resultIntent.putExtra(QSO_SRST, srst);    	
    	resultIntent.putExtra(QSO_MODE, mode);
    	// TODO Add extras or a data URI to this intent as appropriate.
    	setResult(Activity.RESULT_OK, resultIntent);
    	finish();
	}
	public void clickNowOn(View view)
	{
    	EditText edit = (EditText) findViewById(R.id.timeon_edit);
    	TimeZone tz = TimeZone.getTimeZone("GMT+0");
    	Calendar cal = Calendar.getInstance(tz);
    	
    	CharSequence text = ""+cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+
    			cal.get(Calendar.DATE)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
    	edit.setText(text); 
	}
	public void clickNowOff(View view)
	{
    	EditText edit = (EditText) findViewById(R.id.timeoff_edit);
    	TimeZone tz = TimeZone.getTimeZone("GMT+0");
    	Calendar cal = Calendar.getInstance(tz);
    	
    	CharSequence text = ""+cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+
    			cal.get(Calendar.DATE)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
    	edit.setText(text);
	}
}
