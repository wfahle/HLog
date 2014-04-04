package com.wfahle.hlog;

public class TelnetConfig {
	int id;
	String tserver;
	int tport;
    boolean tpreferred;
    
	public TelnetConfig()
	{
	}
	
	public TelnetConfig(String server, int port, boolean preferred)
	{
		tserver = server;
		tport = port;
		tpreferred = preferred;
	}
	public String getServer()
	{
		return tserver;
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
