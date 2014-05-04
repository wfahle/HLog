package com.wfahle.hlog;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.wfahle.hlog.EntryActivity.StableArrayAdapter;

class THandler extends SHandler {

	THandler(EntryActivity entryActivity, ListView listView, TextView txt) {
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
class TerminalSocket extends HSocket implements Runnable {
	private THandler hnd;
	public byte X = 1;
	public byte Y = 1;
	protected String Server = "";
	protected int Port = 0;
	protected String Logon = "";
	
	public TerminalSocket(THandler h) {
		super(2);
		hnd = h;
		tr = new Thread(this);
	}
	

	public void SocketStart() {
		tr.start();
	}

	public void SocketStop() {
		bRunning = false;
		try {
			if (sk != null)
				sk.close();
		} catch (IOException e) {
			hnd.sendMessage(ErrorToMessage("Socket close failed" + Server + ":" + Port));
		}
		sk = null;
	}
		
	public void run() {
		InetAddress ia = null;
		bRunning = true;
		hnd.sendMessage(InfoToMessage("Resolve server name " + Server));
		try {
			ia = InetAddress.getByName(Server);
		} catch (UnknownHostException ex) {
			hnd.sendMessage(ErrorToMessage("Unknown host " + Server));
			return;
		}
		hnd.sendMessage(InfoToMessage("Connect to "  + Server + ":" + Port));
		try {
			sk = new java.net.Socket(ia, Port);
		} catch (IOException ex) {
			hnd.sendMessage(ErrorToMessage("Socket time out for " + Server + ":" + Port));
			return;
		}

		try {
			iStream = sk.getInputStream();
			oStream = sk.getOutputStream();
		} catch (IOException ex) {
			hnd.sendMessage(ErrorToMessage("Protocol error for " + Server + ":" + Port));
			return;
		}

		// wont negotiate window size - was 0xff, 0xfb, 0x1f
		SpecialSocketSend(new byte[] { (byte) 0xff, (byte) 0xfc, (byte) 0x1f });
		while (bRunning) {
			byte[] buf = new byte[2048];
			try {
				int i = iStream.read(buf);
				Parsing(buf, i);
			} catch (Exception ex) {
				hnd.sendMessage(ErrorToMessage("Connection closed read"));
				return; // it's not going to open itself
			}
		}
	}

	private byte mpState = TerminalConstants.TS_MAIN;
	private byte tc = 0;
	private boolean isFirstByte = true;
	private byte bSub = 0;
	private void Parsing(byte[] bData, int count) {
		for (int i = 0; i < count; i++) {
			byte curByte = bData[i];
			switch (mpState) {
			case TerminalConstants.TS_MAIN: {
				if (curByte == TerminalConstants.CMD_IAC) {
					mpState = TerminalConstants.TS_IAC;
				} else {
					String s = new String(bData, i, count - i);
					hnd.sendMessage(StringToMessage(s));
					return;
				}
				break;
			}
			case TerminalConstants.TS_IAC: {
				CommandParser(curByte);
				break;
			}
			case TerminalConstants.TS_OPTIONS: {
				OptionsParses(curByte);
				break;
			}
			case TerminalConstants.TS_SUB: {
				SubParser(curByte);
				break;
			}

			}
		}
	}

	private void OptionsParses(byte opt) {

		if (opt == TerminalConstants.OP_SGA) {
			if (tc == TerminalConstants.CMD_DO) {
				try {
					oStream.write(new byte[] { TerminalConstants.CMD_IAC,
							TerminalConstants.CMD_WILL, opt });
				} catch (Exception ex) {
				}
				mpState = TerminalConstants.TS_MAIN;
				return;
			}
			if (tc == TerminalConstants.CMD_WILL) {
				try {
					oStream.write(new byte[] { TerminalConstants.CMD_IAC,
							TerminalConstants.CMD_DO, opt });
				} catch (Exception ex) {
				}
				mpState = TerminalConstants.TS_MAIN;
				return;
			}
		}
		if (opt == TerminalConstants.OP_ECHO) {
			if (tc == TerminalConstants.CMD_DO) {
				SpecialSocketSend(new byte[] { TerminalConstants.CMD_IAC,
						TerminalConstants.CMD_WILL, opt });
				mpState = TerminalConstants.TS_MAIN;
				return;
			}
			if (tc == TerminalConstants.CMD_WILL) {
				SpecialSocketSend(new byte[] { TerminalConstants.CMD_IAC,
						TerminalConstants.CMD_DO, opt });
				mpState = TerminalConstants.TS_MAIN;
				return;
			}
		}

		if (tc == TerminalConstants.CMD_DO) {
			if (opt == TerminalConstants.OP_TYPE) {
				SpecialSocketSend(new byte[] { TerminalConstants.CMD_IAC,
						TerminalConstants.CMD_WONT, opt }); // was CMD_WILL
				mpState = TerminalConstants.TS_MAIN;
				return;
			}
			if (opt == TerminalConstants.OP_NAWS) {
				SpecialSocketSend(new byte[] { TerminalConstants.CMD_IAC,
						TerminalConstants.CMD_WONT, opt }); // was stuff below
				// negotiate window size
//				SpecialSocketSend(new byte[] { (byte) 0xff, (byte) 0xfa,
//						(byte) 0x1f, (byte) 0x00, X, (byte) 0x00, Y,
//						(byte) 0xff, (byte) 0xf0 });
				mpState = TerminalConstants.TS_MAIN;
				return;
			}
		}
		if (tc == TerminalConstants.CMD_DO || tc == TerminalConstants.CMD_DONT)
			SpecialSocketSend(new byte[] { TerminalConstants.CMD_IAC,
					TerminalConstants.CMD_WONT, opt });
		else
			SpecialSocketSend(new byte[] { TerminalConstants.CMD_IAC,
					TerminalConstants.CMD_DONT, (byte) opt });

		mpState = TerminalConstants.TS_MAIN;
	}

	protected void SpecialSocketSend(byte[] buff) {
		try {
			if (oStream != null) {
				oStream.write(buff);
				oStream.flush();
			}
		} catch (Exception ex) {
			hnd.sendMessage(ErrorToMessage("Connection closed write"));
		}
	}

	private void CommandParser(byte cmd) {
		switch (cmd) {
		case TerminalConstants.CMD_DO:
		case TerminalConstants.CMD_DONT:
		case TerminalConstants.CMD_WILL:
		case TerminalConstants.CMD_WONT:
			tc = cmd;
			mpState = TerminalConstants.TS_OPTIONS;
			break;
		case TerminalConstants.CMD_SB:
			mpState = TerminalConstants.TS_SUB;
			break;
		case TerminalConstants.CMD_SE:
			ProcessSE();
			break;
		case TerminalConstants.CMD_IAC:
			mpState = TerminalConstants.TS_MAIN;
			break;
		default:
			mpState = TerminalConstants.TS_MAIN;
			break;
		}
	}

	private void ProcessSE() {
		if (bSub == 24) {
			try {
//				oStream.write(new byte[] { (byte) 0xff, (byte) 0xfa, 0x18,
//						0x00, 0x56, 0x54, 0x31, 0x30, 0x30, (byte) 0xff,
//						(byte) 0xf0 });
			} catch (Exception ex) {
			}

		}
		mpState = TerminalConstants.TS_MAIN;
	}

	private void SubParser(byte opt) {
		if (opt == TerminalConstants.CMD_IAC) {
			mpState = TerminalConstants.TS_IAC;
			return;
		}
		if (isFirstByte) {
			bSub = opt;
			isFirstByte = false;
		} else {
			isFirstByte = true;
		}
	}
}
