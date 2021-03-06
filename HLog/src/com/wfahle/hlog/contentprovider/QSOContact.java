package com.wfahle.hlog.contentprovider;

import java.util.Calendar;
import java.util.TimeZone;

public class QSOContact {
	String call;
	String rxfreq;
	String txfreq;
	String timeon;
	String timeoff;
	String mode;
	String rrst;
	String srst;
	String name;
	String QTH;
	String state;
	String country;
	String grid;
	String power;
	String complete; // Y, N, NIL, ?
	public QSOContact() {
	
	}
	public QSOContact(String call, String rxfreq, String txfreq, String timeon, String timeoff, String mode, String rrst, String srst, String name,
			String QTH, String state, String country, String grid, boolean complete)
	{
		this.call = call;
		this.rxfreq = rxfreq;
		this.txfreq = txfreq;
		this.timeon = timeon;
		this.timeoff = timeoff;
		this.mode = mode;
		this.rrst = rrst;
		this.srst = srst;
		this.name = name;
		this.QTH=QTH;
		this.state=state;
		this.country=country;
		this.grid=grid;
		this.power="100";
		this.complete = complete?"Y":"N";
	}
	
	// create contact now
	public QSOContact(String call, String rxfreq, String mode, String rrst, String srst)
	{
		this.call = call;
		this.rxfreq = rxfreq;
		this.txfreq = rxfreq;
		this.mode = mode;
		this.rrst = rrst;
		this.srst = srst;
    	TimeZone tz = TimeZone.getTimeZone("GMT+0");
    	Calendar cal = Calendar.getInstance(tz);
    	
    	CharSequence text = ""+cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+
    			cal.get(Calendar.DATE)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
    	this.timeon = (String)text;
    	this.timeoff = this.timeon;
    	this.power = "100";
    	this.complete = "?";
	}
	public String getCall() { return call; }
	public String getrxFreq() { return rxfreq; }
	public String gettxFreq() { return txfreq; }
	public String getMode() { return mode; }
	public String getRRST() { return rrst; }
	public String getSRST() { return srst; }
	public String getName() { return name; }
	public String getQTH() { return QTH; }
	public String getState() { return state; }
	public String getCountry() { return country; }
	public String getGrid() { return grid; }
	public String getPower() { return power; }
	public String getTimeoff() { return timeoff; }
	public String getTimeon() { return timeon; }
	public String getComplete() { return complete; }
	public void setCall(String call) { this.call = call; }
	public void setrxFreq(String rxfreq) { this.rxfreq = rxfreq; }
	public void settxFreq(String txfreq) { this.txfreq = txfreq; }
	public void setMode(String mode) { this.mode = mode; }
	public void setRRST(String rrst) { this.rrst = rrst; }
	public void setSRST(String srst) { this.srst = srst; }
	public void setName(String name) { this.name = name; }
	public void setQTH(String QTH) { this.QTH = QTH; }
	public void setState(String state) { this.state = state; }
	public void setCountry(String country) { this.country = country; }
	public void setGrid(String grid) { this.grid = grid; }
	public void setTimeoff(String timeoff) { this.timeoff = timeoff; }
	public void setTimeon(String timeon) { this.timeon = timeon; }
	public void setComplete(boolean complete) {this.complete = complete?"Y":"N"; }
}
