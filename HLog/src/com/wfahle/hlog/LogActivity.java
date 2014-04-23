package com.wfahle.hlog;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.wfahle.hlog.contentprovider.QSOContactProvider;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LogActivity extends Activity {

	public static final String QSO_RXFREQ = "com.wfahle.hlog.QSO_RXFREQ";
	public static final String QSO_TXFREQ = "com.wfahle.hlog.QSO_TXFREQ";
	public static final String QSO_CALL = "com.wfahle.hlog.QSO_CALL";
	public static final String QSO_RRST = "com.wfahle.hlog.QSO_RRST";
	public static final String QSO_SRST = "com.wfahle.hlog.QSO_SRST";
	public static final String QSO_MODE = "com.wfahle.hlog.QSO_MODE";
	public static final String QSO_TIMEON = "com.wfahle.hlog.QSO_TIMEON";
	public static final String QSO_TIMEOFF = "com.wfahle.hlog.QSO_TIMEOFF";
	public static final String QSO_NAME = "com.wfahle.hlog.QSO_NAME";
	public static final String QSO_QTH = "com.wfahle.hlog.QSO_QTH";
	public static final String QSO_STATE = "com.wfahle.hlog.QSO_STATE";
	public static final String QSO_COUNTRY = "com.wfahle.hlog.QSO_COUNTRY";
	public static final String QSO_GRID = "com.wfahle.hlog.QSO_GRID";
	private AsyncTask<LogActivity, Integer, QRZprofile> updatetask;
	public ProgressDialog progressDialog;	
	private Uri contactUri=null;
	private boolean needsLookup=false;
	
	private void fillData(Uri uri) {
		if (uri != null)
		{
			String[] projection = {    QSOContactTable.KEY_ID, QSOContactTable.KEY_CALL, QSOContactTable.KEY_RXFREQ, QSOContactTable.KEY_TXFREQ,
					QSOContactTable.KEY_TIMEON, QSOContactTable.KEY_TIMEOFF, QSOContactTable.KEY_MODE, QSOContactTable.KEY_RRST,
					QSOContactTable.KEY_SRST, QSOContactTable.KEY_NAME, QSOContactTable.KEY_QTH, QSOContactTable.KEY_STATE, 
					QSOContactTable.KEY_COUNTRY, QSOContactTable.KEY_GRID };
		    Cursor cursor = getContentResolver().query(uri, projection, null, null,
		        null);
		    if (cursor != null) {
		    	cursor.moveToFirst();
	
		    	EditText edit = (EditText) findViewById(R.id.timeon_edit);
		    	String call = cursor.getString(cursor
				          .getColumnIndexOrThrow(QSOContactTable.KEY_CALL));
		    	String freq = cursor.getString(cursor
				          .getColumnIndexOrThrow(QSOContactTable.KEY_RXFREQ));
		    	String mode = cursor.getString(cursor
				          .getColumnIndexOrThrow(QSOContactTable.KEY_MODE));
		    	String rrst = cursor.getString(cursor
				          .getColumnIndexOrThrow(QSOContactTable.KEY_RRST));
		    	String srst = cursor.getString(cursor
				          .getColumnIndexOrThrow(QSOContactTable.KEY_SRST));
		    	String timeon = cursor.getString(cursor
				          .getColumnIndexOrThrow(QSOContactTable.KEY_TIMEON));
		    	String timeoff = cursor.getString(cursor
				          .getColumnIndexOrThrow(QSOContactTable.KEY_TIMEOFF));
		    	String name = cursor.getString(cursor
				          .getColumnIndexOrThrow(QSOContactTable.KEY_NAME));
		    	String qth = cursor.getString(cursor
				          .getColumnIndexOrThrow(QSOContactTable.KEY_QTH));
		    	String state = cursor.getString(cursor
				          .getColumnIndexOrThrow(QSOContactTable.KEY_STATE));
		    	String country = cursor.getString(cursor
				          .getColumnIndexOrThrow(QSOContactTable.KEY_COUNTRY));
		    	String grid = cursor.getString(cursor
				          .getColumnIndexOrThrow(QSOContactTable.KEY_GRID));
		    	
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
		    	edit = (EditText) findViewById(R.id.timeon_edit);
		    	if (timeon == null || timeon.length() == 0)
		    	{
		    		needsLookup = true;
		    		edit.setText(now());
		    	}
		    	else
		        	edit.setText(timeon);
		    	edit = (EditText) findViewById(R.id.timeoff_edit);
		    	edit.setText(timeoff);
		    	edit = (EditText) findViewById(R.id.name_edit);
		    	edit.setText(name);
		    	edit = (EditText) findViewById(R.id.qth_edit);
		    	edit.setText(qth);
		    	edit = (EditText) findViewById(R.id.state_edit);
		    	edit.setText(state);
		    	edit = (EditText) findViewById(R.id.country_edit);
		    	edit.setText(country);
		    	edit = (EditText) findViewById(R.id.grid_edit);
		    	edit.setText(grid);
			    // always close the cursor
			    cursor.close();
		    }
	    }
	}
	CharSequence now() {
    	TimeZone tz = TimeZone.getTimeZone("GMT+0");
    	Calendar cal = Calendar.getInstance(tz);
    	String month = "0"+(cal.get(Calendar.MONTH)+1);
    	String day = "0"+cal.get(Calendar.DATE);
    	String hour = "0"+cal.get(Calendar.HOUR_OF_DAY);
    	String minute = "0"+cal.get(Calendar.MINUTE);
    	String second = "0"+cal.get(Calendar.SECOND);
    	month = month.substring(month.length()-2); // makes 3 into 03, e.g.
    	day = day.substring(day.length()-2);
    	hour = hour.substring(hour.length()-2);
    	minute = minute.substring(minute.length()-2);
    	second = second.substring(second.length()-2);
    	CharSequence text = ""+cal.get(Calendar.YEAR)+"-"+month+"-"+
    			day+" "+ hour +":"+minute+":"+second;
    	return text;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		// Show the Up button in the action bar.
		setupActionBar();
        // Or passed from the other activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
          contactUri = extras
              .getParcelable(QSOContactProvider.CONTENT_ITEM_TYPE);

          fillData(contactUri);
        }

/*    	EditText edit = (EditText) findViewById(R.id.timeon_edit);
    	Intent intent = getIntent();
    	String call = intent.getStringExtra(LogActivity.QSO_CALL);
    	String freq = intent.getStringExtra(LogActivity.QSO_RXFREQ);
    	String mode = intent.getStringExtra(LogActivity.QSO_MODE);
    	String rrst = intent.getStringExtra(LogActivity.QSO_RRST);
    	String srst = intent.getStringExtra(LogActivity.QSO_SRST);
    	String timeon = intent.getStringExtra(LogActivity.QSO_TIMEON);
    	String timeoff = intent.getStringExtra(LogActivity.QSO_TIMEOFF);
    	String name = intent.getStringExtra(LogActivity.QSO_NAME);
    	String qth = intent.getStringExtra(LogActivity.QSO_QTH);
    	String state = intent.getStringExtra(LogActivity.QSO_STATE);
    	String country = intent.getStringExtra(LogActivity.QSO_COUNTRY);
    	String grid = intent.getStringExtra(LogActivity.QSO_COUNTRY);
    	
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
    	edit = (EditText) findViewById(R.id.timeon_edit);
    	if (timeon == null || timeon.length() == 0)
    	{
    		needsLookup = true;
    		edit.setText(now());
    	}
    	else
        	edit.setText(timeon);
    	edit = (EditText) findViewById(R.id.timeoff_edit);
    	edit.setText(timeoff);
    	edit = (EditText) findViewById(R.id.name_edit);
    	edit.setText(name);
    	edit = (EditText) findViewById(R.id.qth_edit);
    	edit.setText(qth);
    	edit = (EditText) findViewById(R.id.state_edit);
    	edit.setText(state);
    	edit = (EditText) findViewById(R.id.country_edit);
    	edit.setText(country);
    	edit = (EditText) findViewById(R.id.grid_edit);
    	edit.setText(grid);*/
		SharedPreferences settings = getSharedPreferences(ConfigActivity.PREFS_NAME, 0);
		final String qrzUser = settings.getString("qrzUser", null);
		final String qrzPassword = settings.getString("qrzPassword", null);

		if (!needsLookup || qrzUser == null || qrzPassword == null || qrzUser.length() == 0
		    || qrzPassword.length() == 0) {
			/* could give them the ability to enter just this info through the config activity, but I won't 
		  LinearLayout searchLL = (LinearLayout) findViewById(R.id.SearchLL01);
		  searchLL.setVisibility(View.INVISIBLE);
		  Intent i = new Intent(this, Settings.class);
		  this.startActivity(i);
		  this.finish();
		  */
			//skip qrz
		}
		else {
	        if (LogActivity.this.updatetask == null) {
	            Log.d("startDownloading", "task was null, calling execute");
	            LogActivity.this.updatetask = new GetProfileTask().execute(LogActivity.this);
	          } else {
	            Status s = LogActivity.this.updatetask.getStatus();
	            if (s == Status.FINISHED) {
	              Log.d("updatetask",
	                  "task wasn't null, status finished, calling execute");
	              LogActivity.this.updatetask = new GetProfileTask().execute(LogActivity.this);
	            }
	          }

		}
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
    	EditText timeon_edit = (EditText) findViewById(R.id.timeon_edit);
    	String timeon = timeon_edit.getEditableText().toString();
    	EditText timeoff_edit = (EditText) findViewById(R.id.timeoff_edit);
    	String timeoff = timeoff_edit.getEditableText().toString();
    	EditText name_edit = (EditText) findViewById(R.id.name_edit);
    	String name = name_edit.getEditableText().toString();
    	EditText qth_edit = (EditText) findViewById(R.id.qth_edit);
    	String qth = qth_edit.getEditableText().toString();
    	EditText state_edit = (EditText) findViewById(R.id.state_edit);
    	String state = state_edit.getEditableText().toString();
    	EditText country_edit = (EditText) findViewById(R.id.country_edit);
    	String country = country_edit.getEditableText().toString();
    	EditText grid_edit = (EditText) findViewById(R.id.grid_edit);
    	String grid = grid_edit.getEditableText().toString();
    	
    	if (timeoff == null || timeoff.length() == 0)
    		timeoff = now().toString();
		ContentValues values = new ContentValues();
		values.put(QSOContactTable.KEY_CALL, call);
		values.put(QSOContactTable.KEY_TXFREQ, freq);
		values.put(QSOContactTable.KEY_RXFREQ, freq);
		values.put(QSOContactTable.KEY_MODE, mode);
		values.put(QSOContactTable.KEY_RRST, rrst);
		values.put(QSOContactTable.KEY_SRST, srst);
		values.put(QSOContactTable.KEY_TIMEON, timeon);
		values.put(QSOContactTable.KEY_TIMEOFF, timeoff);
		values.put(QSOContactTable.KEY_NAME, name);
		values.put(QSOContactTable.KEY_QTH, qth);
		values.put(QSOContactTable.KEY_STATE, state);
		values.put(QSOContactTable.KEY_COUNTRY, country);
		values.put(QSOContactTable.KEY_GRID, grid);
		
		if (contactUri == null) {
		      // New qso
		      contactUri = getContentResolver().insert(QSOContactProvider.CONTENT_URI, values);
		} else {
		      // Update qso
		      getContentResolver().update(contactUri, values, null, null);
		}
    	resultIntent.putExtra(QSOContactProvider.CONTENT_ITEM_TYPE, contactUri);

    	setResult(Activity.RESULT_OK, resultIntent);
    	finish();
	}
	
	public void clickNowOn(View view)
	{
    	EditText edit = (EditText) findViewById(R.id.timeon_edit);
    	CharSequence text = now();
    	edit.setText(text);
	}
	public void clickNowOff(View view)
	{
    	EditText edit = (EditText) findViewById(R.id.timeoff_edit);    	
    	CharSequence text = now();
    	edit.setText(text);
	}
	 private class GetProfileTask extends AsyncTask<LogActivity, Integer, QRZprofile> {

//		    private int linkColor = Color.rgb(Integer.parseInt("66", 16),Integer.parseInt("77", 16),Integer.parseInt("FF", 16));

		    LogActivity that;

		    protected QRZprofile doInBackground(LogActivity... thats) {

		      if (that == null) {
		        this.that = thats[0];
		      }

		      QRZprofile profile = null;

		      publishProgress(0);

		      try {
		        SharedPreferences settings = getSharedPreferences(ConfigActivity.PREFS_NAME, 0);
		        final String qrzUser = settings.getString("qrzUser", null);
		        final String qrzPassword = settings.getString("qrzPassword", null);

		        QRZrequest qrzDb = new QRZrequest(qrzUser, qrzPassword);

		        final EditText callsignSearchText = (EditText) findViewById(R.id.callt_edit);
/*
		        TableLayout profileTable = (TableLayout) findViewById(R.id.ProfileTable);
		        profileTable.removeAllViewsInLayout();
*/
		        String callsign = callsignSearchText.getText().toString().trim().toLowerCase(Locale.US);
		        if (callsign.contains(" ")) {
		          callsign = LogActivity.extractCharsFromPhonetic(callsign);
		        }
		        profile = qrzDb.getHamByCallsign(callsign.replace(" ", ""));

		      } catch (Exception e) {
		        e.printStackTrace();
		      }

		      publishProgress(100);

		      return profile;
		    }

		    protected void onProgressUpdate(Integer... progress) {
		      Log.d("onProgressUpdate", progress[0].toString());
		      if (progress[0] == 0) {
		        that.progressDialog = ProgressDialog.show(that, "Ham",
		            "Querying QRZ.com API", true, false);
		      }
		      if (progress[0] == 100) {
		        that.progressDialog.dismiss();
		      }

		    }

		    protected void onPostExecute(QRZprofile result) {
		    	/*
		      LinearLayout resultsLL = (LinearLayout) findViewById(R.id.ResultsLL);

		      TableLayout profileTable = (TableLayout) findViewById(R.id.ProfileTable);
		      profileTable.removeAllViewsInLayout();
              */
		      if (result == null) {
		        Toast
		            .makeText(
		                that.getBaseContext(),
		                "No results found, incorrect QRZ.com username/password set or no QRZ.com subscription",
		                Toast.LENGTH_LONG).show();
/*		        resultsLL.setVisibility(View.INVISIBLE); */
		        return;
		      }

		      EditText nameText = (EditText) findViewById(R.id.name_edit);
		      if (nameText.getEditableText().toString().length() == 0) {
			      String displayName = "";
			      if (result.getFname() != null && result.getFname().length() > 0) {
			        displayName = result.getFname();
			        if (result.getName() != null && result.getName().length() > 0) {
			          displayName += " " + result.getName();
			        }
			      } else {
			        if (result.getName() != null && result.getName().length() > 0) {
			          displayName = result.getName();
			        }
			      }
	
			      if (displayName.length() > 0) {
			        nameText.setText(displayName);
			      } else {
	//		        nameText.setText("no name?"); // leave name alone
			      }
		      }
		      /* skip image for now, but hey, cool
		      if (result.getImage() != null) {
		        TableRow tr = new TableRow(that);
		        TextView tv = new TextView(that);
		        tr.addView(tv);
		        Spanned htmlString = Html.fromHtml("<img src='" + result.getImage()
		            + "'>", new ImageGetter() {

		          public Drawable getDrawable(String source) {

		            try {

		              Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(
		                  source).getContent());
		              Drawable d = new BitmapDrawable(bitmap);
		              d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		              return d;
		            } catch (MalformedURLException e) {
		              e.printStackTrace();
		            } catch (IOException e) {
		              e.printStackTrace();
		            }
		            return null;
		          }
		        }, null);
		        tv.setAutoLinkMask(Linkify.ALL);
		        tv.setText(htmlString);
		        profileTable.addView(tr);
		      }
              */
		      /* already have call populated, don't overwrite in case we have /p or something
		      if (result.getCall() != null) {
		        TableRow tr = new TableRow(that);
		        TextView tv = new TextView(that);
		        tr.addView(tv);
		        tv.setText("Callsign: " + result.getCall());
		        tv.setTextSize(24);
		        profileTable.addView(tr);
		      }
		      */

		      /* don't use these fields for now
		      if (result.getEmail() != null) {
		        TableRow tr = new TableRow(that);
		        TextView tv = new TextView(that);
		        tr.addView(tv);
		        tv.setAutoLinkMask(Linkify.ALL);
		        tv.setText("Email: " + result.getEmail());
		        tv.setTextSize(24);
		        profileTable.addView(tr);
		      } else {
		        // emailText.setText("Email: n/a");
		      }
		      if (result.getAddr1() != null) {
		        TableRow tr = new TableRow(that);
		        TextView tv = new TextView(that);
		        tr.addView(tv);
		        tv.setText("Street: " + result.getAddr1());
		        tv.setTextSize(24);
		        profileTable.addView(tr);
		      } else {
		        // stateText.setText("State: n/a");
		      }
		      */
    		  EditText qth_edit = (EditText) findViewById(R.id.qth_edit);
		      if (result.getAddr2() != null && qth_edit.getText().length()==0) {
		    	  qth_edit.setText(result.getAddr2());
		      } else {
		        // stateText.setText("State: n/a");
		      }

		      EditText state_edit = (EditText) findViewById(R.id.state_edit);
		      if (result.getState() != null && state_edit.getText().length()==0) {
		      	state_edit.setText(result.getState());
		      } else {
		        // stateText.setText("State: n/a");
		      }

		      EditText country_edit = (EditText) findViewById(R.id.country_edit);
		      if (result.getCountry() != null && country_edit.getText().length()==0) {
		      	country_edit.setText(result.getCountry());
		      } else {
		        // countryText.setText("Country: n/a");
		      }
		      
		      EditText grid_edit = (EditText) findViewById(R.id.grid_edit);

		      if (result.getGrid() != null && grid_edit.getText().length()==0) {
			    grid_edit.setText(result.getGrid());
			    /* ok this would be cool for a button next to the grid
		        TableRow tr = new TableRow(that);
		        LinearLayout ll = new LinearLayout(that);
		        ll.setOrientation(LinearLayout.HORIZONTAL);
		        TextView tv = new TextView(that);
		        tv.setText("Grid: ");
		        tv.setTextSize(24);
		        TextView tv2 = new TextView(that);
		        SpannableString gridSpan = new SpannableString(result.getGrid());
		        gridSpan.setSpan(new UnderlineSpan(), 0, gridSpan.length(), 0);
		        tv2.setText(gridSpan);
		        tv2.setTextSize(24);
		        tv2.setTextColor(linkColor);
		        tv2.setOnClickListener(new OnClickListener() {

		          public void onClick(View v) {
		            if (v instanceof TextView) {
		              TextView tv = (TextView) v;
		              Intent i = new Intent(Intent.ACTION_VIEW);
		              String substring = tv.getText().toString();
		              Location loc = new Location(substring);
		              String uriString = "geo:"
		                  + ((int) (loc.getLatitude().toDegrees() * 1000) / 1000.0) + ","
		                  + ((int) (loc.getLongitude().toDegrees() * 1000) / 1000.0)
		                  + "?z=15";
		              Log.d("QRZ", "grid click, launching intent using uri=" + uriString);
		              i.setData(Uri.parse(uriString));
		              that.startActivity(i);
		            } else {
		              Log.e("QRZ", "v not instanceof TextView");
		            }
		          }
		        });
		        */

		      } else {
		        // gridText.setText("Grid: n/a");
		      }

		      /* bearing and such, nice feature
		      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		      boolean lbsEnabled = settings.getBoolean("lbsEnabled", true);

		      if (result.getGrid() != null && lbsEnabled) {

		        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		        android.location.Location bestLocation = locationManager
		            .getLastKnownLocation(Geo.getBestProviderWithGPSFallback(locationManager));


		        if (bestLocation != null) {
		          Location theirLocation = new Location(result.getGrid());
		          Location myLocation = new Location(bestLocation.getLatitude(),
		              bestLocation.getLongitude());
		          double distance = myLocation.getDistanceMi(theirLocation);
		          TableRow tr = new TableRow(that);
		          TextView tv = new TextView(that);
		          tr.addView(tv);
		          tv.setText("Distance: " + ((int) (distance * 100)) / 100.0 + " mi");
		          tv.setTextSize(24);
		          profileTable.addView(tr);

		          int bearing = (int) myLocation.getBearing(theirLocation);
		          tr = new TableRow(that);
		          tv = new TextView(that);
		          tr.addView(tv);
		          tv.setText("Bearing: " + bearing + " deg");
		          tv.setTextSize(24);
		          profileTable.addView(tr);
		        }


		      } else {
		        // gridText.setText("Grid: n/a");
		      }
              */
		      /* not using web link, ham class, etc
		      if (result.getUrl() != null) {
		        TableRow tr = new TableRow(that);
		        TextView tv = new TextView(that);
		        tr.addView(tv);
		        // Spanned htmlString = Html.fromHtml("Web: " + "<a href='" +
		        // result.getUrl() + "'>" + result.getUrl() + "</a>");
		        tv.setAutoLinkMask(Linkify.ALL);
		        tv.setText("Web: " + result.getUrl());
		        tv.setTextSize(24);
		        profileTable.addView(tr);
		      }

		      String hamClass = result.getHamclass();
		      if (hamClass != null) {

		        if (hamClass.equalsIgnoreCase("e")) {
		          hamClass = "Extra";
		        } else if (hamClass.equalsIgnoreCase("a")) {
		          hamClass = "Advanced";
		        } else if (hamClass.equalsIgnoreCase("t")) {
		          hamClass = "Tech";
		        } else if (hamClass.equalsIgnoreCase("g")) {
		          hamClass = "General";
		        } else if (hamClass.equalsIgnoreCase("c")) {
		          hamClass = "Club";
		        }

		        TableRow tr = new TableRow(that);
		        TextView tv = new TextView(that);
		        tr.addView(tv);
		        tv.setText("Class: " + hamClass);
		        tv.setTextSize(24);
		        profileTable.addView(tr);
		      } else {
		        // classText.setText("Class: n/a");
		      }

		      if (result.getEfdate() != null) {
		        TableRow tr = new TableRow(that);
		        TextView tv = new TextView(that);
		        tr.addView(tv);
		        tv.setText("Effective: " + result.getEfdate());
		        tv.setTextSize(24);
		        profileTable.addView(tr);
		      }

		      if (result.getExpdate() != null) {
		        TableRow tr = new TableRow(that);
		        TextView tv = new TextView(that);
		        tr.addView(tv);
		        tv.setText("Expires: " + result.getExpdate());
		        tv.setTextSize(24);
		        profileTable.addView(tr);
		      } else {
		        // expiresText.setText("Expires: n/a");
		      }

		      if (result.getU_views() != null) {
		        TableRow tr = new TableRow(that);
		        TextView tv = new TextView(that);
		        tr.addView(tv);
		        tv.setText("Views: " + result.getU_views());
		        tv.setTextSize(24);
		        profileTable.addView(tr);
		      }
		       */
		      /*
		       * if (result.getBio() != null) { TableRow tr = new TableRow(that);
		       * TextView tv = new TextView(that); TextView tv1 = new TextView(that);
		       * tr.addView(tv1); tv1.setText("Bio: "); tr.addView(tv); Spanned
		       * htmlString = Html.fromHtml(result.getBio()); tv.setText(htmlString);
		       * profileTable.addView(tr); }
		       */

		    }
		  }

		  public static String extractCharsFromPhonetic(String input) {
		    return input.replaceAll("x ray", "x").replaceAll("alpha", "a")
		        .replaceAll("bravo", "b").replaceAll("charlie", "c")
		        .replaceAll("delta", "d").replaceAll("echo", "e")
		        .replaceAll("foxtrot", "f").replaceAll("golf", "g")
		        .replaceAll("hotel", "h").replaceAll("india", "i")
		        .replaceAll("juliet", "j").replaceAll("kilo", "k")
		        .replaceAll("lima", "l").replaceAll("mike", "m")
		        .replaceAll("november", "n").replaceAll("oscar", "o")
		        .replaceAll("papa", "p").replaceAll("quebec", "q")
		        .replaceAll("romeo", "r").replaceAll("sierra", "s")
		        .replaceAll("tango", "t").replaceAll("uniform", "u")
		        .replaceAll("victor", "v").replaceAll("whiskey", "w")
		        .replaceAll("yankee", "y").replaceAll("zulu", "z")
		        .replaceAll("one", "1").replaceAll("two", "2").replaceAll("three", "3")
		        .replaceAll("four", "4").replaceAll("five", "5").replaceAll("six", "6")
		        .replaceAll("seven", "7").replaceAll("eight", "8")
		        .replaceAll("niner", "9").replaceAll("nine", "9")
		        .replaceAll("zero", "0").replaceAll("x-ray", "x");
		  }	
}
