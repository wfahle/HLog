package com.wfahle.hlog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wfahle.hlog.contentprovider.QSOContactProvider;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    EditText callBox;
    EditText txfreqBox;
    EditText rxfreqBox;
    EditText modeBox;
    EditText rrstBox;
    EditText srstBox;
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
	protected final static String SCROLL_STRINGS = "formerStrings";
	private boolean wasLoggedIn=false;

	protected String mode(String freq)
	{
		String ret = "";
		double f = Double.parseDouble(freq);
		if (f > 1.8 && f < 1.843 )
			ret = "CW";
		else if (f >= 1.843 && f < 2.0 )
			ret = "LSB";
		else if (f >= 3.5 && f < 3.57)
			ret = "CW";
		else if (f >= 3.57 && f < 3.6)
			ret = "RTTY";
		else if (f >= 3.6 && f < 3.845)
			ret = "LSB";
		else if (f >= 3.845 && f <= 3.88)
			ret = "SSTV";
		else if (f>3.88 && f < 3.9)
			ret = "AM";
		else if (f > 3.9 && f < 4.0)
			ret = "LSB";
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
			ret = "LSB";
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
			ret = "USB";
		else if (f >= 18.068 && f < 18.11)
			ret = "CW";
		else if (f >= 18.11 && f <= 18.168)
			ret = "USB";
		else if (f >= 21.0 && f < 21.07)
			ret = "CW";
		else if (f >= 21.07 && f < 21.11)
			ret = "RTTY";
		else if (f >= 21.11 && f < 21.2)
			ret = "CW";
		else if (f >= 21.2 && f <= 21.45)
			ret = "USB";
		else if (f >= 24.890 && f < 24.93)
			ret = "CW";
		else if (f >= 24.93 && f <= 24.99)
			ret = "USB";
		else if (f >= 28.0 && f <= 28.3)
			ret = "CW";
		else if (f >= 28.3 && f < 29.0)
			ret = "USB";
		else if (f >= 29.0 && f < 29.2)
			ret = "AM";
		else if (f >= 29.2 && f < 29.7)
			ret = "FM";
		else if (f >= 50.0 && f < 50.1)
			ret = "CW";
		else if (f >= 50.1 && f < 52.0)
			ret = "USB";
		else if (f >= 52.0 && f <= 54.0)
			ret = "FM";
		return ret;
	}
	
	protected int parseanInt(String num) {
		// apparently Java doesn't like parseInt("1.0");
		// assume input starts with a number or space
		int posp = num.indexOf(".");
		if (posp == -1)
			posp = num.length();
		return Integer.parseInt(num.substring(0, posp));
	}
	
	protected String upDown(String rfreq, String comment, boolean ssb) {
		String ret = rfreq;
		
		comment = comment.toLowerCase(Locale.US);
        // Handles: QSX 3.838, QSX 4, UP 5, DOWN 2, U 5, D4, U4, DN4, UP4, DOWN4, QSX7144, u1.8, etc.
        int posp = rfreq.indexOf('.');
        int hz = 0;
        int khz = 0;
        if (posp < 0)
            khz = parseanInt(rfreq);
        else
        {
            khz = parseanInt(rfreq.substring(0, posp));
        	hz = parseanInt(rfreq.substring(posp+1));
        }
        int adjust = 0; // adjust tx up or down accordingly
        int hadjust = 0;
        if (comment.matches(".*up* *[1-9][0-9]*\\.*[0-9]*.*")) // matches "people, up 10", "u1.8", "u 1", etc.
        {
        	 Pattern p = Pattern.compile("up* *([1-9][0-9]*)\\.([0-9])"); // only matches x.y
        	 Matcher m = p.matcher(comment);
        	 if (m.find()) { // dot found for sure, like up 1.9
        	     String num = m.group(1); // Access a submatch group; String can't do this.
        	     adjust = parseanInt(num);
        	     String rem = m.group(2);
        	     hadjust = parseanInt(rem);
        	 }
        	 else { // no dot
	        	 p = Pattern.compile("up* *([1-9][0-9]*)");
	        	 m = p.matcher(comment);
	        	 if (m.find()) { // Find each match in turn; String can't do this.
	        	     String num = m.group(1); 
	        	     adjust = parseanInt(num);
	        	 }
        	 }
        }
        else if (comment.matches(".*[1-9][0-9]* *up")) { // matches "worked 5 up", "5-10 up" "2up" etc
			Pattern p = Pattern.compile("([1-9][0-9]*) *up");
			Matcher m = p.matcher(comment);
			if (m.find()) { // Find each match in turn; String can't do this.
			     String num = m.group(1); 
			     adjust = parseanInt(num);
			}
        }
        else if (comment.matches(".* u") || comment.contains("up") || comment.matches("u .*") ||
        		comment.matches(".* u .*") || comment.equals("u")) {
        	adjust = ssb?5:1;
        }
        else if (comment.matches(".*d[n:own]* *[1-9][0-9]*\\.*[0-9]*.*")) { // matches "people, dn 10, ok?", "d10", "d 1", "down1" etc.
        	Pattern p = Pattern.compile("d[n:own]* *([1-9][0-9]*)\\.([0-9])");
        	Matcher m = p.matcher(comment);
        	if (m.find()) { // Find each match in turn; String can't do this.
        		String num = m.group(1); // Access a submatch group; String can't do this.
        		adjust = -parseanInt(num);
        		String rem = m.group(2);
        		int thz = parseanInt(rem); // assumption - one digit
        		if (thz != 0)
        		{
        			adjust -= 1; // borrow
        			thz = 10 - thz;
        		}
        	}
        	else {
	        	p = Pattern.compile("d[n:own]* *([1-9][0-9]*)");
	        	m = p.matcher(comment);
	        	if (m.find()) { // Find each match in turn; String can't do this.
	        		String num = m.group(1); // Access a submatch group; String can't do this.
	        		adjust = -parseanInt(num);
	        	}
        	}
        }
        else if (comment.contains("dn") || comment.contains("down") || comment.matches(".* d")
        		|| comment.matches("d .*") || comment.equals("d"))
        {
        	adjust = ssb?-5:-1;
        }
        else if (comment.contains("qsx"))
        {
        	Pattern p = Pattern.compile("qsx *([1-9][0-9]*)\\.([0-9]+)");
        	Matcher m = p.matcher(comment);
        	if (m.find()) { // Find each match in turn; String can't do this.
        		String num = m.group(1); // Access a submatch group; String can't do this.
        		String rem = m.group(2);
        		int freqp = parseanInt(num);
        		if (freqp <= 100) { // it's something like 3.182
        			int tkhz = freqp*1000+parseanInt(rem);
        			adjust = tkhz - khz;
        		}
        		else {
        			rem = rem.substring(0,1); // just first sig digit
        			int thz = parseanInt(rem);
        			adjust = freqp - khz; // adjust back to the qsx frequency
        			hadjust = thz - hz;
        		}
        	}
        	else {
            	p = Pattern.compile("qsx *([1-9][0-9]*)");
            	m = p.matcher(comment);
	        	if (m.find()) { // Find each match in turn; String can't do this.
	        		String num = m.group(1); // Access a submatch group; String can't do this.
	        		int freqp = parseanInt(num);
	        		if (freqp <= 1000)
	        			adjust = freqp;
	        		else
	        			adjust = freqp - khz; // adjust back to the qsx frequency
	        	}
        	}
        }
        if (adjust != 0 || hadjust != 0) {
        	khz = khz + adjust;
        	hz = hz + hadjust;
        	if (hz < 0) {
        		khz--; // borrow
        		hz+=10;
        		if (hz < 0) // wth?
        			hz = 0; 
        	}
        	else if (hz > 10) {
        		khz++;
        		hz-=10;
        		if (hz > 10) { // still?
        			hz = 0;
        		}
        	}
        	ret = Integer.toString(khz);
        	if (hz != 0 || posp > 0) {
        		ret = ret + "." + Integer.toString(hz);
        	}
        }		
		return ret;
	}
	
	void sendAndWait(byte[] cmd)
	{
		try {
			radiosk.SpecialSocketSend(cmd);
			Thread.sleep(150);
		} catch (InterruptedException e) {
		}
	}
	
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
        qso = null;

        String[] strarray = null;
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
            if (savedInstanceState.getString(SCROLL_STRINGS, null) != null)
            {
            	strarray = TextUtils.split(savedInstanceState.getString(SCROLL_STRINGS, null), "\n");
            	savedInstanceState.putString(SCROLL_STRINGS, null);
            }
        }

        
        
        LocalDBHandler ldb = new LocalDBHandler(getBaseContext());
        List<TelnetConfig> tlist = ldb.getAllConfigs();
        if (!tlist.isEmpty())
        {
        	for (int i=0; i<tlist.size(); i++)
        	{
	        	TelnetConfig cfg = tlist.get(i);
	        	telnetServer = cfg.getServer();
	        	telnetPort = cfg.getPort();
	        	telnetLogon = cfg.getCall();
	        	radioServer =  cfg.getRadioServer();
	        	radioPort = cfg.getRadioPort();
	        	if (cfg.getPreferred())
	        		break;
        	}
        }

        final ListView lv = (ListView) findViewById(R.id.spot_list);
        lv.setClickable(true);
        final ArrayList<String> list = new ArrayList<String>();
        if (strarray != null) {
        	for (int i=0; i<strarray.length; i++)
        		list.add(strarray[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this, R.layout.spotview, list);
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
            int pos2 = text.indexOf(' ', pos+1);
            if (pos2 <=0)
            	pos2 = text.length();
            String kRFreq = text.substring(pos+1, pos2); // receive frequency in khz
            int posp = kRFreq.indexOf('.');
            String comment ="";
            if (pos2 < text.length())
            	comment = text.substring(pos2);
             
            qsoRFreq = convertToMHz(kRFreq, posp);
            rxfreqBox.setText(qsoRFreq);
            qsoMode = mode(qsoRFreq);
            boolean voice = qsoMode.equals("USB") || qsoMode.equals("SSB") || qsoMode.equals("LSB") ||
            		qsoMode.equals("AM") || qsoMode.equals("FM");
            String kTFreq = upDown(kRFreq, comment, voice); // get tx freq from "up 1", e.g.
            qsoTFreq = convertToMHz(kTFreq, posp);
            txfreqBox.setText(qsoTFreq);
            modeBox.setText(qsoMode);

            if (radiosk != null)
            {
            	byte cmd[] = new byte[5];
				int md = 2; // cw
				if (qsoMode.equals("USB"))
					md = 1;
				else if (qsoMode.equals("LSB"))
					md = 0;
				else if (qsoMode.equals("AM"))
					md = 4;
				else if (qsoMode.equals("FM"))
					md = 8;
				cmd[0] = (byte)md;
				cmd[1] = 0;
				cmd[2] = 0;
				cmd[3] = 0;
				cmd[4] = 7; // 
				sendAndWait(cmd);
            	if (!qsoTFreq.equals(qsoRFreq))
            	{
	    				cmd[0] = 0;
	    				cmd[1] = 0;
	    				cmd[2] = 0;
	    				cmd[3] = 0;
	    				cmd[4] = (byte)0x81; // vfo a/b
	    				sendAndWait(cmd);
	    				cmd[0]=(byte)md;
	    				cmd[4]=7;
	    				sendAndWait(cmd);
	            		byte frecmd[] = getBCD(qsoTFreq, (byte)1);
	            		sendAndWait(frecmd);
	            		cmd[4] = (byte)0x81; // vfo a/b
	            		sendAndWait(cmd);
	    				cmd[4] = (byte)2; // split on
	    				sendAndWait(cmd);
            	}
            	else
            	{
	    				cmd[0] = 0;
	    				cmd[1] = 0;
	    				cmd[2] = 0;
	    				cmd[3] = 0;
	    				cmd[4] = (byte)0x82; // split off
	    				sendAndWait(cmd);
            	}
				cmd = getBCD(qsoRFreq, (byte) 1);
				sendAndWait(cmd);
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
    
	byte[] getBCD(String freqinMhz, byte cmd)
	{
		byte[] ret = new byte[5];
		int len = freqinMhz.length();
        int posp = freqinMhz.indexOf('.');
        if (posp == -1)
        	posp = len;
        // magically convert string to bcd, put in cmd
        int mhz = parseanInt(freqinMhz.substring(0, posp));
        posp++;
        if (posp<len)
        {
        	mhz = mhz*10+freqinMhz.charAt(posp)-'0';
        	posp++;
        }
        else
        	mhz = mhz*10;
        int dig[] = {0,0,0,0};
        for (int i=0; i<dig.length && posp<len; i++)
        {
        	dig[i] = freqinMhz.charAt(posp)-'0';
        	posp++;
        }
        ret[0]= (byte)((mhz/1000)*16+(mhz%1000)/100);
        ret[1]= (byte)((mhz%100/10)*16 + mhz%10);
        ret[2] = (byte)(dig[0]*16+dig[1]);
        ret[3] = (byte)(dig[2]*16+dig[3]);
		ret[4] = cmd;
		return ret;
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
		      	qsoCall = cursor.getString(cursor
			          .getColumnIndexOrThrow(QSOContactTable.KEY_CALL));
		  		qsoTFreq = cursor.getString(cursor
				      .getColumnIndexOrThrow(QSOContactTable.KEY_TXFREQ));
		  		qsoRFreq = cursor.getString(cursor
			          .getColumnIndexOrThrow(QSOContactTable.KEY_RXFREQ));
		  		qsoMode = cursor.getString(cursor
			          .getColumnIndexOrThrow(QSOContactTable.KEY_MODE));
		  		qsoRRST = cursor.getString(cursor
			          .getColumnIndexOrThrow(QSOContactTable.KEY_RRST));
		  		qsoSRST = cursor.getString(cursor
			          .getColumnIndexOrThrow(QSOContactTable.KEY_SRST));
		  		
		  		qso = new QSOContact(qsoCall, qsoRFreq, qsoTFreq, 
		  				cursor.getString(cursor
				          .getColumnIndexOrThrow(QSOContactTable.KEY_TIMEON)),
				        cursor.getString(cursor
						  .getColumnIndexOrThrow(QSOContactTable.KEY_TIMEOFF)), 
						qsoMode, qsoRRST, qsoSRST,
						cursor.getString(cursor
						  .getColumnIndexOrThrow(QSOContactTable.KEY_NAME)),
						cursor.getString(cursor
						  .getColumnIndexOrThrow(QSOContactTable.KEY_QTH)),
						cursor.getString(cursor
						  .getColumnIndexOrThrow(QSOContactTable.KEY_STATE)),
						cursor.getString(cursor
						  .getColumnIndexOrThrow(QSOContactTable.KEY_COUNTRY)),
						cursor.getString(cursor
						  .getColumnIndexOrThrow(QSOContactTable.KEY_GRID)), false);
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
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
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
            if (savedInstanceState.getString(SCROLL_STRINGS, null) != null)
            {
	            String[] strarray = TextUtils.split(
	            		savedInstanceState.getString(SCROLL_STRINGS, null), "\n");
	            if (strarray != null)
	            {
		            final ListView lv = (ListView) findViewById(R.id.spot_list);
		            StableArrayAdapter adapter = (StableArrayAdapter)lv.getAdapter();
		            if (strarray != null) {
		            	for (int i=0; i<strarray.length; i++)
		            		adapter.add(strarray[i]); // can't use addAll here - add is overridden
		            }
	            }
            }
        }
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    saveState();
	    outState.putParcelable(QSOContactProvider.CONTENT_ITEM_TYPE, contactUri);
	    outState.putBoolean(LOGIN_STRING, wasLoggedIn);
        final ListView lv = (ListView) findViewById(R.id.spot_list);
        StableArrayAdapter adapter = (StableArrayAdapter)lv.getAdapter();

        int len = adapter.getCount();
        String[] strarray = new String[len];
        for (int i=0; i<len; i++)
        	strarray[i] = adapter.getItem(i);
        
	    String value = TextUtils.join("\n", strarray);
	    outState.putString(SCROLL_STRINGS, value);
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    if (/*wasLoggedIn*/ true) {
	    	LogOn(null); // log on.
	    }
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    saveState();
	    if (state == loggedIn)
	    {
	    	wasLoggedIn=true;
	    	LogOn(null); // log off.
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
        	radiosk.setPoll(true);
	        byte[] cmd = new byte[5];
			cmd[0] = 0;
			cmd[1] = 0;
			cmd[2] = 0;
			cmd[3] = 0;
			cmd[4] = (byte)0x3; // read freq and mode
			radiosk.SpecialSocketSend(cmd);
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
        
        public void addItem(String item) {
        	super.add(item);
        	curId++;
        	mIdMap.put(item,  curId);
        }

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
    	if (telnetsk != null) {
    		sent_shdx=true;
    		String shdxstr = "sh/dx\r\n";
			telnetsk.SpecialSocketSend(shdxstr.getBytes());
    	}
    }

    public void submitCall() {
    	if (telnetsk != null) {
			telnetsk.SpecialSocketSend(telnetsk.Logon.getBytes());
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
				byte[] cmd = {0, 0, 0, 0, (byte) 0xE7}; // read receiver status - cause read loop to unlock
				radiosk.SpecialSocketSend(cmd);
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
				final MyHandler hnd = new MyHandler(this, lv, tv);
				telnetsk = new TerminalSocket(hnd);
				radiosk = new RadioSocket(hnd);
			}
			telnetsk.Server = telnetServer;
			telnetsk.Port = telnetPort;
			telnetsk.Logon = telnetLogon+"\r\n"; 
			telnetsk.SocketStart();
			if (radiosk != null)
			{
				radiosk.Server = radioServer;
				radiosk.Port = radioPort;
				radiosk.SocketStart();
			}
			state = loggingIn; 
			
		}
	}
	
	public void done(View view) {
		if (state == loggedIn || state == loggingIn) {
			state = loggedIn; // skip ahead to logged in state; it will log out.
			LogOn(null); // log off
		}
		wasLoggedIn=false;
		Intent resultIntent = new Intent();
		// return current contact - may need to be deleted.
		resultIntent.putExtra(QSOContactProvider.CONTENT_ITEM_TYPE, contactUri);
    	setResult(Activity.RESULT_OK, resultIntent);
    	finish();
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
	
    public void logContact(View view) {
    	saveState();
    	Intent intent = new Intent(this, LogActivity.class);
    	/*
    	EditText call = (EditText) findViewById(R.id.call_edit);
    	EditText rxfreq = (EditText) findViewById(R.id.rxfreq_edit);
    	EditText txfreq = (EditText) findViewById(R.id.txfreq_edit);
    	EditText mode = (EditText) findViewById(R.id.mode_edit);
    	EditText rrst = (EditText) findViewById(R.id.rrst_edit);
    	EditText srst = (EditText) findViewById(R.id.srst_edit);
    	
    	intent.putExtra(LogActivity.QSO_CALL, call.getEditableText().toString());
    	intent.putExtra(LogActivity.QSO_RXFREQ, rxfreq.getEditableText().toString());
    	intent.putExtra(LogActivity.QSO_TXFREQ, txfreq.getEditableText().toString());
    	intent.putExtra(LogActivity.QSO_MODE, mode.getEditableText().toString());
    	intent.putExtra(LogActivity.QSO_RRST, rrst.getEditableText().toString());
    	intent.putExtra(LogActivity.QSO_SRST, srst.getEditableText().toString());
    	intent.putExtra(LogActivity.QSO_TIMEON, qsoTimeon);
    	intent.putExtra(LogActivity.QSO_TIMEOFF, qsoTimeoff);
    	intent.putExtra(LogActivity.QSO_NAME, qsoName);
    	intent.putExtra(LogActivity.QSO_QTH, qsoQTH);
    	intent.putExtra(LogActivity.QSO_STATE, qsoState);
    	intent.putExtra(LogActivity.QSO_COUNTRY, qsoCountry);
    	intent.putExtra(LogActivity.QSO_GRID, qsoGrid); */
    	
//	    Uri qsoUri = Uri.parse(QSOContactProvider.CONTENT_URI + "/" + id);
	    intent.putExtra(QSOContactProvider.CONTENT_ITEM_TYPE, contactUri);

    	startActivityForResult(intent, log_request);
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
