package com.wfahle.hlog.network;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.wfahle.hlog.EntryActivity;

public class RHandler extends SHandler {
	public static final int mrigpoll = lastmsg+1;
	public static final int mrigrx = mrigpoll+1;
	public static final int mrigmode = mrigrx+1;

	public RHandler(EntryActivity entryActivity, ListView listView, TextView txt) {
		super(entryActivity, listView, txt);
	}
	public void handleMessage(android.os.Message msg) {
		Bundle hm = msg.getData(); 
		String msgText = hm.getString("message");
		System.out.println(msgText);
		if (msg.what == mrigpoll) {
			main.pollRig();
		}
		else if (msg.what == mrigrx) {
			main.rigRXFreq(msgText);
		}
		else if (msg.what == mrigmode) {
			main.rigMode(msgText);
		}
		else 
			super.handleMessage(msg);

	}

}
