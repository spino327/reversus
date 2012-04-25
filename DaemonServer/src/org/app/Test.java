package org.app;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		Server server = new Server(8082);
		 
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
 
        context.addServlet(new ServletHolder(new HelloServlet()),"/test");
        
//        context.addServlet(new ServletHolder(new HelloServlet()),"/*");
//        context.addServlet(new ServletHolder(new HelloServlet("Buongiorno Mondo")),"/it/*");
//        context.addServlet(new ServletHolder(new HelloServlet("Bonjour le Monde")),"/fr/*");
 
        server.start();
        server.join();
	}

}

class HelloServlet extends HttpServlet
{
    private String greeting="Hello World";
    public HelloServlet(){}
    public HelloServlet(String greeting)
    {
        this.greeting=greeting;
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>"+greeting+"</h1>");
        response.getWriter().println("session=" + request.getSession(true).getId());
        Enumeration en = request.getParameterNames();
        
        while(en.hasMoreElements()){
        	response.getWriter().println(en.nextElement());
        }
        
    }
}