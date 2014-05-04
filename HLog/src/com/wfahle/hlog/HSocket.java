package com.wfahle.hlog;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.wfahle.hlog.EntryActivity.StableArrayAdapter;
class SHandler extends Handler {
	EntryActivity main;
	ListView lv;
	TextView txt;

	public static final int mnormal = 0;
	public static final int merror = 1;
	public static final int minfo = 2;
	
	SHandler(EntryActivity entryActivity, ListView listView, TextView txt) {
		super();
		this.main = entryActivity;
		lv = listView;
		this.txt = txt;
	}
	public void handleMessage(android.os.Message msg) {
		Bundle hm = msg.getData(); 
		String msgText = hm.getString("message");
		System.out.println(msgText);

		if (msg.what == merror) {
			txt.setText(msgText);
			txt.setTextColor(0xFFFF0000);
			txt.setVisibility(View.VISIBLE);
			main.socketError(hm.getInt("id"));
		}
		else if(msg.what == minfo) {
			txt.setText(msgText);
			txt.setTextColor(0xFF00FF00);
			txt.setVisibility(View.VISIBLE);			
		}

	}

}

public class HSocket {
	protected volatile boolean bRunning=false;
	public Thread tr;
	protected Socket sk = null;
	protected InputStream iStream = null;
	protected OutputStream oStream = null;
	private int id=0;
	
	HSocket(int i)
	{
		id = i;
	}
	
	protected Message StringToMessage(String s) {
		Message msg = new Message();
		msg.what = SHandler.mnormal;
		Bundle hm = new Bundle();
		hm.putString("message", s);
		msg.setData(hm);
		return msg;
	}
	
	protected Message ErrorToMessage(String s) {
		Message msg = new Message();
		msg.what = SHandler.merror;
		Bundle hm = new Bundle();
		hm.putInt("id", id);
		hm.putString("message", s);		
		msg.setData(hm);
		return msg;
	}
	
	protected Message InfoToMessage(String s) {
		Message msg = new Message();
		msg.what = SHandler.minfo;
		Bundle hm = new Bundle();
		hm.putString("message", s);
		msg.setData(hm);
		return msg;
	}
	
	protected Message SpecialToMessage(String s, int what) {
		Message msg = new Message();
		msg.what = what;
		Bundle hm = new Bundle();
		hm.putString("message", s);
		msg.setData(hm);
		return msg;
	}	
}
