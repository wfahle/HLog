package com.wfahle.unused;

public class TelnetConfig {
	int _id;
	String tserver;
	String tcall;
	String rserver;
	int tport;
	int rport;
    boolean tpreferred;
    
	public TelnetConfig()
	{
	}
	
	public TelnetConfig(int id, String call, String server, int port, 
			String radioServer, int radioPort, boolean preferred)
	{
		_id = id;
		tcall = call;
		tserver = server;
		tport = port;
		tpreferred = preferred;
		rserver= radioServer;
		rport = radioPort;
	}
	public int getId()
	{
		return _id;
	}
	public String getServer()
	{
		return tserver;
	}
	public String getRadioServer()
	{
		return rserver;
	}
	public String getCall()
	{
		return tcall;
	}
	public int getPort()
	{
		return tport;
	}
	public int getRadioPort()
	{
		return rport;
	}
	public boolean getPreferred()
	{
		return tpreferred;
	}
}
