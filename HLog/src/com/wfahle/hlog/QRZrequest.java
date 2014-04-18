package com.wfahle.hlog;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class QRZrequest {

	  private String login;
	  private String password;
	  private String sessionKey;
	  private String agent = "Generic Java Client";
/*
	  @SuppressWarnings("unused")
	  private QRZrequest() {
	    // TODO Auto-generated constructor stub
	  }
*/
	  public QRZrequest(final String sessionKeyIn) {
	    super();
	    this.login = null;
	    this.password = null;
	    this.sessionKey = sessionKeyIn;
	  }

	  public QRZrequest(final String loginIn, final String passwordIn) {
	    super();
	    this.login = loginIn;
	    this.password = passwordIn;
	    try {
	      this.refreshSessionKey();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

	  public QRZrequest(final String loginIn, final String passwordIn, final String agentIn) {
	    super();
	    this.login = loginIn;
	    this.password = passwordIn;
	    this.agent = agentIn;
	    try {
	      this.refreshSessionKey();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

	  private InputStream qrzHttpClient(QRZparmslist apiParams)
	      throws ClientProtocolException, IOException {

	    HttpParams params = new BasicHttpParams();
	    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	    HttpProtocolParams.setContentCharset(params, "UTF-8");
	    HttpProtocolParams.setUseExpectContinue(params, true);
	    HttpProtocolParams.setHttpElementCharset(params, "UTF-8");

	    HttpProtocolParams.setUserAgent(params, agent);

	    DefaultHttpClient client = new DefaultHttpClient(params);

	    InputStream data = null;

	    if (apiParams == null) {
	      apiParams = new QRZparmslist();
	    }

	    // qrz.com dropped ssl support, passwords sent in plaintext, huge security
	    // flaw
	    String requestUrl = "http://www.qrz.com/xml?" + apiParams.toString();

	    HttpGet method = new HttpGet(requestUrl);
	    HttpResponse res = client.execute(method);
	    data = res.getEntity().getContent();

	    return data;

	  }

	  private String extractSessionKey(final InputStream docInputStream)
	      throws IOException, SAXException, ParserConfigurationException,
	      QRZrequestException {

	    String sessionKey = null;

	    Document doc = null;
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db;

	    db = dbf.newDocumentBuilder();
	    doc = db.parse(docInputStream);

	    doc.getDocumentElement().normalize();

	    NodeList nodeList = doc.getElementsByTagName("QRZDatabase");
	    if (nodeList != null && nodeList.getLength() > 0) {
	      nodeList = ((Element) nodeList.item(0)).getElementsByTagName("Session");
	      if (nodeList != null && nodeList.getLength() > 0) {
	        nodeList = ((Element) nodeList.item(0)).getElementsByTagName("Key");
	        if (nodeList != null && nodeList.getLength() > 0) {
	          Node moo = nodeList.item(0).getFirstChild();
	          if (moo != null) {
	            sessionKey = moo.getNodeValue();
	            return sessionKey;
	          }
	        }
	      }
	    }
	    throw new QRZrequestException("sessionKeyExcept");
	  }

	  private boolean refreshSessionKey() throws QRZrequestException {

	    if (this.login == null) {
	      throw new QRZrequestException("no login specified");
	    }
	    if (this.password == null) {
	      throw new QRZrequestException("no password specified");
	    }

	    QRZparmslist qp = new QRZparmslist();

	    qp.add(new QRZparm("username", this.login));
	    qp.add(new QRZparm("password", this.password));
	    // qp.add(new QrzQueryParam("agent", "unknown"));

	    try {
	      this.sessionKey = extractSessionKey(this.qrzHttpClient(qp));
	    } catch (Exception e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }

	    return false;
	  }

	  public QRZprofile getHamByCallsign(final String callsign)
	      throws ParserConfigurationException, SAXException {

	    if (this.sessionKey == null) {
	      try {
	        this.refreshSessionKey();
	      } catch (QRZrequestException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      }
	    }

	    QRZparmslist qp = new QRZparmslist();

	    qp.add(new QRZparm("s", this.sessionKey));
	    qp.add(new QRZparm("callsign", callsign));
	    // qp.add(new QrzQueryParam("agent", "unknown"));

	    try {
	      return extractHamProfile(this.qrzHttpClient(qp));
	    } catch (ClientProtocolException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    } catch (QRZrequestException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    } catch (QRZcallsignException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	    return null;
	  }

	  private QRZprofile extractHamProfile(final InputStream docInputStream)
	      throws QRZrequestException, ParserConfigurationException, SAXException,
	      IOException, QRZcallsignException {

	    String sessionKey = null;

	    Document doc = null;
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db;

	    db = dbf.newDocumentBuilder();
	    doc = db.parse(docInputStream);

	    doc.getDocumentElement().normalize();

	    NodeList nodeList = doc.getElementsByTagName("QRZDatabase");
	    if (nodeList != null && nodeList.getLength() > 0) {
	      nodeList = ((Element) nodeList.item(0)).getElementsByTagName("Session");
	      if (nodeList != null && nodeList.getLength() > 0) {
	        nodeList = ((Element) nodeList.item(0)).getElementsByTagName("Key");
	        if (nodeList != null && nodeList.getLength() > 0) {
	          Node moo = nodeList.item(0).getFirstChild();
	          if (moo != null) {
	            sessionKey = moo.getNodeValue();
	          }
	        }
	      }
	    }

	    if (sessionKey == null || sessionKey.length() == 0) {
	      String errorMsg = null;
	      nodeList = doc.getElementsByTagName("QRZDatabase");
	      if (nodeList != null && nodeList.getLength() > 0) {
	        nodeList = ((Element) nodeList.item(0)).getElementsByTagName("Session");
	        if (nodeList != null && nodeList.getLength() > 0) {
	          nodeList = ((Element) nodeList.item(0)).getElementsByTagName("Error");
	          if (nodeList != null && nodeList.getLength() > 0) {
	            Node moo = nodeList.item(0).getFirstChild();
	            if (moo != null) {
	              errorMsg = moo.getNodeValue();
	            }
	          }
	        }
	      }
	      throw new QRZrequestException(errorMsg);
	    }

	    String errorMsg = null;
	    nodeList = doc.getElementsByTagName("QRZDatabase");
	    if (nodeList != null && nodeList.getLength() > 0) {
	      nodeList = ((Element) nodeList.item(0)).getElementsByTagName("Session");
	      if (nodeList != null && nodeList.getLength() > 0) {
	        nodeList = ((Element) nodeList.item(0)).getElementsByTagName("Error");
	        if (nodeList != null && nodeList.getLength() > 0) {
	          Node moo = nodeList.item(0).getFirstChild();
	          if (moo != null) {
	            errorMsg = moo.getNodeValue();
	            throw new QRZcallsignException(errorMsg);
	          }
	        }
	      }
	    }

	    QRZprofile profile = new QRZprofile();

	    nodeList = doc.getElementsByTagName("QRZDatabase");
	    if (nodeList != null && nodeList.getLength() > 0) {
	      nodeList = ((Element) nodeList.item(0)).getElementsByTagName("Callsign");
	      if (nodeList != null && nodeList.getLength() > 0) {
	        nodeList = nodeList.item(0).getChildNodes();
	        for (int i = 0; i < nodeList.getLength(); i++) {
	          if (nodeList.item(i) != null && nodeList.item(i).hasChildNodes()) {
	            String nodeName = nodeList.item(i).getNodeName();
	            Node moo = nodeList.item(i).getFirstChild();
	            if (moo != null) {
	              String nodeValue = moo.getNodeValue();
	              profile.setProfileField(nodeName, nodeValue);
	            }
	          }
	        }
	      }
	    }

	    return profile;
	  }

}
