package com.wfahle.hlog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wfahle.hlog.contentprovider.QSOContactProvider;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class EntryActivity extends Activity {
	protected final static int config_request = 1; // enum of all the requests would be better
	protected final static int log_request = 2; // enum of all the requests would be better
	protected final static int loggedOut = 0;
	protected final static int loggingIn = 1;
	protected final static int loggedIn = 2;

	protected String telnetServer="";
	protected int telnetPort = 23;
	protected String telnetLogon = "";
	protected String qsoTFreq = "";
	protected String qsoRFreq = "";
	protected String qsoCall = "";
	protected String qsoMode = "";
	protected String qsoRRST = "";
	protected String qsoSRST = "";
	protected String qsoTimeon = "";
	protected String qsoTimeoff = "";
	protected String qsoName = "";
	protected String qsoQTH = "";
	protected String qsoState = "";
	protected String qsoCountry = "";
	protected String qsoGrid = "";
    EditText callBox;
    EditText txfreqBox;
    EditText rxfreqBox;
    EditText modeBox;
    EditText rrstBox;
    EditText srstBox;

	protected TerminalSocket st = null;
	protected int state=loggedOut;
	private Uri contactUri;

	protected String mode(String freq)
	{
		String ret = "";
		double f = Double.parseDouble(freq);
		if (f > 1.8 && f < 1.843 )
			ret = "CW";
		else if (f >= 1.843 && f < 2.0 )
			ret = "SSB";
		else if (f >= 3.5 && f < 3.57)
			ret = "CW";
		else if (f >= 3.57 && f < 3.6)
			ret = "RTTY";
		else if (f >= 3.6 && f < 3.845)
			ret = "SSB";
		else if (f >= 3.845 && f <= 3.88)
			ret = "SSTV";
		else if (f>3.88 && f < 3.9)
			ret = "AM";
		else if (f > 3.9 && f < 4.0)
			ret = "SSB";
		else if (f >= 7.0 && f < 7.04)
			ret = "CW";
		else if (f >= 7.04 && f <= 7.045)
			ret = "RTTY";
		else if (f > 7.045 && f < 7.07)
			ret = "CW";
		else if (f >= 7.07 && f <= 7.075)
			ret = "PSK31";
		else if (f > 7.075 && f <= 7.08)
			ret = "JT65";
		else if (f > 7.08 && f < 7.125)
			ret = "CW";
		else if (f >= 7.125 && f < 7.290)
			ret = "SSB";
		else if (f >= 7.290 && f <= 7.3)
			ret = "AM";
		else if (f>=10.1 && f < 10.13)
			ret = "CW";
		else if (f >= 10.13 && f <=10.14)
			ret = "RTTY";
		else if (f > 10.14 && f <= 10.15)
			ret = "CW";
		else if (f >= 14.0 && f < 14.07)
			ret = "CW";
		else if (f >= 14.07 && f <= 14.095)
			ret = "RTTY";
		else if (f >= 14.095 && f < 14.15)
			ret = "CW";
		else if (f >= 14.15 && f < 14.35)
			ret = "SSB";
		else if (f >= 18.068 && f < 18.11)
			ret = "CW";
		else if (f >= 18.11 && f <= 18.168)
			ret = "SSB";
		else if (f >= 21.0 && f < 21.07)
			ret = "CW";
		else if (f >= 21.07 && f < 21.11)
			ret = "RTTY";
		else if (f >= 21.11 && f < 21.2)
			ret = "CW";
		else if (f >= 21.2 && f <= 21.45)
			ret = "SSB";
		else if (f >= 24.890 && f < 24.93)
			ret = "CW";
		else if (f >= 24.93 && f <= 24.99)
			ret = "SSB";
		else if (f >= 28.0 && f <= 28.3)
			ret = "CW";
		else if (f >= 28.3 && f < 29.0)
			ret = "SSB";
		else if (f >= 29.0 && f < 29.2)
			ret = "AM";
		else if (f >= 29.2 && f < 29.7)
			ret = "FM";
		else if (f >= 50.0 && f < 50.1)
			ret = "CW";
		else if (f >= 50.1 && f < 52.0)
			ret = "SSB";
		else if (f >= 52.0 && f <= 54.0)
			ret = "FM";
		return ret;
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        Bundle extras = getIntent().getExtras();
        callBox = (EditText) findViewById(R.id.call_edit);
        txfreqBox = (EditText) findViewById(R.id.txfreq_edit);
        rxfreqBox = (EditText) findViewById(R.id.rxfreq_edit);
        modeBox = (EditText) findViewById(R.id.mode_edit);
        rrstBox = (EditText) findViewById(R.id.rrst_edit);
        srstBox = (EditText) findViewById(R.id.srst_edit);

        // check from the saved Instance
        contactUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState
            .getParcelable(QSOContactProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
          contactUri = extras
              .getParcelable(QSOContactProvider.CONTENT_ITEM_TYPE);

          fillData(contactUri);
        }
        
        final ListView lv = (ListView) findViewById(R.id.spot_list);
        lv.setClickable(true);
        final ArrayList<String> list = new ArrayList<String>();
        final StableArrayAdapter adapter = new StableArrayAdapter(this, R.layout.spotview, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            Object o = lv.getItemAtPosition(position);
            
            String text = o.toString();
            int pos =  text.indexOf(' ');
            qsoCall = text.substring(0, pos);
            callBox.setText(qsoCall);
            int pos2 = text.indexOf(' ', pos+1);
            if (pos2 <=0)
            	pos2 = text.length();
            qsoRFreq = text.substring(pos+1, pos2);
            int posp = qsoRFreq.indexOf('.');
            // TODO: QSX 3.838, QSX 4, UP 5, DOWN 2, U 5, D4, U4, DN4, UP4, DOWN4, QSX7144
            String comment ="";
            if (pos2 < text.length())
            	comment = text.substring(pos2);
            if (comment.contains("up1") || comment.contains("up 1") ) // for now treat as up 1
            {
            	int addLoc = -1;
            	if (posp == -1)
            		addLoc = qsoRFreq.length()-1;
            	else
            		addLoc = posp - 1;
            	while (addLoc > 0)
            	{
            		char t = qsoRFreq.charAt(addLoc);
            		if (t == '9')
            		{
            			t = '0';
            			qsoTFreq = (qsoRFreq.substring(0, addLoc) + t);
            			if (addLoc < qsoRFreq.length()-1)
            				qsoTFreq = qsoTFreq + qsoRFreq.substring(addLoc+1);
            			
            			addLoc--;
            		}
            		else
            		{
            			t++;
            			qsoTFreq = (qsoRFreq.substring(0, addLoc) + t);
                    	if (addLoc < qsoRFreq.length()-1)
            				qsoTFreq = qsoTFreq + qsoRFreq.substring(addLoc+1);
            			addLoc = 0;
            		}
            	}
            		
            }
            else
            	qsoTFreq = qsoRFreq;
            qsoRFreq = convertToMHz(qsoRFreq, posp);
            qsoTFreq = convertToMHz(qsoTFreq, posp);
            txfreqBox.setText(qsoTFreq);
            rxfreqBox.setText(qsoRFreq);
            qsoMode = mode(qsoRFreq);
            modeBox.setText(qsoMode);
            if (qsoMode.equals("SSB") || qsoMode.equals("AM") || qsoMode.equals("FM"))
            {
            	qsoRRST = "59";
            	qsoSRST = "59";
            }
            else
            {
            	qsoRRST="599";
            	qsoSRST="599";
            }
        	rrstBox.setText(qsoRRST);
        	srstBox.setText(qsoSRST);
          }
        });
    }
    
	private void fillData(Uri uri) {
	    String[] projection = { QSOContactTable.KEY_CALL,
	        QSOContactTable.KEY_RXFREQ, QSOContactTable.KEY_TXFREQ, QSOContactTable.KEY_MODE, QSOContactTable.KEY_RRST, QSOContactTable.KEY_SRST };
	    Cursor cursor = getContentResolver().query(uri, projection, null, null,
	        null);
	    if (cursor != null) {
	      cursor.moveToFirst();

	      /* this is for a spinner - could be mode, for example, but have to maintain
	      String category = cursor.getString(cursor
	          .getColumnIndexOrThrow(QSOContactTable.COLUMN_CATEGORY));
	      for (int i = 0; i < mCategory.getCount(); i++) {

	        String s = (String) mCategory.getItemAtPosition(i);
	        if (s.equalsIgnoreCase(category)) {
	          mCategory.setSelection(i);
	        }
	      } */

	      callBox.setText(cursor.getString(cursor
		          .getColumnIndexOrThrow(QSOContactTable.KEY_CALL)));
	      txfreqBox.setText(cursor.getString(cursor
			      .getColumnIndexOrThrow(QSOContactTable.KEY_TXFREQ)));
	      rxfreqBox.setText(cursor.getString(cursor
			      .getColumnIndexOrThrow(QSOContactTable.KEY_RXFREQ)));
	      modeBox.setText(cursor.getString(cursor
			      .getColumnIndexOrThrow(QSOContactTable.KEY_MODE)));
	      rrstBox.setText(cursor.getString(cursor
			      .getColumnIndexOrThrow(QSOContactTable.KEY_RRST)));
	      srstBox.setText(cursor.getString(cursor
			      .getColumnIndexOrThrow(QSOContactTable.KEY_SRST)));
	      // always close the cursor
	      cursor.close();
	    }
	}

	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    saveState();
	    outState.putParcelable(QSOContactProvider.CONTENT_ITEM_TYPE, contactUri);
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    saveState();
	}

	private void saveState() {
	    qsoCall = callBox.getText().toString();
	    qsoTFreq = txfreqBox.getText().toString();
	    qsoRFreq = rxfreqBox.getText().toString();
	    qsoMode = modeBox.getText().toString();
	    qsoRRST = rrstBox.getText().toString();
	    qsoSRST = srstBox.getText().toString();

	    // only save if either callsign or frequency
	    // is available

	    if (qsoCall.length() == 0 && qsoRFreq.length() == 0) {
	      return;
	    }

		ContentValues values = new ContentValues();
		values.put(QSOContactTable.KEY_CALL, qsoCall);
		values.put(QSOContactTable.KEY_TXFREQ, qsoTFreq);
		values.put(QSOContactTable.KEY_RXFREQ, qsoRFreq);
		values.put(QSOContactTable.KEY_MODE, qsoMode);
		values.put(QSOContactTable.KEY_RRST, qsoRRST);
		values.put(QSOContactTable.KEY_SRST, qsoSRST);
		values.put(QSOContactTable.KEY_TIMEON, qsoTimeon);
		values.put(QSOContactTable.KEY_TIMEOFF, qsoTimeoff);
		values.put(QSOContactTable.KEY_NAME, qsoName);
		values.put(QSOContactTable.KEY_QTH, qsoQTH);
		values.put(QSOContactTable.KEY_STATE, qsoState);
		values.put(QSOContactTable.KEY_COUNTRY, qsoCountry);
		values.put(QSOContactTable.KEY_GRID, qsoGrid);
		
		if (contactUri == null) {
		      // New qso
		      contactUri = getContentResolver().insert(QSOContactProvider.CONTENT_URI, values);
		} else {
		      // Update todo
		      getContentResolver().update(contactUri, values, null, null);
		}
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      switch(requestCode) {
      	case (log_request) : {
            if (resultCode == Activity.RESULT_OK) {
            	qsoTFreq = data.getStringExtra(LogActivity.QSO_FREQ);
            	qsoRFreq = data.getStringExtra(LogActivity.QSO_FREQ);
            	qsoCall = data.getStringExtra(LogActivity.QSO_CALL);
            	qsoRRST = data.getStringExtra(LogActivity.QSO_RRST);
            	qsoSRST = data.getStringExtra(LogActivity.QSO_SRST);
            	qsoMode = data.getStringExtra(LogActivity.QSO_MODE);
            	qsoTimeon = data.getStringExtra(LogActivity.QSO_TIMEON);
            	qsoTimeoff = data.getStringExtra(LogActivity.QSO_TIMEOFF);
            	qsoName = data.getStringExtra(LogActivity.QSO_NAME);
            	qsoQTH = data.getStringExtra(LogActivity.QSO_QTH);
            	qsoState = data.getStringExtra(LogActivity.QSO_STATE);
            	qsoCountry = data.getStringExtra(LogActivity.QSO_COUNTRY);
            	qsoGrid = data.getStringExtra(LogActivity.QSO_GRID);
            	
                EditText callBox = (EditText) findViewById(R.id.call_edit);
                EditText txfreqBox = (EditText) findViewById(R.id.txfreq_edit);
                EditText rxfreqBox = (EditText) findViewById(R.id.rxfreq_edit);
                EditText modeBox = (EditText) findViewById(R.id.mode_edit);
                EditText rrstBox = (EditText) findViewById(R.id.rrst_edit);
                EditText srstBox = (EditText) findViewById(R.id.srst_edit);
                callBox.setText(qsoCall);
                txfreqBox.setText(qsoTFreq);
                rxfreqBox.setText(qsoRFreq);
                modeBox.setText(qsoMode);
            	rrstBox.setText(qsoRRST);
            	srstBox.setText(qsoSRST);
            	saveState();
            	newContact(null);
            }
    	  break;
      	}
        case (config_request) : {
          if (resultCode == Activity.RESULT_OK) {
        		telnetServer = data.getStringExtra(ConfigActivity.SERVER_NAME);
        		telnetPort = data.getIntExtra(ConfigActivity.PORT_NUMBER, 23);
        		telnetLogon = data.getStringExtra(ConfigActivity.LOGON_CALL);
          }
          break;
        } 
      }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    protected String convertToMHz(String freq, int posp)
    {
    
    	String ret = "";
        if (posp == -1)
        {
        	ret = freq.substring(0,freq.length()-3)+"."+freq.substring(freq.length()-3);
        }
        else
        {
        	if (freq.substring(posp+1).equals("0"))
        		ret = freq.substring(0, posp-3)+"."+freq.substring(posp-3, posp);
        	else
        		ret = freq.substring(0, posp-3)+"."+freq.substring(posp-3, posp)+freq.substring(posp+1);
        }
        return ret;
    }
    
    public class StableArrayAdapter extends ArrayAdapter<String> {

    	protected int curId = 0;
        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
    	
        public StableArrayAdapter(Context context, int textViewResourceId,
            List<String> objects) {
          super(context, textViewResourceId, objects);
          for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
            curId = i;
          }
        }

        @Override
        public long getItemId(int position) {
          String item = getItem(position);
          return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
          return true;
        }
        
        public void addItem(String item)
        {
        	super.add(item);
        	curId++;
        	mIdMap.put(item,  curId);
        }

      }
    
    public void newContact(View view) {
	      callBox.setText("");
	      txfreqBox.setText("");
	      rxfreqBox.setText("");
	      modeBox.setText("");
	      rrstBox.setText("");
	      srstBox.setText("");
	      fillData(null);
    }

    public void submitCall()
    {
    	if (st != null)
    	{
			try {
				st.oStream.write(st.Logon.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			state = loggedIn;
			Button button = (Button) findViewById(R.id.dxcStart);
			button.setText("Stop");
    	}
    }
    
	public void LogOn(View view) { /*telnet login button pressed */	
		if (state == loggingIn)
		{
			return;
		}
		else if (state == loggedIn) {
			if(st != null) {
				try {
					String quit = "q\r\n";
					st.oStream.write(quit.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			state = loggedOut;
			Button button = (Button) findViewById(R.id.dxcStart);
			button.setText(R.string.login_ui);
			st=null; // TODO: can we stop it instead?
		}
		else
		{
			if (st == null)
			{
			    final ListView lv = (ListView) findViewById(R.id.spot_list);
			    final TextView tv = (TextView) findViewById(R.id.dxc_out);
				final MyHandler hnd = new MyHandler(this, lv, tv);
				st = new TerminalSocket(hnd);
			}
			st.Server = telnetServer;
			st.Port = telnetPort;
			st.Logon = telnetLogon; // TODO: fire it up.
			st.SocketStart();
			state = loggingIn; // TODO: if it fails to log in, start over
			// TODO: once logged in, log out on inactivity for x minutes - config
		}
	}
	
    public void logContact(View view) {
    	saveState();
    	Intent intent = new Intent(this, LogActivity.class);
    	EditText call = (EditText) findViewById(R.id.call_edit);
    	EditText freq = (EditText) findViewById(R.id.rxfreq_edit);
    	EditText mode = (EditText) findViewById(R.id.mode_edit);
    	EditText rrst = (EditText) findViewById(R.id.rrst_edit);
    	EditText srst = (EditText) findViewById(R.id.srst_edit);
    	
    	intent.putExtra(LogActivity.QSO_CALL, call.getEditableText().toString());
    	intent.putExtra(LogActivity.QSO_FREQ, freq.getEditableText().toString());
    	intent.putExtra(LogActivity.QSO_MODE, mode.getEditableText().toString());
    	intent.putExtra(LogActivity.QSO_RRST, rrst.getEditableText().toString());
    	intent.putExtra(LogActivity.QSO_SRST, srst.getEditableText().toString());
     	
    	startActivityForResult(intent, log_request);
    }
    
    public void configureApp(View view) {
    	saveState();
    	Intent intent = new Intent(this, ConfigActivity.class);
    	startActivityForResult(intent, config_request);
    }
    
}

class Params {
	final int MAX_PARAMS = 10;
	int[] aParams = new int[MAX_PARAMS];
	boolean[] aIsUsed = new boolean[MAX_PARAMS];
	int iCurrent = 0;
	int iCount = 0;

	Params() {
		Clear();
	}

	void Clear() {
		for (int i = 0; i < MAX_PARAMS; i++) {
			aParams[i] = 0;
			aIsUsed[i] = false;
			iCurrent = 0;
			iCount = 0;
		}
	}

	int getCurrentParam() {
		return aParams[iCurrent];
	}

	void setCurrentParam(int val) {
		aParams[iCurrent] = val;
		aIsUsed[iCurrent] = true;
		iCount = iCurrent + 1;
	}

	int getParam(int i) {
		return aParams[i];
	}

	int getCount() {
		return iCount;
	}

	void nextParam() {
		iCurrent++;
	}
}
