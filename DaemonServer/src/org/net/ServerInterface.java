package org.net;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Embedded web server 
 * @author pinogal
 *
 */
public class ServerInterface {

	private Server server;
	
	private HttpServlet mobileGateway = new HttpServlet() {
	    
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
	        response.setContentType("text/html");
	        response.setStatus(HttpServletResponse.SC_OK);
	        response.getWriter().println("<h1>"+"CPEG657"+"</h1>");
	        response.getWriter().println("session=" + request.getSession(true).getId());
//	        Enumeration en = request.getParameterNames();
	        
	        Enumeration en = request.getHeaderNames();
	        response.setHeader("Result", "false");
	        
	        while(en.hasMoreElements()){
	        	String parameter = (String) en.nextElement();
	        	response.getWriter().println("</br> " + parameter);
//	        	response.getWriter().println(request.getParameter(parameter));
//	        	System.out.println("<server>" +parameter + ":" + request.getParameter(parameter));
	        	
	        	response.getWriter().println(request.getHeader(parameter));
	        	System.out.println("<server>" +parameter + ":" + request.getHeader(parameter));
	        }
	        
	    }
	};
	
	public ServerInterface(int port) {
		server = new Server(port);
		
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
	
}
