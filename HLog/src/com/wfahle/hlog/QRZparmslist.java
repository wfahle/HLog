package com.wfahle.hlog;

import java.util.ArrayList;
import java.util.List;

public class QRZparmslist {

	  private List<QRZparm> parms;

	  public QRZparmslist() {	    // TODO Auto-generated constructor stub
	    this.parms = new ArrayList<QRZparm>();
	  }

	  public QRZparmslist(final List<QRZparm> parmListIn) {
	    this.parms = parmListIn;
	  }

	  public boolean add(QRZparm parm) {
	    return this.parms.add(parm);
	  }

	  @Override
	  public String toString() {
	    StringBuilder output = new StringBuilder();

	    for (int n = 0; n < this.parms.size(); n++) {
	      output.append(this.parms.get(n).getKey()).append("=")
	          .append(this.parms.get(n).getValue());
	      if (n != this.parms.size() - 1) {
	        output.append(";");
	      }
	    }

	    return output.toString();
	  }

}
