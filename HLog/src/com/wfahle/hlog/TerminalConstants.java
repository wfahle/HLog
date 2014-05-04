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
import android.text.TextUtils;
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

class MySignal{

	  protected boolean hasDataToProcess = false;

	  public synchronized boolean hasDataToProcess(){
	    return this.hasDataToProcess;
	  }

	  public synchronized void setHasDataToProcess(boolean hasData){
	    this.hasDataToProcess = hasData;  
	  }

}


