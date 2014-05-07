package com.wfahle.hlog.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import android.os.AsyncTask;

class RadioParms {
	public RadioSocket rsk;
	public static final int pollRig = 1;
	public static final int tuneRig = 2;
	public int command;
	public String mode;
	public String rfreq;
	public String tfreq;
	RadioParms(RadioSocket rsk, int command, String mode, String rfreq, String tfreq) {
		this.rsk = rsk;
		this.command = command;
		this.mode = mode;
		this.rfreq=rfreq;
		this.tfreq=tfreq;
	}
}

class CallRadio extends AsyncTask<RadioParms, Void, Void> {
	@Override
	protected Void doInBackground(RadioParms... params) {
		// if needed, we can include a parameter to specify which command on the radio to call.
		// For now, it calls sendToRadio only. We should use this task only, so that calls are
		// made serially
		int count = params.length;
		for (int i=0; i<count; i++) {
			switch(params[i].command) {
			case 1:
				params[i].rsk.pollRadio();
			break;
			case 2:
				params[i].rsk.sendToRadio(params[i]);
			break;
			}
			
		}
		return null;
	}
}

public class RadioSocket extends HSocket implements Runnable {
	private String Server = "";
	private int Port = 0;
	private RHandler hnd;
	public static final int ackOK = 0;
	public static final int ackERR = -1;
	private HSignal pollingrig;
	private int[] rigFreq=null;
	private volatile int curRigByte = 0;

	public RadioSocket(RHandler h) {
		super(1); // radio is 1, telnet is 2
		pollingrig = new HSignal();
		hnd = h;
		tr = new Thread(this);
	}
// note: These are called from the CallRadio thread only
	public synchronized void setPoll(boolean poll)
	{
		if (!poll)
			curRigByte = 0; // it's been reset, ok to return to normal mode
		pollingrig.setHasDataToProcess(poll);
	}
	
	protected int parseanInt(String num) {
		// apparently Java doesn't like parseInt("1.0");
		// assume input starts with a number or space
		int posp = num.indexOf(".");
		if (posp == -1)
			posp = num.length();
		return Integer.parseInt(num.substring(0, posp));
	}
	
	protected byte[] getBCD(String freqinMhz, byte cmd)
	{
		byte[] ret = new byte[5];
		int len = freqinMhz.length();
        int posp = freqinMhz.indexOf('.');
        if (posp == -1)
        	posp = len;
        // magically convert string to bcd, put in cmd
        int mhz = parseanInt(freqinMhz.substring(0, posp));
        posp++;
        if (posp<len) {
        	mhz = mhz*10+freqinMhz.charAt(posp)-'0';
        	posp++;
        }
        else
        	mhz = mhz*10;
        int dig[] = {0,0,0,0};
        for (int i=0; i<dig.length && posp<len; i++) {
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

	void sendToRadio(RadioParms rsk) {
		while (pollingrig.hasDataToProcess()) {
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				return; // let's just skip it if it's interrupted
			}
		}
    	byte cmd[] = new byte[5];
		int md = 2; // cw
		if (rsk.mode.equals("USB"))
			md = 1;
		else if (rsk.mode.equals("LSB"))
			md = 0;
		else if (rsk.mode.equals("AM"))
			md = 4;
		else if (rsk.mode.equals("FM"))
			md = 8;
		cmd[0] = (byte)md;
		cmd[1] = 0;
		cmd[2] = 0;
		cmd[3] = 0;
		cmd[4] = 7; // 
		sendAndWait(cmd);
    	if (!rsk.tfreq.equals(rsk.rfreq))
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
        		byte frecmd[] = getBCD(rsk.tfreq, (byte)1);
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
		cmd = getBCD(rsk.rfreq, (byte) 1);
		sendAndWait(cmd);		
	}
	
	public void pollRig() {
		/*
		setPoll(true);
        byte[] cmd = new byte[5];
		cmd[0] = 0;
		cmd[1] = 0;
		cmd[2] = 0;
		cmd[3] = 0;
		cmd[4] = (byte)0x3; // read freq and mode
		SpecialSocketSend(cmd);*/
	}
	
	void sendAndWait(byte[] cmd)
	{
		try {
			SpecialSocketSend(cmd);
			Thread.sleep(150);
		} catch (InterruptedException e) {
		}
	}
// end bg thread routines	

	public void tuneRadio(String mode, String rfreq, String tfreq) {
		RadioParms rp = new RadioParms(this, 2, mode, rfreq, tfreq);
		new CallRadio().execute(rp); // run in bg
	}
	
	public void pollRadio() { // get's frequency info from rig - note, not 
//		RadioParms rp = new RadioParms(this, 1, null, null, null);
//		new CallRadio().execute(rp);
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
				hnd.sendMessage(ErrorToMessage("Socket close failed" + getServer() + ":" + getPort()));
			}
		}
		sk = null;
	}
	public void readStatus() {
		byte[] cmd = {0, 0, 0, 0, (byte) 0xE7}; // read receiver status - cause read loop to unlock
		SpecialSocketSend(cmd);
	}

	private void SpecialSocketSend(byte[] buff) {
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
		hnd.sendMessage(InfoToMessage("Resolve server name " + getServer()));
		
		try {
			ia = InetAddress.getByName(getServer());
		} catch (UnknownHostException ex) {
			hnd.sendMessage(ErrorToMessage("Unknown host " + getServer()));
			return;
		}
		hnd.sendMessage(InfoToMessage("Connect to "  + getServer() + ":" + getPort()));
		try {
			sk = new java.net.Socket(ia, getPort());
		} catch (IOException ex) {
			hnd.sendMessage(ErrorToMessage("Socket time out for " + getServer() + ":" + getPort()));
			return;
		}

		try {
			iStream = sk.getInputStream();
			oStream = sk.getOutputStream();
		} catch (IOException ex) {
			hnd.sendMessage(ErrorToMessage("Protocol error for " + getServer() + ":" + getPort()));
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
				if (rigFreq == null) { // just reuse it if not null
					rigFreq = new int[5]; // store up until we have them all
				}
				if (curRigByte >= 0) {
					rigFreq[curRigByte] = (curByte & 0xFF);
					curRigByte++;
					if (curRigByte > 4) { // got them all
						curRigByte = -1;
						int mhz = (rigFreq[0]>>>4) * 100 + (rigFreq[0]& 0xF ) * 10 + (rigFreq[1]>>>4);
						int khz = (rigFreq[1] & 0xF) * 100 + (rigFreq[2] >>> 4) * 10 + 
								(rigFreq[2] & 0xF);
						String kstring = "00"+Integer.toString(khz);
						kstring = kstring.substring(kstring.length()-3);
						String extra = "";
						if (rigFreq[3] != 0) {
							extra = Integer.toString(rigFreq[3]>>>4);
							if ((rigFreq[3] & 0xF) != 0)
								extra += Integer.toString(rigFreq[3] & 0xF);
						}
						if (mhz != 305) {// for some reason 305 comes up if the radio is off
							String rfreq = Integer.toString(mhz)+"."+kstring+extra;
							hnd.sendMessage(SpecialToMessage(rfreq, RHandler.mrigrx));
							int mode = rigFreq[4];
							String modemsg = "";
							switch (mode) {
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
	}
	public String getServer() {
		return Server;
	}
	public void setServer(String server) {
		Server = server;
	}
	public int getPort() {
		return Port;
	}
	public void setPort(int port) {
		Port = port;
	}

}
