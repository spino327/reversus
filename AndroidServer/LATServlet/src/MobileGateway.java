

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MobileGateway
 */
@WebServlet("/MobileGateway")
public class MobileGateway extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private DBHandler dbHandler;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("<h1>"+"CPEG657"+"</h1>");
		response.getWriter().println("session=" + request.getSession(true).getId());

		Enumeration en = request.getParameterNames();
		String parameter = "";
		while(en.hasMoreElements()){
			parameter = (String) en.nextElement();
			response.getWriter().println("<br/> " + parameter);
			//	        	response.getWriter().println(request.getParameter(parameter));
			//	        	System.out.println("<server>" +parameter + ":" + request.getParameter(parameter));

			response.getWriter().println(request.getParameter(parameter));
			System.out.println("<server>" +parameter + ":" + request.getParameter(parameter));
		}
		
		// getting the parameters from the smartphone request
		Map<String, String> data = new HashMap<String, String>();
		data.put("lat", request.getParameter("lat"));
		data.put("lon", request.getParameter("lon"));
		data.put("user", request.getParameter("user"));
		data.put("ts", request.getParameter("ts"));

		// Connecting to the database
		String user = data.get("user");

        			// Checking the user
        			// Does the user exist?
        			if (dbHandler.isValidUser(user)) {

        				// 1. PLACE RESOLUTION. TODO use the four square API to retrieve the possible places for the given lat and lon
        				LocationAnalyzer locAn = new LocationAnalyzer();
        				ArrayList<String> possibles = locAn.bestResult(data.get("lat"), data.get("lon"));

        				// 1.1. Probably we will have a list of possible places, so it is important to try to guess which place has a 
        				// higher probability given the USER "profile". Maybe we can use ideas from collaborative filtering, in order
        				// to figure out the place given that the user has a similar profile to other users.

        				//dbHandler.addLocationEntry(user, data.get("lon"), data.get("lat"), data.get("ts"), possibles.get(0));

        				//1.1.5 Send data to phone for evaluation and selection

        				// 1.2. TODO insert into the database.

        				// check whether or not the Place exist in the db.

        				// 2. CATEGORIZATION. TODO having the place resolution

        				response.setHeader("Result", "true");

        				return;
        			} else {
        				System.out.println("user " + user + " doesn't exist");
        				response.setHeader("Error", "invalid user");
        			}

            		// if nothing works
            		//response.setHeader("Result", "false");
        		} 


    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileGateway() {
        super();
        // TODO Auto-generated constructor stub
        dbHandler = new DBHandler("jdbc:mysql://localhost:3306/latdb", "servlet", "1234");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
