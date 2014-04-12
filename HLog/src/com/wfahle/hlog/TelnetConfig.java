package com.wfahle.hlog;

public class TelnetConfig {
	int _id;
	String tserver;
	String tcall;
	int tport;
    boolean tpreferred;
    
	public TelnetConfig()
	{
	}
	
	public TelnetConfig(int id, String call, String server, int port, boolean preferred)
	{
		_id = id;
		tcall = call;
		tserver = server;
		tport = port;
		tpreferred = preferred;
	}
	public int getId()
	{
		return _id;
	}
	public String getServer()
	{
		return tserver;
	}
	public String getCall()
	{
		return tcall;
	}
	public int getPort()
	{
		return tport;
	}
	public boolean getPreferred()
	{
		return tpreferred;
	}
}
