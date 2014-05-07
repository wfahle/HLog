package com.wfahle.hlog.network;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.wfahle.hlog.Entity;
import com.wfahle.hlog.EntryActivity;
import com.wfahle.hlog.R;
import com.wfahle.hlog.network.SHandler;
import com.wfahle.hlog.utils.GlobalDxccList;
import com.wfahle.hlog.SpotDetails;
import com.wfahle.hlog.EntryActivity.StableArrayAdapter;

public class THandler extends SHandler {

	public THandler(EntryActivity entryActivity, ListView listView, TextView txt) {
		super(entryActivity, listView, txt);
	}

	protected boolean whiteSpace(char txt) {
		if (txt == ' ' || txt == '\t' || txt == '\r' || txt == '\n')
			return true;
		return false;
	}
	
	int skipWhitespace(String msgText, int startPos){
		// skip whitespace if any
		while (startPos < msgText.length() && whiteSpace(msgText.charAt(startPos)))
			startPos++;
		return startPos;
	}

	int skipText(String msgText, int startPos){
		// skip whitespace if any
		while (startPos < msgText.length() && !whiteSpace(msgText.charAt(startPos)))
			startPos++;
		return startPos;
	}
	
	public void handleMessage(android.os.Message msg) {
		Bundle hm = msg.getData(); 
		String msgText = hm.getString("message");
		System.out.println(msgText);
		if(msg.what == mnormal) {
			final StableArrayAdapter adapter = (StableArrayAdapter)lv.getAdapter();
			if (msgText.indexOf("DX de ")==0) { // starts dx
				int pos = msgText.indexOf(':'); // find "DX de W3WW:" location
				int posf = skipWhitespace(msgText, pos+1);
				int endf = skipText(msgText, posf); // find end of freq
				int poscall = skipWhitespace(msgText, endf);
				
				int endcall = skipText(msgText, poscall);
				int posmsg = skipWhitespace(msgText, endcall);
				int endmsg = posmsg;
				while (endmsg < msgText.length() && !whiteSpace(msgText.charAt(endmsg))) {
					endmsg++;
					if (endmsg < msgText.length() && whiteSpace(msgText.charAt(endmsg)))
						endmsg++; // stop on multiple whitespace.
				}
				String call = msgText.substring(poscall, endcall);
				Entity en =GlobalDxccList.dxcc_display(call);
				int image = R.drawable.zz;
				if (en != null)
					image = en.Image;
				SpotDetails sd = new SpotDetails(call, 
						msgText.substring(posf, endf), msgText.substring(posmsg, endmsg),
						image);
				adapter.addItem(sd); // strip "DX de "
		    	adapter.notifyDataSetChanged();
			}
			else if (msgText.indexOf("call:") >= 0) {
				main.submitCall();
			}
			else if (msgText.indexOf("login:") >= 0) {
				main.submitCall();
			}
			else {
				//" 28020.1  6W1SR       24-Apr-2014 1951Z  Heard in MD               <W3LPL>\n"
				int pos = msgText.indexOf('.');
				if (pos != -1 && pos+22 < msgText.length() 
					&& msgText.charAt(pos+18) == '-' && msgText.charAt(pos+22)== '-') {
					String[] msgs = TextUtils.split(msgText, "\n");
					for (int i=0; i<msgs.length; i++) {
						msgText = msgs[i];
						pos = msgText.indexOf('.');
						if (pos != -1 && pos+22 < msgText.length() 
								&& msgText.charAt(pos+18) == '-' && msgText.charAt(pos+22)== '-') {
							int posf = skipWhitespace(msgText, 0);
							int endf = skipText(msgText, posf);
							int poscall = skipWhitespace(msgText, endf);
							int endcall = skipText(msgText, poscall);
							// skip whitespace up to date
							int posmsg = skipWhitespace(msgText, endcall);
							// skip date
							posmsg = skipText(msgText, posmsg);
							// skip whitespace up to time
							posmsg = skipWhitespace(msgText, posmsg);
							// skip time
							posmsg = skipText(msgText, posmsg);
							// skip whitespace after time
							posmsg = skipWhitespace(msgText, posmsg);
							int endmsg = posmsg;
							while (endmsg < msgText.length() && !whiteSpace(msgText.charAt(endmsg))) {
								endmsg++;
								if (endmsg < msgText.length() && whiteSpace(msgText.charAt(endmsg)))
									endmsg++; // stop on multiple whitespace.
							}
							String call = msgText.substring(poscall, endcall);
							Entity en =GlobalDxccList.dxcc_display(call);
							int image = R.drawable.zz;
							if (en != null)
								image = en.Image;
							SpotDetails sd = new SpotDetails(call, 
									msgText.substring(posf, endf), msgText.substring(posmsg, endmsg),
									image);
							adapter.addItem(sd);
						}
					}
			    	adapter.notifyDataSetChanged();		
				}
			}
		}
		else 
			super.handleMessage(msg);
	}
}

