

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PossibilityDownloader
 */
@WebServlet("/checkin")
public class PossibilityDownloader extends HttpServlet {
	private DBHandler dbHandler;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PossibilityDownloader() {
        super();
        dbHandler = new DBHandler("jdbc:mysql://localhost:3306/latdb", "servlet", "1234");
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		Enumeration en = request.getParameterNames();
		String parameter = "";
		while(en.hasMoreElements()){
			parameter = (String) en.nextElement();
			System.out.println("<checkin>" +parameter + ":" + request.getParameter(parameter));
		}
		
		String user = request.getParameter("user");
		if (dbHandler.isValidUser(user)){
			
			
			String place = request.getParameter("place");
			if (place.equals("null")){
				LocationAnalyzer locAn = new LocationAnalyzer();
				ArrayList<String> possibles = locAn.bestResult(request.getParameter("lat"), request.getParameter("lon"));

				response.getWriter().println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
				response.getWriter().println("<possibilities>");
				for (String p : possibles){
					if (p.contains("&"))
						p = p.replace("&", "and");
					response.getWriter().println("<possible name=\"" + p + "\"/>");
				}
				response.getWriter().println("</possibilities>");
			}
			else {
				dbHandler.addLocationEntry(user, request.getParameter("lon"), request.getParameter("lat"), 
						request.getParameter("ts"), place, request.getParameter("health"));
			}
		}
		response.flushBuffer();
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
