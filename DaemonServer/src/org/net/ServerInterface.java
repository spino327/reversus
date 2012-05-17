/**
 * Copyright (c) 2012, University of Delaware
 * All rights reserved.
 *
 * @author: Sergio Pino
 * @author: Keith Elliott
 * Website: http://www.eecis.udel.edu/~pinogal, http://www.eecis.udel.edu/~kelliott
 * emails  : sergiop@udel.edu - kelliott@udel.edu
 * Date   : May, 2012
 *
 */

package org.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.db.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.irtech.Categorization;
import org.irtech.DataObserver;
import org.irtech.DataSubject;
import org.irtech.PlaceResolution;

/**
 * Embedded web server 
 * @author pinogal
 *
 */
public class ServerInterface implements DataSubject {

	private Server server;
	private HashSet<DataObserver> observers;
	private DBHandler dbHandler;
	
	/**
	 * Handle the mobile connections
	 */
	private HttpServlet mobileGateway = new HttpServlet() {
	    
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
	        response.setContentType("text/html");
	        response.setStatus(HttpServletResponse.SC_OK);
	        response.getWriter().println("<h1>"+"CPEG657 Project"+"</h1>");
	        response.getWriter().println("<h2>"+"Keith Elliott, Sergio Pino"+"</h2>");
	        response.getWriter().println("session=" + request.getSession(true).getId());
	        
	        Enumeration en = request.getParameterNames();
	        
	        System.out.println("\n\n");
	        while(en.hasMoreElements()){
	        	String parameter = (String) en.nextElement();
	        	response.getWriter().println("</br> " + parameter);
	        	response.getWriter().println(request.getParameter(parameter));
	        	System.out.println("<server>" +parameter + ":" + request.getParameter(parameter));
	        }
	        
	        // getting the parameters from the smartphone request
	        Map<String, String> data = new HashMap<String, String>();
	        data.put("lat", request.getParameter("lat"));
	        data.put("lon", request.getParameter("lon"));
	        data.put("user", request.getParameter("user"));
	        data.put("ts", request.getParameter("ts"));
	        
	        // check data
	        boolean checkSt = true;
	        try {
	        	Float.parseFloat(data.get("lat"));
	        	Float.parseFloat(data.get("lon"));
	        	Integer.parseInt(data.get("user"));
	        	
	        } catch (NumberFormatException ex) {
	        	ex.printStackTrace();
	        	response.setHeader("Error", "invalid data");
	        	checkSt = false;
	        } catch (NullPointerException ex) {
	        	ex.printStackTrace();
	        	response.setHeader("Error", "no data received");
	        	checkSt = false;
	        }
	        
	        // Connecting to the database
	        String user = data.get("user");

	        // Checking the user
	        // Does the user exist?
	        if (dbHandler.isValidUser(user) &&
	        		checkSt) {

	        	// 1. PLACE RESOLUTION. TODO use the four square API to retrieve the possible places for the given lat and lon
	        	

	        	// 1.1. Probably we will have a list of possible places, so it is important to try to guess which place has a 
	        	// higher probability given the USER "profile". Maybe we can use ideas from collaborative filtering, in order
	        	// to figure out the place given that the user has a similar profile to other users.
	        	
	        	int placeID = PlaceResolution.getResolution(data, dbHandler);
	        	
	        	System.out.println("MostLikely place: " + dbHandler.getPlaceName(placeID));
	        	
	        	data.put("placeID", Integer.toString(placeID));
	        	// add the record to LocationData. Add one to the frequency of the user to that place.
	        	dbHandler.addLocationEntry(user, data.get("lon"), data.get("lat"), data.get("ts"), placeID);

	        	// Class the Observers
	        	// HERE WE EXECUTE ALL THE INFORMATION RETRIEVAL TECHNIQUES
				ServerInterface.this.notifyObservers(data, dbHandler);

	        	response.setHeader("Result", "true");

	        	return;
	        	
	        } else {
	        	System.out.println("user " + user + " doesn't exist");
	        	response.setHeader("Error", "invalid user");
	        }

	        // if nothing works
	        response.setHeader("Result", "false");
	        
	    }
	};
	
	/**
	 * Handle the WebApp connections
	 */
	private HttpServlet webInterface = new HttpServlet() {
	    
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
	    	
	    	String user = request.getParameter("user");
	    	String table = " ";
	    	
	    	if (dbHandler.isValidUser(user)) {
	    		
	    		ArrayList<String[]> result = dbHandler.getHaveBeenTable(user);
	    		
	    		StringBuffer sb = new StringBuffer();
	    		
	    		sb.append("<table border=\"1\" bordercolor=\"FFCC00\" style=\"background-color:FFFFCC\" width=\"400\" cellpadding=\"3\" cellspacing=\"3\">");
	    		sb.append("<th> Place Name </th>");
	    		sb.append("<th> Date </th>");
	    		sb.append("<th> Category </h>");
	    		
	    		if (result!=null) {
		    		
	    			for (String[] strings : result) {
		    			sb.append("<tr>");
		    			sb.append("<td>" + strings[0] + "</td>");
		    			sb.append("<td>" + strings[1] + "</td>");
		    			sb.append("<td>" + strings[2] + "</td>");
		    			sb.append("</tr>");
		    		}
	    		}
		        
	    		sb.append("</table>");
	    		
	    		table = sb.toString();
	    	}
	    	
	    	response.setContentType("text/html");
	        response.setStatus(HttpServletResponse.SC_OK);
	        response.getWriter().println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
	        		"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
	        		"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">" +
	        		"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" +
	        		"<meta name=\"Generator\" content=\"iWeb 3.0.4\" />" +
	        		"<meta name=\"iWeb-Build\" content=\"local-build-20120514\" />" +
	        		"<meta http-equiv=\"X-UA-Compatible\" content=\"IE=EmulateIE7\" />" +
	        		"<meta name=\"viewport\" content=\"width=700\" />" +
	        		"<title>LAT-Locational Activity Tracker</title>" +
	        		"<link rel=\"stylesheet\" type=\"text/css\" media=\"screen,print\" href=\"http://localhost/webapp/webapp.css\" />" +
	        		"<!--[if lt IE 8]><link rel='stylesheet' type='text/css' media='screen,print' href='http://localhost/webapp/webappIE.css'/><![endif]-->" +
	        		"<!--[if gte IE 8]><link rel='stylesheet' type='text/css' media='screen,print' href='Media/IE8.css'/><![endif]-->" +
	        		"<script type=\"text/javascript\" src=\"Scripts/iWebSite.js\"></script>" +
	        		"<script type=\"text/javascript\" src=\"http://localhost/webapp/Widgets/SharedResources/WidgetCommon.js\"></script>" +
	        		"<script type=\"text/javascript\" src=\"http://localhost/webapp/Widgets/Navbar/navbar.js\"></script>" +
	        		"<script type=\"text/javascript\" src=\"http://localhost/webapp/webapp.js\"></script>" +
	        		"</head>" +
	        		"<body style=\"background: rgb(25, 21, 21); margin: 0pt; \" onload=\"onPageLoad();\" onunload=\"onPageUnload();\">" +
	        		"<div style=\"text-align: center; \">" +
	        		"<div style=\"margin-bottom: 0px; margin-left: auto; margin-right: auto; margin-top: 0px; overflow: hidden; position: relative; word-wrap: break-word;  text-align: left; width: 700px; \" id=\"body_content\">" +
	        		"<div style=\"background: transparent url(http://localhost/webapp/rp_tile_v6.jpg) repeat scroll top left; width: 700px; \">" +
	        		"<div style=\"height: 181px; margin-left: 0px; position: relative; width: 700px; z-index: 10; \" id=\"header_layer\">" +
	        		"<div style=\"height: 0px; line-height: 0px; \" class=\"bumper\"> </div>" +
	        		"<div style=\"height: 95px; width: 634px;  height: 95px; left: 33px; position: absolute; top: 86px; width: 634px; z-index: 1; \" class=\"tinyText\">" +
	        		"<div style=\"position: relative; width: 634px; \">" +
	        		"<img src=\"http://localhost/webapp/shapeimage_1.png\" alt=\"LAT-Locational Activity Tracker\" style=\"height: 51px; left: 0px; margin-left: 2px; margin-top: 27px; position: absolute; top: 0px; width: 386px; \" />" +
	        		"</div>" +
	        		"</div>" +
	        		"</div>" +
	        		"<div style=\"margin-left: 0px; position: relative; width: 700px; z-index: 0; \" id=\"nav_layer\">" +
	        		"<div style=\"height: 0px; line-height: 0px; \" class=\"bumper\"> </div>" +
	        		"<div style=\"height: 490px; width: 700px;  height: 490px; left: 0px; position: absolute; top: -489px; width: 700px; z-index: 1; \" class=\"tinyText style_SkipStroke\">" +
	        		"<img src=\"http://localhost/webapp/navfill-v2.jpg\" alt=\"\" style=\"border: none; height: 490px; width: 700px; \" />" +
	        		"</div>" +
	        		"<div style=\"height: 1px; line-height: 1px; \" class=\"tinyText\"> </div>" +
	        		"<div class=\"com-apple-iweb-widget-navbar flowDefining\" id=\"widget0\" style=\"margin-left: 0px; margin-top: -1px; opacity: 1.00; position: relative; width: 700px; z-index: 1; \">" +
	        		"<div id=\"widget0-navbar\" class=\"navbar\">" +
	        		"<div id=\"widget0-bg\" class=\"navbar-bg\">" +
	        		"<ul id=\"widget0-navbar-list\" class=\"navbar-list\">" +
	        		"<li></li>" +
	        		"</ul>" +
	        		"</div>" +
	        		"</div>" +
	        		"</div>" +
	        		"<script type=\"text/javascript\"><!--//--><![CDATA[//><!--" +
//	        		"new NavBar('widget0', 'Scripts/Widgets/Navbar', 'Scripts/Widgets/SharedResources', '.', {\"path-to-root\": \"\", \"navbar-css\": \".navbar {\n\tfont-family: 'Courier New', Courier, monospace, serif;\n\tfont-size: 1.1em;\n\tcolor: #AFAA9F;\n\tmargin: -2px 0 0 0px;\n\tline-height: 20px;\n\tpadding: 5px 0 12px 0;\n\tfont-weight: bold;\n\tbackground-image: url(http://localhost/webapp\/navfill-v2_1.jpg);\n}\n\n.navbar-bg {\n\ttext-align: left;\n}\n\n.navbar-bg ul {\n\tlist-style: none;\n\tmargin: 0px;\n\tpadding: 0 10px 0 30px;\n}\n\n\nli {\n\tlist-style-type: none;\n\tdisplay: inline;\n\tpadding: 0px 25px 0 0;\n}\n\n\nli a {\n\ttext-decoration: none;\n\tcolor: #AFAA9F;\n}\n\nli a:visited {\n\ttext-decoration: none;\n\tcolor: #AFAA9F;\n}\n\nli a:hover\n{\n \tcolor: #D64600;\n}\n\n\nli.current-page a\n{\n\t color: #fff;\n\n}\n\", \"current-page-GUID\": \"F5817181-824E-4876-A7A3-B6ADA3C7631E\", \"isCollectionPage\": \"NO\"});" +
	        		"//--><!]]></script>" +
	        		"<div style=\"clear: both; height: 0px; line-height: 0px; \" class=\"spacer\"> </div>" +
	        		"</div>" +
	        		"<div style=\"margin-left: 0px; position: relative; width: 700px; z-index: 5; \" id=\"body_layer\">" +
	        		"<div style=\"height: 0px; line-height: 0px; \" class=\"bumper\"> </div>" +
	        		"<div style=\"height: 25px; width: 700px;  height: 25px; left: 0px; position: absolute; top: -15px; width: 700px; z-index: 1; \" class=\"tinyText style_SkipStroke_1\">" +
	        		"<img src=\"http://localhost/webapp/pagetop10.png\" alt=\"\" style=\"border: none; height: 25px; width: 700px; \" />" +
	        		"</div>" +
	        		"<br>USERID = " + user +
	        		// Table
	        		table +        		
	        		// EOTable
	        		"<div style=\"height: 480px; line-height: 480px; \" class=\"spacer\"> </div>" +
	        		"</div>" +
	        		"<div style=\"height: 100px; margin-left: 0px; position: relative; width: 700px; z-index: 15; \" id=\"footer_layer\">" +
	        		"<div style=\"height: 0px; line-height: 0px; \" class=\"bumper\"> </div>" +
	        		"<a href=\"http://apple.com/mac\" title=\"http://apple.com/mac\"><img src=\"http://localhost/webapp/mwmac.png\" alt=\"Made on a Mac\" style=\"border: none; height: 50px; left: 0px; opacity: 0.55; position: absolute; top: 39px; width: 139px; z-index: 1; \" id=\"id1\" />" +
	        		"</a>" +
	        		"</div>" +
	        		"</div>" +
	        		"</div>" +
	        		"</div>" +
	        		"</body>" +
	        		"</html>");
	        
	    }
	};
	
	public ServerInterface(int port) {
		server = new Server(port);
		observers = new HashSet<DataObserver>();
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        
        server.setHandler(context);
 
        context.addServlet(new ServletHolder(mobileGateway),"/gateway");
        context.addServlet(new ServletHolder(webInterface),"/web");
        
        dbHandler = new DBHandler("jdbc:mysql://localhost:3306/Project", "root", "root");
	}
	
	public void start() {
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		try {
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void join() {
		try {
			server.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void attach(DataObserver obj) {
		observers.add(obj);
	}

	@Override
	public void detach(DataObserver obj) {		
		observers.remove(obj);
	}

	@Override
	public void notifyObservers(Map<String, String> data, DBHandler db) {
		
		for (DataObserver observer : observers) {
			
			// call interface method
			observer.computeIRTech(data, db);
		}
		
	}
	
	public static void main(String[] args) {
		
		System.out.println("Server starting up...");
		
		ServerInterface server = new ServerInterface(8080);
		
		server.attach(new Categorization());
		
		server.start();
		server.join();
	}
	
}
