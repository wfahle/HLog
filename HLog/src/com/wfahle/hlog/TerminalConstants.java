package com.wfahle.hlog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.wfahle.hlog.EntryActivity.StableArrayAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public final class TerminalConstants {

	final static public byte CMD_IAC = (byte) 255; /* interpret as command: */
	final static public byte CMD_DONT = (byte) 254; /* you are not to use option */
	final static public byte CMD_DO = (byte) 253; /* please, you use option */
	final static public byte CMD_WONT = (byte) 252; /* I won't use option */
	final static public byte CMD_WILL = (byte) 251; /* I will use option */
	final static public byte CMD_SB = (byte) 250; /*
													 * interpret as
													 * subnegotiation
													 */
	final static public byte CMD_SE = (byte) 240; /* end sub negotiation */
	final static public byte CMD_GA = (byte) 249; /* you may reverse the line */
	final static public byte CMD_EL = (byte) 248; /* erase the current line */
	final static public byte CMD_EC = (byte) 247; /*
													 * erase the current
													 * character
													 */
	final static public byte CMD_AYT = (byte) 246; /* are you there */
	final static public byte CMD_AO = (byte) 245; /*
													 * abort output--but let
													 * prog finish
													 */
	final static public byte CMD_IP = (byte) 244; /*
													 * interrupt
													 * process--permanently
													 */
	final static public byte CMD_BREAK = (byte) 243; /* break */
	final static public byte CMD_DM = (byte) 242; /*
													 * data mark--for connect.
													 * cleaning
													 */
	final static public byte CMD_NOP = (byte) 241; /* nop */
	final static public byte CMD_EOR = (byte) 239; /*
													 * end of record
													 * (transparent mode)
													 */
	final static public byte CMD_ABORT = (byte) 238; /* Abort process */
	final static public byte CMD_SUSP = (byte) 237; /* Suspend process */
	final static public byte CMD_EOF = (byte) 236;

	final static public byte TS_MAIN = 0;
	final static public byte TS_IAC = 1;
	final static public byte TS_OPTIONS = 2;
	final static public byte TS_SUB = 3;

	final static public byte OP_BINARY = 0; /* 8-bit data path */
	final static public byte OP_ECHO = 1; /* echo */
	final static public byte OP_RCP = 2; /* prepare to reconnect */
	final static public byte OP_SGA = 3; /* suppress go ahead */
	final static public byte OP_NAMS = 4; /* approximate message size */
	final static public byte OP_STATUS = 5; /* give status */
	final static public byte OP_TM = 6; /* timing mark */
	final static public byte OP_RCTE = 7; /*
											 * remote controlled transmission
											 * and echo
											 */
	final static public byte OP_NAOL = 8; /* negotiate about output line width */
	final static public byte OP_NAOP = 9; /* negotiate about output page size */
	final static public byte OP_NAOCRD = 10; /*
												 * negotiate about CR
												 * disposition
												 */
	final static public byte OP_NAOHTS = 11; /*
												 * negotiate about horizontal
												 * tabstops
												 */
	final static public byte OP_NAOHTD = 12; /*
												 * negotiate about horizontal
												 * tab disposition
												 */
	final static public byte OP_NAOFFD = 13; /*
												 * negotiate about formfeed
												 * disposition
												 */
	final static public byte OP_NAOVTS = 14; /*
												 * negotiate about vertical tab
												 * stops
												 */
	final static public byte OP_NAOVTD = 15; /*
												 * negotiate about vertical tab
												 * disposition
												 */
	final static public byte OP_NAOLFD = 16; /*
												 * negotiate about output LF
												 * disposition
												 */
	final static public byte OP_XASCII = 17; /* extended ascic character set */
	final static public byte OP_LOGOUT = 18; /* force logout */
	final static public byte OP_BM = 19; /* byte macro */
	final static public byte OP_DET = 20; /* data entry terminal */
	final static public byte OP_SUPDUP = 21; /* supdup protocol */
	final static public byte OP_SUPDUPOUTPUT = 22; /* supdup output */
	final static public byte OP_SNDLOC = 23; /* send location */
	final static public byte OP_TYPE = 24; /* terminal type */
	final static public byte OP_EOR = 25; /* end or record */
	final static public byte OP_TUID = 26; /* TACACS user identification */
	final static public byte OP_OUTMRK = 27; /* output marking */
	final static public byte OP_TTYLOC = 28; /* terminal location number */
	final static public byte OP_REGIME3270 = 29; /* 3270 regime */
	final static public byte OP_X3PAD = 30; /* X.3 PAD */
	final static public byte OP_NAWS = 31; /* window size */
	final static public byte OP_TSPEED = 32; /* terminal speed */
	final static public byte OP_LFLOW = 33; /* remote flow control */
	final static public byte OP_LINEMODE = 34; /* Linemode option */
	final static public byte OP_XDISPLOC = 35; /* X Display Location */
	final static public byte OP_OLD_ENVIRON = 36; /*
													 * Old - Environment
													 * variables
													 */
	final static public byte OP_AUTHENTICATION = 37; /* Authenticate */
	final static public byte OP_ENCRYPT = 38; /* Encryption option */
	final static public byte OP_NEW_ENVIRON = 39; /*
													 * New - Environment
													 * variables
													 */
	final static public byte OP_TN3270E = 40; /* TN3270 enhancements */
	final static public byte OP_XAUTH = 41;
	final static public byte OP_CHARSET = 42; /* Character set */
	final static public byte OP_RSP = 43; /* Remote serial port */
	final static public byte OP_COM_PORT_OPTION = 44; /* Com port control */
	final static public byte OP_SLE = 45; /* Suppress local echo */
	final static public byte OP_STARTTLS = 46; /* Start TLS */
	final static public byte OP_KERMIT = 47; /*
												 * Automatic Kermit file
												 * transfer
												 */
	final static public byte OP_SEND_URL = 48;
	final static public byte OP_FORWARD_X = 49;
	final static public byte OP_PRAGMA_LOGON = (byte) 138;
	final static public byte OP_SPI_LOGON = (byte) 139;
	final static public byte OP_RAGMA_HEARTBEAT = (byte) 140;
	final static public byte OP_EXOPL = (byte) 255; /* extended-options-list */

}
class MyHandler extends Handler {
	EntryActivity main;
	ListView lv;
	TextView txt;

	MyHandler(EntryActivity entryActivity, ListView listView, TextView txt) {
		super();
		this.main = entryActivity;
		lv = listView;
		this.txt = txt;
	}

	protected boolean whiteSpace(char txt) {
		if (txt == ' ' || txt == '\t' || txt == '\r' || txt == '\n')
			return true;
		return false;
	}
	public void handleMessage(android.os.Message msg) {
		Bundle hm = msg.getData(); // TODO put error messages out to the screen
		String msgText = hm.getString("message");
		System.out.println(msgText);
		if(msg.what == 0)
		{
			final StableArrayAdapter adapter = (StableArrayAdapter)lv.getAdapter();
			if (msgText.indexOf("DX de ")==0) // starts dx
			{
				int pos = msgText.indexOf(':'); // find "DX de W3WW:" location
				int posf = pos+1;
				while (posf < msgText.length() && whiteSpace(msgText.charAt(posf)))
					posf++;
				int endf = msgText.indexOf(' ', posf); // find end of freq
				int poscall = endf;
				while (poscall < msgText.length() && whiteSpace(msgText.charAt(poscall)))
					poscall++;
				int endcall = msgText.indexOf(' ', poscall);
				int posmsg = endcall;
				while (posmsg < msgText.length() && whiteSpace(msgText.charAt(posmsg)))
					posmsg++;
				int endmsg = posmsg;
				while (endmsg < msgText.length() && !whiteSpace(msgText.charAt(endmsg))) {
					endmsg++;
					if (endmsg < msgText.length() && whiteSpace(msgText.charAt(endmsg)))
						endmsg++; // stop on multiple whitespace.
				}
				String item = msgText.substring(poscall, endcall) + " " +
						msgText.substring(posf, endf) +  " " +
						msgText.substring(posmsg, endmsg);
				adapter.addItem(item); // strip "DX de "
		    	adapter.notifyDataSetChanged();
			}
			else if (msgText.indexOf("call:") >= 0)
			{
				main.submitCall();
			}
			else if (msgText.indexOf("login:") >= 0)
			{
				main.submitCall();
			}
		}
		else if(msg.what == 1)
		{
			txt.setText(msgText);
			txt.setTextColor(0xFFFF0000);
			txt.setVisibility(View.VISIBLE);
			
		}
		else if(msg.what == 2)
		{
			txt.setText(msgText);
			txt.setTextColor(0xFF00FF00);
			txt.setVisibility(View.VISIBLE);			
		}
	}

}


class TerminalSocket implements Runnable {
	public Thread tr;
	private MyHandler hnd;
	protected Socket sk = null;
	private InputStream iStream = null;
	public OutputStream oStream = null;
	public byte X = 1;
	public byte Y = 1;
	protected String Server = "";
	protected int Port = 0;
	protected String Logon = "";
	private boolean bRunning;
	
	public TerminalSocket(MyHandler h) {
		hnd = h;
		tr = new Thread(this);
	}
	

	public void SocketStart() {
		tr.start();
	}

	public void SocketStop() {
		bRunning = false;
		try {
			sk.close();
		} catch (IOException e) {
			hnd.sendMessage(ErrorToMessage("Socket close failed" + Server + ":" + Port));
		}
		sk = null;
	}
	
	private Message StringToMessage(String s) {
		Message msg = new Message();
		msg.what = 0;
		Bundle hm = new Bundle();
		hm.putString("message", s);
		msg.setData(hm);
		return msg;
	}
	
	private Message ErrorToMessage(String s) {
		Message msg = new Message();
		msg.what = 1;
		Bundle hm = new Bundle();
		hm.putString("message", s);		
		msg.setData(hm);
		return msg;
	}
	
	private Message InfoToMessage(String s) {
		Message msg = new Message();
		msg.what = 2;
		Bundle hm = new Bundle();
		hm.putString("message", s);
		msg.setData(hm);
		return msg;
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
			oStream.write(buff);
			oStream.flush();
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
