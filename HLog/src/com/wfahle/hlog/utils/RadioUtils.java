package com.wfahle.hlog.utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RadioUtils {
	public static String mode(String freq) {
		String ret = "";
		double f = Double.parseDouble(freq);
		if (f > 1.8 && f < 1.843 )
			ret = "CW";
		else if (f >= 1.843 && f < 2.0 )
			ret = "LSB";
		else if (f >= 3.5 && f < 3.57)
			ret = "CW";
		else if (f >= 3.57 && f < 3.6)
			ret = "RTTY";
		else if (f >= 3.6 && f < 3.845)
			ret = "LSB";
		else if (f >= 3.845 && f <= 3.88)
			ret = "SSTV";
		else if (f>3.88 && f < 3.9)
			ret = "AM";
		else if (f > 3.9 && f < 4.0)
			ret = "LSB";
		else if (f >= 7.0 && f < 7.04)
			ret = "CW";
		else if (f >= 7.04 && f <= 7.045)
			ret = "RTTY";
		else if (f > 7.045 && f < 7.07)
			ret = "CW";
		else if (f >= 7.07 && f <= 7.075)
			ret = "PSK31";
		else if (f > 7.075 && f <= 7.08)
			ret = "JT65";
		else if (f > 7.08 && f < 7.125)
			ret = "CW";
		else if (f >= 7.125 && f < 7.290)
			ret = "LSB";
		else if (f >= 7.290 && f <= 7.3)
			ret = "AM";
		else if (f>=10.1 && f < 10.13)
			ret = "CW";
		else if (f >= 10.13 && f <=10.14)
			ret = "RTTY";
		else if (f > 10.14 && f <= 10.15)
			ret = "CW";
		else if (f >= 14.0 && f < 14.07)
			ret = "CW";
		else if (f >= 14.07 && f <= 14.095)
			ret = "RTTY";
		else if (f >= 14.095 && f < 14.15)
			ret = "CW";
		else if (f >= 14.15 && f < 14.35)
			ret = "USB";
		else if (f >= 18.068 && f < 18.11)
			ret = "CW";
		else if (f >= 18.11 && f <= 18.168)
			ret = "USB";
		else if (f >= 21.0 && f < 21.07)
			ret = "CW";
		else if (f >= 21.07 && f < 21.11)
			ret = "RTTY";
		else if (f >= 21.11 && f < 21.2)
			ret = "CW";
		else if (f >= 21.2 && f <= 21.45)
			ret = "USB";
		else if (f >= 24.890 && f < 24.93)
			ret = "CW";
		else if (f >= 24.93 && f <= 24.99)
			ret = "USB";
		else if (f >= 28.0 && f <= 28.3)
			ret = "CW";
		else if (f >= 28.3 && f < 29.0)
			ret = "USB";
		else if (f >= 29.0 && f < 29.2)
			ret = "AM";
		else if (f >= 29.2 && f < 29.7)
			ret = "FM";
		else if (f >= 50.0 && f < 50.1)
			ret = "CW";
		else if (f >= 50.1 && f < 52.0)
			ret = "USB";
		else if (f >= 52.0 && f <= 54.0)
			ret = "FM";
		return ret;
	}
	
	public static int parseanInt(String num) {
		// apparently Java doesn't like parseInt("1.0");
		// assume input starts with a number or space
		int posp = num.indexOf(".");
		if (posp == -1)
			posp = num.length();
		return Integer.parseInt(num.substring(0, posp));
	}
	
	public static String upDown(String rfreq, String comment, boolean ssb) {
		String ret = rfreq;
		
		comment = " "+comment.toLowerCase(Locale.US);
        // Handles: QSX 3.838, QSX 4, UP 5, DOWN 2, U 5, D4, U4, DN4, UP4, DOWN4, QSX7144, u1.8, etc.
        int posp = rfreq.indexOf('.');
        int hz = 0;
        int khz = 0;
        if (posp < 0)
            khz = parseanInt(rfreq);
        else
        {
            khz = parseanInt(rfreq.substring(0, posp));
        	hz = parseanInt(rfreq.substring(posp+1));
        }
        int adjust = 0; // adjust tx up or down accordingly
        int hadjust = 0;
        if (comment.matches(".*\\sup* *[1-9][0-9]*\\.*[0-9]*.*")) // matches "people, up 10", "u1.8", "u 1", etc.
        {
        	 Pattern p = Pattern.compile("up* *([1-9][0-9]*)\\.([0-9])"); // only matches x.y
        	 Matcher m = p.matcher(comment);
        	 if (m.find()) { // dot found for sure, like up 1.9
        	     String num = m.group(1); // Access a submatch group; String can't do this.
        	     adjust = parseanInt(num);
        	     String rem = m.group(2);
        	     hadjust = parseanInt(rem);
        	 }
        	 else { // no dot
	        	 p = Pattern.compile("up* *([1-9][0-9]*)");
	        	 m = p.matcher(comment);
	        	 if (m.find()) { // Find each match in turn; String can't do this.
	        	     String num = m.group(1); 
	        	     adjust = parseanInt(num);
	        	 }
        	 }
        }
        else if (comment.matches(".*[1-9][0-9]* *up")) { // matches "worked 5 up", "5-10 up" "2up" etc
			Pattern p = Pattern.compile("([1-9][0-9]*) *up");
			Matcher m = p.matcher(comment);
			if (m.find()) { // Find each match in turn; String can't do this.
			     String num = m.group(1); 
			     adjust = parseanInt(num);
			}
        }
        else if (comment.matches(".* u") || comment.contains("up") || comment.matches("u .*") ||
        		comment.matches(".* u .*") || comment.equals("u")) {
        	adjust = ssb?5:1;
        }
        else if (comment.matches(".*d[n:own]* *[1-9][0-9]*\\.*[0-9]*.*")) { // matches "people, dn 10, ok?", "d10", "d 1", "down1" etc.
        	Pattern p = Pattern.compile("d[n:own]* *([1-9][0-9]*)\\.([0-9])");
        	Matcher m = p.matcher(comment);
        	if (m.find()) { // Find each match in turn; String can't do this.
        		String num = m.group(1); // Access a submatch group; String can't do this.
        		adjust = -parseanInt(num);
        		String rem = m.group(2);
        		int thz = parseanInt(rem); // assumption - one digit
        		if (thz != 0)
        		{
        			adjust -= 1; // borrow
        			thz = 10 - thz;
        		}
        	}
        	else {
	        	p = Pattern.compile("d[n:own]* *([1-9][0-9]*)");
	        	m = p.matcher(comment);
	        	if (m.find()) { // Find each match in turn; String can't do this.
	        		String num = m.group(1); // Access a submatch group; String can't do this.
	        		adjust = -parseanInt(num);
	        	}
        	}
        }
        else if (comment.contains("dn") || comment.contains("down") || comment.matches(".* d")
        		|| comment.matches("d .*") || comment.equals("d"))
        {
        	adjust = ssb?-5:-1;
        }
        else if (comment.contains("qsx"))
        {
        	Pattern p = Pattern.compile("qsx *([1-9][0-9]*)\\.([0-9]+)");
        	Matcher m = p.matcher(comment);
        	if (m.find()) { // Find each match in turn; String can't do this.
        		String num = m.group(1); // Access a submatch group; String can't do this.
        		String rem = m.group(2);
        		int freqp = parseanInt(num);
        		if (freqp <= 100) { // it's something like 3.182
        			int tkhz = freqp*1000+parseanInt(rem);
        			adjust = tkhz - khz;
        		}
        		else {
        			rem = rem.substring(0,1); // just first sig digit
        			int thz = parseanInt(rem);
        			adjust = freqp - khz; // adjust back to the qsx frequency
        			hadjust = thz - hz;
        		}
        	}
        	else {
            	p = Pattern.compile("qsx *([1-9][0-9]*)");
            	m = p.matcher(comment);
	        	if (m.find()) { // Find each match in turn; String can't do this.
	        		String num = m.group(1); // Access a submatch group; String can't do this.
	        		int freqp = parseanInt(num);
	        		if (freqp <= 1000)
	        			adjust = freqp;
	        		else
	        			adjust = freqp - khz; // adjust back to the qsx frequency
	        	}
        	}
        }
        //TODO: check for reasonableness of adjustments - in band, etc.
        if (adjust == 599 || adjust == 59) //"signals up 599"
        	adjust = 0;
        if (adjust != 0 || hadjust != 0) {
        	khz = khz + adjust;
        	hz = hz + hadjust;
        	if (hz < 0) {
        		khz--; // borrow
        		hz+=10;
        		if (hz < 0) // wth?
        			hz = 0; 
        	}
        	else if (hz > 10) {
        		khz++;
        		hz-=10;
        		if (hz > 10) { // still?
        			hz = 0;
        		}
        	}
        	ret = Integer.toString(khz);
        	if (hz != 0 || posp > 0) {
        		ret = ret + "." + Integer.toString(hz);
        	}
        }		
		return ret;
	}
	
    public static String convertToMHz(String freq, int posp)
    {
    
    	String ret = "";
        if (posp == -1)
        {
        	ret = freq.substring(0,freq.length()-3)+"."+freq.substring(freq.length()-3);
        }
        else
        {
        	if (freq.substring(posp+1).equals("0"))
        		ret = freq.substring(0, posp-3)+"."+freq.substring(posp-3, posp);
        	else
        		ret = freq.substring(0, posp-3)+"."+freq.substring(posp-3, posp)+freq.substring(posp+1);
        }
        return ret;
    }
    

}
