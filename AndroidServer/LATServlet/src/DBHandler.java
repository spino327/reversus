


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBHandler {
	String dbURL;
	String dbUser;
	String dbPassword;
	Connection conn;
	
	DBHandler(String url, String newUser, String passwd){
		dbURL = url;
		dbUser = newUser;
		dbPassword = passwd;
		try {
			
			conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Initializing DBHandler");
	}
	
	public void addUser(String newUser){
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            pst = con.prepareStatement("INSERT INTO Users(name) VALUES(?)");
            pst.setString(1, newUser);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DBHandler.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DBHandler.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
	}
	
	public void addUserNeed(String needName, String user){
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            pst = con.prepareStatement(
            		"INSERT INTO UserNeeds(Userid, name) " +
            		"SELECT Users.id, ? " +
            		"FROM Users" +
            		"WHERE Users.name = ?");
            pst.setString(1, needName);
            pst.setString(2, user);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DBHandler.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DBHandler.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
	}
	
	public void addLocationEntry(String user, String longitude, String latitude, String timestamp, String placeName){
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            /*pst = con.prepareStatement(
            		"INSERT INTO LocationData(Userid, longitude, latitude, timestamp, Placeid) " +
            		"SELECT Users.id, ?, ?, ?, Places.id " +
            		"FROM Users, Places" +
            		"WHERE Users.name = ? OR Places.name = ?");*/
            pst = conn.prepareStatement("INSERT INTO LocationData(userID, longitude, latitude, tsTime, placeName)" +
            		"VALUES(?,?,?,?,?)");
            pst.setString(1, getUserId(user));
            pst.setString(2, longitude);
            pst.setString(3, latitude);
            pst.setString(4, timestamp);
            pst.setString(5, placeName);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DBHandler.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                /*
                if (con != null) {
                    con.close();
                }*/

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DBHandler.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
	}
	
	public void addPlace(String name, String longitude, String latitude){
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            pst = con.prepareStatement("INSERT INTO Places(name, longitude, latitude) VALUES(?,?,?)");
            pst.setString(1, name);
            pst.setString(2, longitude);
            pst.setString(3, latitude);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DBHandler.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DBHandler.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
	}
	
	public void addCategory(){
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            pst = con.prepareStatement("INSERT INTO Users(name) VALUES(?)");
            //pst.setString(1, newUser);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DBHandler.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DBHandler.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
	}
	
	public void addPlaceCategory(){
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            pst = con.prepareStatement("INSERT INTO Users(name) VALUES(?)");
            //pst.setString(1, newUser);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DBHandler.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DBHandler.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
	}

	public boolean isValidUser(String user){
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
        try {
            if (conn != null){
            	pst = conn.prepareStatement("SELECT Id FROM Users WHERE name = ?");
            	pst.setString(1, user);
            	rs = pst.executeQuery();
            	
            	System.out.println("Checking if " + user + " is valid user");
            	return rs.first();
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DBHandler.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            System.out.println("Server Connection Error");

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                /*
                if (conn != null) {
                    conn.close();
                }*/
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DBHandler.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return false;
	}
	
	public String getUserId(String user){
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
        try {
            if (conn != null){
            	pst = conn.prepareStatement("SELECT Id FROM Users WHERE name = ?");
            	pst.setString(1, user);
            	rs = pst.executeQuery();
            	
            	System.out.println("Checking if " + user + " is valid user");
            	rs.first();
            	return rs.getString(1);
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DBHandler.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            System.out.println("Server Connection Error");

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                /*if (conn != null) {
                    conn.close();
                }*/
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DBHandler.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return "";
	}
}
