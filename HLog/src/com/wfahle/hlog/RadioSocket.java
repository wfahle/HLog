package com.wfahle.hlog;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
class RHandler extends SHandler {
	public static final int mrigpoll = 3;
	public static final int mrigrx = 4;
	public static final int mrigmode = 5;

	RHandler(EntryActivity entryActivity, ListView listView, TextView txt) {
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

class RadioSocket extends HSocket implements Runnable {
	protected String Server = "";
	protected int Port = 0;
	private RHandler hnd;
	public static final int ackOK = 0;
	public static final int ackERR = -1;
	private MySignal pollingrig;
	private int[] rigFreq=null;
	private volatile int curRigByte = 0;

	public RadioSocket(RHandler h) {
		super(1); // radio is 1, telnet is 2
		pollingrig = new MySignal();
		hnd = h;
		tr = new Thread(this);
	}
	
	public synchronized void setPoll(boolean poll)
	{
		if (!poll)
			curRigByte = 0; // it's been reset, ok to return to normal mode
		pollingrig.setHasDataToProcess(poll);
	}
			
	public void SocketStart() {
		tr.start();
	}

	public void SocketStop() {
		bRunning = false;
		if (sk != null) {
			try {
				sk.close();
			} catch (IOException e) {
				hnd.sendMessage(ErrorToMessage("Socket close failed" + Server + ":" + Port));
			}
		}
		sk = null;
	}
	/*
Name	P1	P2	P3	P4	Opcode	Notes
Read EEprom	MSB	LSB	0x00	0x00	0xBB	Returns 2 bytes, the specified address and the 'next' address. 16 bit memory addressing, unused memory returns 1 byte (for me 0xf0).
Write EEprom	MSB	LSB	byte 1	byte 2	0xBC	Writes 2 bytes to the specified address and the 'next' address. 16 bit memory addressing.
Get Radio Config	0	0	0	0	0xA7	Returns 9 bytes for the radio config, the byte values are not yet documented
Get Tx Metering	0	0	0	0	0xBD	4 nybbles are returned, in order power, vswr, alc, modulation
Factory Reset	0	0	0	0	0xBE	erases alignment data use with caution!
	 * 
Address	Name	Values
0x0068	VFO	0x80=VFO-A 0x81=VFO-B
0x008D	Split	0x00=No split, 0x80=split
*/
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

		while (bRunning) {
			try {
				byte[] buf = new byte[2048];
				try {
					int i = iStream.read(buf);
					Parsing(buf, i);
				} catch (Exception ex) {
					hnd.sendMessage(ErrorToMessage("Connection closed read"));
					return;
				}
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// don't care if it can sleep, I can't, that's why I'm up programming
			}
		}
	}

	private void Parsing(byte[] bData, int count) {
		for (int i = 0; i < count; i++) {
			byte curByte = bData[i];
			if (pollingrig.hasDataToProcess())
			{
				if (rigFreq == null) // just reuse it
				{
					rigFreq = new int[5]; // store up until we have them all
				}
				if (curRigByte >= 0)
				{
					rigFreq[curRigByte] = (curByte & 0xFF);
					curRigByte++;
					if (curRigByte > 4) // got them all
					{
						curRigByte = -1;
						int mhz = (rigFreq[0]>>>4) * 100 + (rigFreq[0]& 0xF ) * 10 + (rigFreq[1]>>>4);
						int khz = (rigFreq[1] & 0xF) * 100 + (rigFreq[2] >>> 4) * 10 + 
								(rigFreq[2] & 0xF);
						String kstring = "00"+Integer.toString(khz);
						kstring = kstring.substring(kstring.length()-3);
						String extra = "";
						if (rigFreq[3] != 0)
						{
							extra = Integer.toString(rigFreq[3]>>>4);
							if ((rigFreq[3] & 0xF) != 0)
								extra += Integer.toString(rigFreq[3] & 0xF);
						}
						String rfreq = Integer.toString(mhz)+"."+kstring+extra;
						hnd.sendMessage(SpecialToMessage(rfreq, RHandler.mrigrx));
						int mode = rigFreq[4];
						String modemsg = "";
						switch (mode)
						{
							case 0:
								modemsg = "LSB";
								break;
							case 1:
								modemsg = "USB";
								break;
							case 2:
								modemsg = "CW";
								break;
							case 3:
								modemsg = "CWR";
								break;
							case 4:
								modemsg = "AM";
								break;
							case 6:
								modemsg = "WFM";
								break;
							case 8:
								modemsg = "FM";
								break;
							case 10:
								modemsg = "DIG";
								break;
							case 12:
								modemsg = "PKT";
								break;
						}
						hnd.sendMessage(SpecialToMessage(modemsg, RHandler.mrigmode));
					}
				}
			}
		}
	}

	protected void SpecialSocketSend(byte[] buff) {
		try {
			if (oStream != null)
			{
				oStream.write(buff);
				oStream.flush();
			}
		} catch (Exception ex) {
			hnd.sendMessage(ErrorToMessage("Connection closed write"));
		}
	}
}
