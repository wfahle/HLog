package com.wfahle.hlog.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TerminalSocket extends HSocket implements Runnable {
	private THandler hnd;
	public byte X = 1;
	public byte Y = 1;
	private String Server = "";
	private int Port = 0;
	private String Logon = "";
	
	public TerminalSocket(THandler h) {
		super(2);
		hnd = h;
		tr = new Thread(this);
	}
	

	public String getLogon() {
		return Logon;
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
			hnd.sendMessage(ErrorToMessage("Socket close failed" + getServer() + ":" + getPort()));
		}
		sk = null;
	}
		
	public void SpecialSocketSend(byte[] buff) {
		try {
			if (oStream != null) {
				oStream.write(buff);
				oStream.flush();
			}
		} catch (Exception ex) {
			hnd.sendMessage(ErrorToMessage("Connection closed write"));
		}
	}

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


	public void setLogon(String logon) {
		Logon = logon;
	}
}
