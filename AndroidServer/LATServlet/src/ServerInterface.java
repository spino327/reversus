

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.agatha.db.ConnectTo;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
//import org.irtech.DataObserver;
//import org.irtech.DataSubject;

/**
 * Embedded web server 
 * @author pinogal
 *
 */
public class ServerInterface {

	private Server server;
	private DBHandler dbHandler;
	//private HashSet<DataObserver> observers;
	private HttpServlet mobileGateway = new HttpServlet() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5600593145771584745L;

		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("<h1>"+"CPEG657"+"</h1>");
			response.getWriter().println("session=" + request.getSession(true).getId());

			Enumeration en = request.getParameterNames();

			// getting the parameters from the smartphone request
			Map<String, String> data = new HashMap<String, String>();
			data.put("lat", request.getHeader("lat"));
			data.put("lon", request.getHeader("lon"));
			data.put("user", request.getHeader("user"));
			data.put("ts", request.getHeader("ts"));

			// check data
			boolean checkSt = true;
			try {

				Float.parseFloat(data.get("lat"));
				Float.parseFloat(data.get("lon"));
				Integer.parseInt(data.get("user"));
				Float.parseFloat(data.get("ts"));

			} catch (NumberFormatException ex) {
				ex.printStackTrace();
				response.setHeader("Error", "invalid data");
				checkSt = false;
			} catch (NullPointerException ex) {
				ex.printStackTrace();
				response.setHeader("Error", "no data received");
				checkSt = false;
			}


			while(en.hasMoreElements()){
				String parameter = (String) en.nextElement();
				response.getWriter().println("</br> " + parameter);
				//	        	response.getWriter().println(request.getParameter(parameter));
				//	        	System.out.println("<server>" +parameter + ":" + request.getParameter(parameter));

				response.getWriter().println(request.getHeader(parameter));
				System.out.println("<server>" +parameter + ":" + request.getHeader(parameter));
			}

			// Connecting to the database
			//ConnectTo conn = new ConnectTo("com.mysql.jdbc.Driver");
			String user = data.get("user");
			/*if (conn.getConnection("jdbc:mysql://localhost:3306/CPEG657", "root", "root") &&
	        		checkSt)*/ {

	        			// Checking the user
	        			//ResultSet res = conn.sQLQuery("SELECT ID FROM `USER` WHERE name = ?");
	        			// Does the user exist?
	        			if (dbHandler.isValidUser(user)) {

	        				// Class the Observers
	        				//ServerInterface.this.notifyObservers(data, conn);


	        				// 1. PLACE RESOLUTION. TODO use the four square API to retrieve the possible places for the given lat and lon
	        				LocationAnalyzer locAn = new LocationAnalyzer();
	        				ArrayList<String> possibles = locAn.bestResult(data.get("lat"), data.get("lon"));

	        				// 1.1. Probably we will have a list of possible places, so it is important to try to guess which place has a 
	        				// higher probability given the USER "profile". Maybe we can use ideas from collaborative filtering, in order
	        				// to figure out the place given that the user has a similar profile to other users.

	        				//DBHandler db = new DBHandler("jdbc:mysql://localhost:3306/CPEG657", "root", "root");

	        				//dbHandler.addLocationEntry(user, data.get("lon"), data.get("lat"), data.get("ts"), possibles.get(0));

	        				//1.1.5 Send data to phone for evaluation and selection

	        				// 1.2. TODO insert into the database.

	        				// check whether or not the Place exist in the db.

	        				// 2. CATEGORIZATION. TODO having the place resolution

	        				response.setHeader("Result", "true");

	        				return;
	        			} else {
	        				System.out.println("user " + user + " don't exist");
	        				response.setHeader("Error", "invalid user");
	        			}

	        		} 

	        		// if nothing works
	        		response.setHeader("Result", "false");

		}
	};

	public ServerInterface(int port) {
		dbHandler = new DBHandler("jdbc:mysql://localhost:3306/CPEG657", "root", "root");
		server = new Server(port);
		//observers = new HashSet<DataObserver>();

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		
		server.setHandler(context);
		context.addServlet(new ServletHolder(mobileGateway),"/gateway");
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
	/*
	@Override
	public void attach(DataObserver obj) {
		observers.add(obj);
	}

	@Override
	public void detach(DataObserver obj) {		
		observers.remove(obj);
	}

	@Override
	public void notifyObservers(Map<String, String> data, ConnectTo conn) {

		for (DataObserver observer : observers) {

			// call interface method
			observer.computeIRTech(data, conn);
		}

	}
	 */
}
