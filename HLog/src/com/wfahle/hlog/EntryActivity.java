package com.wfahle.hlog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wfahle.hlog.contentprovider.QSOContactProvider;
import com.wfahle.hlog.contentprovider.QSOContactTable;
import com.wfahle.hlog.network.RHandler;
import com.wfahle.hlog.network.RadioSocket;
import com.wfahle.hlog.network.THandler;
import com.wfahle.hlog.network.TerminalSocket;
import com.wfahle.hlog.utils.Entity;
import com.wfahle.hlog.utils.GlobalDxccList;
import com.wfahle.hlog.utils.RadioUtils;
import com.wfahle.unused.QSOContact;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EntryActivity extends Activity {
	protected final static int log_request = 2; // enum of all the requests would be better
	protected final static int loggedOut = 0;
	protected final static int loggingIn = 1;
	protected final static int loggedIn = 2;

	protected String telnetServer="";
	protected int telnetPort = 23;
	protected String telnetLogon = "";
	protected String radioServer="";
	protected int radioPort = 7373;
	protected QSOContact qso=null;
	private boolean sent_shdx=false;
	private boolean abandonChanges = false;
    EditText callBox;
    EditText txfreqBox;
    EditText rxfreqBox;
    EditText modeBox;
    EditText rrstBox;
    EditText srstBox;
    TextView countryTxt;
    /* Change Mode:
     * P1  x x x 07 
     * P1 = 00 : LSB, P1 = 01 : USB, P1 = 02 : CW,
     * P1 = 03 : CWR, P1 = 04 : AM, P1 = 08 : FM,
     * P1 = 0A : DIG, P1 = 0C : PKT P1 = 88 : FMN,
     */
	protected TerminalSocket telnetsk = null;
	protected RadioSocket radiosk = null;
	protected int state=loggedOut;
	private Uri contactUri;
	protected final static String LOGIN_STRING = "wasLoggedIn";
	protected final static String DONE_STRING = "abandonChanges";
	protected final static String SCROLL_STRINGS = "formerStrings";
	private boolean wasLoggedIn=false;
	private int mInterval = 2000; // 2 seconds by default, can be changed later
	private Handler mHandler;

	boolean shdxing() {
		return sent_shdx;
	}
	
	void shdxing(boolean set) {
		sent_shdx = false; // shdx run is over
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        callBox = (EditText) findViewById(R.id.call_edit);
        txfreqBox = (EditText) findViewById(R.id.txfreq_edit);
        rxfreqBox = (EditText) findViewById(R.id.rxfreq_edit);
        modeBox = (EditText) findViewById(R.id.mode_edit);
        rrstBox = (EditText) findViewById(R.id.rrst_edit);
        srstBox = (EditText) findViewById(R.id.srst_edit);
        countryTxt = (TextView) findViewById(R.id.entity);
        qso = null;
	    mHandler = new Handler();
        
        ArrayList<SpotDetails> strarray = new ArrayList<SpotDetails>();
        if (savedInstanceState == null)
        {
        	contactUri = null;
        	wasLoggedIn = false;
        }
        else
        {
            // check from the saved Instance
            contactUri =  (Uri) savedInstanceState.getParcelable(QSOContactProvider.CONTENT_ITEM_TYPE);
            fillData(contactUri);
            wasLoggedIn = savedInstanceState.getBoolean(LOGIN_STRING);
            strarray = savedInstanceState.getParcelableArrayList(SCROLL_STRINGS); 
            if (strarray!= null)
            {
            	savedInstanceState.putParcelableArrayList(SCROLL_STRINGS, null);
            }
            else
            	strarray = new ArrayList<SpotDetails>();
        }
        
	    final SharedPreferences settings = getSharedPreferences(ConfigActivity.PREFS_NAME, 0);
		telnetServer = settings.getString(ConfigActivity.CLUSTER_SERVER, null);
		telnetPort = settings.getInt(ConfigActivity.TELNET_PORT, 23);
		telnetLogon = settings.getString(ConfigActivity.TELNET_LOGON, null);
		radioServer = settings.getString(ConfigActivity.PIGLET_ID, null);
		radioPort = settings.getInt(ConfigActivity.PIGLET_PORT, 7373);
        final ListView lv = (ListView) findViewById(R.id.spot_list);
        lv.setClickable(true);
        
        final StableArrayAdapter adapter = new StableArrayAdapter(this, R.layout.spot_row, strarray);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        		String qsoTFreq = "";
        		String qsoRFreq = "";
        		String qsoCall = "";
        		String qsoMode = "";
        		String qsoRRST = "";
        		String qsoSRST = "";

            Object o = lv.getItemAtPosition(position);
            
            String text = o.toString();
            int pos =  text.indexOf(' ');
            qsoCall = text.substring(0, pos);
            callBox.setText(qsoCall);
            Entity en = GlobalDxccList.dxcc_display(qsoCall);
            countryTxt.setText(": "+ (en == null?"":en.Country));
          
            int pos2 = text.indexOf(' ', pos+1);
            if (pos2 <=0)
            	pos2 = text.length();
            String kRFreq = text.substring(pos+1, pos2); // receive frequency in khz
            int posp = kRFreq.indexOf('.');
            String comment ="";
            if (pos2 < text.length())
            	comment = text.substring(pos2);
             
            qsoRFreq = RadioUtils.convertToMHz(kRFreq, posp);
            rxfreqBox.setText(qsoRFreq);
            qsoMode = RadioUtils.mode(qsoRFreq);
            boolean voice = qsoMode.equals("USB") || qsoMode.equals("SSB") || qsoMode.equals("LSB") ||
            		qsoMode.equals("AM") || qsoMode.equals("FM");
            String kTFreq = RadioUtils.upDown(kRFreq, comment, voice); // get tx freq from "up 1", e.g.
            qsoTFreq = RadioUtils.convertToMHz(kTFreq, posp);
            txfreqBox.setText(qsoTFreq);
            modeBox.setText(qsoMode);

            if (radiosk != null)
            {
            	radiosk.tuneRadio(qsoMode, qsoRFreq, qsoTFreq);
            }
            if (voice)
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
        	qso = new QSOContact(qsoCall, qsoRFreq, qsoTFreq, "", "", qsoMode, qsoRRST, qsoSRST, "",
        			"", "", "", "", false);
          }
        });
    }
    	
	private String keyString(Cursor cursor, String column) {
		return cursor.getString(cursor.getColumnIndexOrThrow(column));
	}
	
	private void fillData(Uri uri) {
		if (uri != null)
		{
			String[] projection = {    QSOContactTable.KEY_ID, QSOContactTable.KEY_CALL, QSOContactTable.KEY_RXFREQ, QSOContactTable.KEY_TXFREQ,
					QSOContactTable.KEY_TIMEON, QSOContactTable.KEY_TIMEOFF, QSOContactTable.KEY_MODE, QSOContactTable.KEY_RRST,
					QSOContactTable.KEY_SRST, QSOContactTable.KEY_NAME, QSOContactTable.KEY_QTH, QSOContactTable.KEY_STATE, 
					QSOContactTable.KEY_COUNTRY, QSOContactTable.KEY_GRID, QSOContactTable.KEY_COMPLETE };
		    Cursor cursor = getContentResolver().query(uri, projection, null, null,
		        null);
		    String qsoCall = "";
		    String qsoTFreq = "";
		    String qsoRFreq = "";
		    String qsoMode = "";
		    String qsoRRST = "";
		    String qsoSRST = "";
		    	
		    if (cursor != null) {
		    	cursor.moveToFirst();
		    	
		      	qsoCall = keyString(cursor, QSOContactTable.KEY_CALL);
		  		qsoTFreq = keyString(cursor, QSOContactTable.KEY_TXFREQ);
		  		qsoRFreq = keyString(cursor, QSOContactTable.KEY_RXFREQ);
		  		qsoMode = keyString(cursor, QSOContactTable.KEY_MODE);
		  		qsoRRST = keyString(cursor, QSOContactTable.KEY_RRST);
		  		qsoSRST = keyString(cursor, QSOContactTable.KEY_SRST);
		  		
		  		qso = new QSOContact(qsoCall, qsoRFreq, qsoTFreq, 
		  				keyString(cursor, QSOContactTable.KEY_TIMEON),
		  				keyString(cursor, QSOContactTable.KEY_TIMEOFF), 
						qsoMode, qsoRRST, qsoSRST,
						keyString(cursor, QSOContactTable.KEY_NAME),
						keyString(cursor, QSOContactTable.KEY_QTH),
						keyString(cursor, QSOContactTable.KEY_STATE),
						keyString(cursor, QSOContactTable.KEY_COUNTRY),
						keyString(cursor, QSOContactTable.KEY_GRID), false);
/*		  		qsoComplete = cursor.getString(cursor
				          .getColumnIndexOrThrow(QSOContactTable.KEY_COMPLETE));*/
			    callBox.setText(qsoCall);	      
			    txfreqBox.setText(qsoTFreq);
			    rxfreqBox.setText(qsoRFreq);
			    modeBox.setText(qsoMode);
			    rrstBox.setText(qsoRRST);
			    srstBox.setText(qsoSRST);
			    // always close the cursor
			    cursor.close();
		    }
	    }
	}

	@Override 
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState == null) {
        	contactUri = null;
        	wasLoggedIn = false;
        }
        else {
            // check from the saved Instance
            contactUri =  (Uri) savedInstanceState.getParcelable(QSOContactProvider.CONTENT_ITEM_TYPE);            
            fillData(contactUri);
            wasLoggedIn = savedInstanceState.getBoolean(LOGIN_STRING);
            ArrayList<SpotDetails> stateList = savedInstanceState.getParcelableArrayList(SCROLL_STRINGS); 
            if (stateList != null) {
	            final ListView lv = (ListView) findViewById(R.id.spot_list);
	            StableArrayAdapter adapter = (StableArrayAdapter)lv.getAdapter();
            	for (int i=0; i<stateList.size(); i++)
            		adapter.add(stateList.get(i)); // can't use addAll here - add is overridden
            }
        }
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    if (abandonChanges) // user hit done button or back button, just bail from this activity
	    	return;
	    outState.putParcelable(QSOContactProvider.CONTENT_ITEM_TYPE, contactUri);
	    outState.putBoolean(LOGIN_STRING, wasLoggedIn);
        final ListView lv = (ListView) findViewById(R.id.spot_list);
        StableArrayAdapter adapter = (StableArrayAdapter)lv.getAdapter();

        int len = adapter.getCount();
        ArrayList<SpotDetails> strarray = new ArrayList<SpotDetails>();
        for (int i=0; i<len; i++)
        	strarray.add(adapter.getItem(i));

        outState.putParcelableArrayList(SCROLL_STRINGS, strarray);
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    if (/*wasLoggedIn*/ true) {
	    	LogOn(); // log on.
	    	startRepeatingTask();
	    }
	}


	  Runnable mStatusChecker = new Runnable() {
	    @Override 
	    public void run() {
	      pollRig(); //this function can change value of mInterval.
	      mHandler.postDelayed(mStatusChecker, mInterval);
	    }
	  };

	  void startRepeatingTask() {
	    mStatusChecker.run(); 
	  }

	  void stopRepeatingTask() {
	    mHandler.removeCallbacks(mStatusChecker);
	  }
	  
	@Override
	protected void onPause() {
	    super.onPause();
	    stopRepeatingTask();
	    if (!abandonChanges) {
		    saveState();
		    if (state == loggedIn) {
		    	wasLoggedIn=true;
		    	LogOn(); // log off.
		    }
	    }
	}

	private void saveState() {
	    String qsoCall = callBox.getText().toString();
	    String qsoTFreq = txfreqBox.getText().toString();
	    String qsoRFreq = rxfreqBox.getText().toString();
	    String qsoMode = modeBox.getText().toString();
	    String qsoRRST = rrstBox.getText().toString();
	    String qsoSRST = srstBox.getText().toString();

	    if (qsoTFreq.length() == 0 && qsoRFreq.length() == 0 && qsoCall.length() == 0 && 
	    		qsoMode.length() == 0 && qsoRRST.length() == 0 && qsoSRST.length() == 0)
	    	return;
	    if (qso == null) {
	    	qso = new QSOContact();
	    }
		ContentValues values = new ContentValues();
		values.put(QSOContactTable.KEY_CALL, qsoCall);
		values.put(QSOContactTable.KEY_TXFREQ, qsoTFreq);
		values.put(QSOContactTable.KEY_RXFREQ, qsoRFreq);
		values.put(QSOContactTable.KEY_MODE, qsoMode);
		values.put(QSOContactTable.KEY_RRST, qsoRRST);
		values.put(QSOContactTable.KEY_SRST, qsoSRST);
		values.put(QSOContactTable.KEY_TIMEON, qso.getTimeon());
		values.put(QSOContactTable.KEY_TIMEOFF, qso.getTimeoff());
		values.put(QSOContactTable.KEY_NAME, qso.getName());
		values.put(QSOContactTable.KEY_QTH, qso.getQTH());
		values.put(QSOContactTable.KEY_STATE, qso.getState());
		values.put(QSOContactTable.KEY_COUNTRY, qso.getCountry());
		values.put(QSOContactTable.KEY_GRID, qso.getGrid());
		values.put(QSOContactTable.KEY_COMPLETE, qso.getComplete());
		
		if (contactUri == null) {
		      // New qso
		      contactUri = getContentResolver().insert(QSOContactProvider.CONTENT_URI, values);
		} else {
		      // Update qso
		      getContentResolver().update(contactUri, values, null, null);
		}
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      switch(requestCode) {
      	case (log_request) : {
            if (resultCode == Activity.RESULT_OK) {
            	newContact();
            }
    	  break;
      	}
      }
    }

    public void pollRig()
    {
        if (radiosk != null)
        {
        	radiosk.pollRig();
        }
    }
    
    public void rigRXFreq(String rx)
    {
        EditText rxfreqBox = (EditText) findViewById(R.id.rxfreq_edit);
    	String qsoRFreq = rx;
        rxfreqBox.setText(qsoRFreq);    	
    }
    
    public void rigMode(String mode)
    {
        EditText modeBox = (EditText) findViewById(R.id.mode_edit);
    	String qsoMode = mode;
        modeBox.setText(qsoMode);
        if (radiosk != null)
        	radiosk.setPoll(false);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
    }

    public class StableArrayAdapter extends ArrayAdapter<SpotDetails> {

    	protected int curId = 0;
    	private LayoutInflater l_Inflater;

        HashMap<SpotDetails, Integer> mIdMap = new HashMap<SpotDetails, Integer>();
    	
        public StableArrayAdapter(Context context, int textViewResourceId,
            List<SpotDetails> objects) {
        	super(context, textViewResourceId, objects);
  		  	l_Inflater = LayoutInflater.from(context);

	  		for (int i = 0; i < objects.size(); ++i) {
        	  	SpotDetails sd = objects.get(i);
        	  	mIdMap.put(sd, i);
        	  	curId = i;
          	}
        }

        @Override
        public long getItemId(int position) {
          SpotDetails item = getItem(position);
          return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
          return true;
        }
        
        public void addItem(SpotDetails item) {
        	super.add(item);
        	curId++;
        	mIdMap.put(item,  curId);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
    		ViewHolder holder;
    		if (convertView == null) {
    			convertView = l_Inflater.inflate(R.layout.spot_row, null);
    			holder = new ViewHolder();
    			holder.txt_itemName = (TextView) convertView.findViewById(R.id.spot_call);
    			holder.txt_itemDescription = (TextView) convertView.findViewById(R.id.dx_message);
    			holder.txt_itemPrice = (TextView) convertView.findViewById(R.id.spot_rxfreq);
    			holder.itemImage = (ImageView) convertView.findViewById(R.id.spot_flag);

    			convertView.setTag(holder);
    		} else {
    			holder = (ViewHolder) convertView.getTag();
    		}
    		holder.txt_itemName.setText(getItem(position).getCall());
    		holder.txt_itemDescription.setText(getItem(position).getItemDescription());
    		holder.txt_itemPrice.setText(getItem(position).getFrequency());
    		holder.itemImage.setImageResource(getItem(position).getImageNumber());

    		return convertView;
    	}

    }
	static class ViewHolder {
		TextView txt_itemName;
		TextView txt_itemDescription;
		TextView txt_itemPrice;
		ImageView itemImage;
	}
    
    public void newContact() {
    	callBox.setText("");
	    txfreqBox.setText("");
	    rxfreqBox.setText("");
	    modeBox.setText("");
	    rrstBox.setText("");
	    srstBox.setText("");
	    contactUri = null;
	    qso = null;
    }

    public void shdx(View v) {
    	if (telnetsk != null && state == loggedIn) {
    		sent_shdx=true;
    		String shdxstr = "sh/dx\r\n";
			telnetsk.SpecialSocketSend(shdxstr.getBytes());
    	}
    }

    public void submitCall() {
    	if (telnetsk != null) {
			telnetsk.SpecialSocketSend(telnetsk.getLogon().getBytes());
			state = loggedIn;
    	}
    }
    
	public void LogOn() { /*done will log out */	
		if (state == loggingIn)
		{
			return;
		}
		else if (state == loggedIn) {
			if(telnetsk != null) {
				String quit = "q\r\n";
				telnetsk.SpecialSocketSend(quit.getBytes());
				Button button = (Button) findViewById(R.id.dxcStart);
				button.setText(R.string.login_ui);
				telnetsk.SocketStop();
				telnetsk=null;
			}
			if (radiosk != null)
			{
				radiosk.readStatus();
				radiosk.SocketStop();
			}
			state = loggedOut;
			radiosk = null;
		}
		else
		{
			if (telnetsk == null)
			{
			    final ListView lv = (ListView) findViewById(R.id.spot_list);
			    final TextView tv = (TextView) findViewById(R.id.dxc_out);
				final THandler hnd = new THandler(this, lv, tv);
				final RHandler rhnd = new RHandler(this, lv, tv);
				telnetsk = new TerminalSocket(hnd);
				radiosk = new RadioSocket(rhnd);
			}
			telnetsk.setServer(telnetServer);
			telnetsk.setPort(telnetPort);
			telnetsk.setLogon(telnetLogon+"\r\n"); 
			telnetsk.SocketStart();
			if (radiosk != null)
			{
				radiosk.setServer(radioServer);
				radiosk.setPort(radioPort);
				radiosk.SocketStart();
			}
			state = loggingIn; 
			
		}
	}
	
	private void abandon(int resultCode) {
		if (state == loggedIn || state == loggingIn) {
			state = loggedIn; // skip ahead to logged in state; it will log out.
			LogOn(); // log off
		}
		wasLoggedIn=false;
		abandonChanges = true; // state was deliberately cancelled by back or done button
		Intent resultIntent = new Intent();
		// return current contact - may need to be deleted.
		resultIntent.putExtra(QSOContactProvider.CONTENT_ITEM_TYPE, contactUri);
    	setResult(resultCode, resultIntent);
	}
	
    @Override
    public void onBackPressed() {
    	abandon(Activity.RESULT_OK);
    	super.onBackPressed(); // just calls finish()?
    }
	
	public void socketError(int id) {
		// if it's an error, don't bother sending any more, just clean up
		if (id == 1) // radio socket
		{
			if (radiosk != null)
				radiosk.SocketStop();
			radiosk = null;
		}
		else if (id == 2) { // telnet socket
			if (telnetsk != null)
				telnetsk.SocketStop();
			telnetsk = null;
			state = loggedOut;
		}
	}
	
	public void onFilter(View view) {
		if (state == loggedIn) {
			//TODO: send configured filter info
		}
	}
	
	public void done(View view) {
		abandon(Activity.RESULT_OK);
    	finish();
	}
	
    public void logContact(View view) {
    	saveState();
    	Intent intent = new Intent(this, LogActivity.class);
	    intent.putExtra(QSOContactProvider.CONTENT_ITEM_TYPE, contactUri);

    	startActivityForResult(intent, log_request);
    }
    
    
}
