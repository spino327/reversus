package lat.server;


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
	
	DBHandler(String url, String newUser, String passwd){
		dbURL = url;
		dbUser = newUser;
		dbPassword = passwd;
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

            pst = con.prepareStatement(
            		"INSERT INTO LocationData(Userid, longitude, latitude, timestamp, Placeid) " +
            		"SELECT Users.id, ?, ?, ?, Places.id " +
            		"FROM Users, Places" +
            		"WHERE Users.name = ? OR Places.name = ?");
            pst.setString(1, longitude);
            pst.setString(2, latitude);
            pst.setString(3, timestamp);
            pst.setString(4, user);
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
                if (con != null) {
                    con.close();
                }

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
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean valid = false;
		 
        try {
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            pst = con.prepareStatement("SELECT Id FROM Users WHERE name = ?");
            pst.setString(1, user);
            rs = pst.executeQuery();
            
            rs.last();
            if (rs.getRow() > 0){
            	valid = true;
            }

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
        return valid;
	
	}
	/*public static void main(String[] args) {

        Connection con = null;
        PreparedStatement pst = null;

        String url = "jdbc:mysql://localhost:3306/CPEG657";
        String user = "root";
        String password = "root";

        try {

            String newUser = "John Smith";
            String newEmail = "jsmith@udel.edu";
            con = DriverManager.getConnection(url, user, password);

            pst = con.prepareStatement("INSERT INTO Users(name, email) VALUES(?,?)");
            pst.setString(1, newUser);
            pst.setString(2, newEmail);
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
    }*/
}
