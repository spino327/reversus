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

package org.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBHandler {
	String dbURL;
	String dbUser;
	String dbPassword;
	Connection conn;
	
	public DBHandler(String url, String newUser, String passwd){
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
	
	/**
	 * FIXME  Add the record to LocationData. 
	 *        Add one to the frequency of the user to that place.
	 * 
	 * @param user
	 * @param longitude
	 * @param latitude
	 * @param timestamp
	 * @param placeID
	 */
	public void addLocationEntry(String user, String longitude, String latitude, String timestamp, int placeID){
        
		Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            /*pst = con.prepareStatement(
            		"INSERT INTO LocationData(Userid, longitude, latitude, timestamp, Placeid) " +
            		"SELECT Users.id, ?, ?, ?, Places.id " +
            		"FROM Users, Places" +
            		"WHERE Users.name = ? OR Places.name = ?");*/
//            pst = conn.prepareStatement("INSERT INTO LocationData(userID, longitude, latitude, tsTime, placeID)" +
//            		"VALUES(?,?,?,?,?)");
            pst = conn.prepareStatement("INSERT INTO LocationData(userID, longitude, latitude, placeID)" +
            		"VALUES(?,?,?,?)");
            pst.setString(1, user);
            pst.setString(2, longitude);
            pst.setString(3, latitude);
//            pst.setString(4, timestamp);
//            pst.setInt(5, placeID);
            pst.setInt(4, placeID);
            
            //Add one to the frequency of the user to that place.
            if (pst.executeUpdate() >= 1) {
            	
            	pst = conn.prepareStatement("SELECT FREQUENCY FROM HaveBeen WHERE Userid = ? AND Placeid = ?");
            	pst.setString(1, user);
            	pst.setInt(2, placeID);
            	
            	rs = pst.executeQuery();
            	int freq = 0;
            	
            	if(rs.first()) {
            		freq = rs.getInt(1);
            	} else {
            		
            		pst = conn.prepareStatement("INSERT INTO HaveBeen (userID, placeID, frequency) VALUES(?,?,?)");
                    pst.setString(1, user);
                    pst.setInt(2, placeID);
                    pst.setInt(3, 0);
            		
                    pst.executeUpdate();
            	}
            	
            	freq += 1;
        		
        		pst = conn.prepareStatement("UPDATE HaveBeen SET FREQUENCY = ? WHERE Userid = ? AND Placeid = ?");
            	pst.setInt(1, freq);
        		pst.setString(2, user);
            	pst.setInt(3, placeID);
        		
            	if (pst.executeUpdate() >= 1) {
            		System.out.println("updated!");
            	}
            }

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
	
	/**
	 * FIXME
	 * @param name
	 * @param longitude
	 * @param latitude
	 */
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
	
	/**
	 * 
	 * @param name
	 * @param longitude
	 * @param latitude
	 * @param fsID
	 */
	public void addPlace(String name, float longitude, float latitude, String fsID){
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            pst = con.prepareStatement("INSERT INTO PLACE(NAME, LONGITUDE, LATITUDE, fsID) VALUES(?,?,?,?)");
            pst.setString(1, name);
            pst.setFloat(2, longitude);
            pst.setFloat(3, latitude);
            pst.setString(4, fsID);
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
	
	public boolean addPlaceCategory(int placeID, String cat){
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            pst = con.prepareStatement("INSERT INTO Place_Category (placeID, catID) " +
            		"VALUES (?, (SELECT ID FROM Category WHERE name = ?))");
            
            pst.setInt(1, placeID);
            pst.setString(2, cat);
            
            if (pst.executeUpdate()>=1) {
            	return true;
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
        return false;
	}

	public boolean isValidUser(String user){
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
        try {
            if (conn != null){
            	pst = conn.prepareStatement("SELECT ID FROM USERS WHERE ID = ?");
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
	
	/**
	 * Check if the place has a 
	 * 
	 * @param placeID
	 * @return
	 */
	public boolean hasCategory(int placeID) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
        try {
            if (conn != null){
            	pst = conn.prepareStatement("SELECT COUNT(*) FROM  Place_Category WHERE placeID = ?");
            	pst.setInt(1, placeID);
            	rs = pst.executeQuery();
            	
            	System.out.println("Checking if place[" + placeID + "] has a category");
            	
            	if (rs.first()) {
            		
            		if (rs.getInt(1)>0) {
            			return true;
            		} else {
            			System.out.println("get count = 0");
            		}
            	} 
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
	
	/**
	 * return the place ID
	 * @param placeName
	 * @param fsID foursquare Place ID
	 * @return placeId
	 */
	public int getPlaceId(String placeName, float latitude, float longitude, String fsID) {
		
		// to lower case
		placeName = placeName.toLowerCase();
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
        try {
            if (conn != null){
            	pst = conn.prepareStatement("SELECT ID FROM PLACE WHERE NAME = ? AND LONGITUDE = ? AND LATITUDE = ?");
            	
            	pst.setString(1, placeName);
            	pst.setFloat(2, longitude);
            	pst.setFloat(3, latitude);
            	
            	rs = pst.executeQuery();
            	
            	if(rs.first()) {
            		// there is a record for the place name
            		return rs.getInt(1);
            		
            	} else {
            		
            		// we create a record for this place an return the id
            		this.addPlace(placeName, longitude, latitude, fsID);
            		
//            		pst = conn.prepareStatement("SELECT ID FROM PLACE WHERE NAME = ?");
//                	pst.setString(1, placeName);
                	rs = pst.executeQuery();
                	
                	if(rs.first()) {
                		// there is a record for the place name
                		return rs.getInt(1);
                		
                	}
            	}
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
        return -1;
	}
	
	/**
	 * return the [Place Name, Date, Category]
	 * @param placeName
	 * @param fsID foursquare Place ID
	 * @return return an arraylist with the 3 values
	 */
	public ArrayList<String[]> getHaveBeenTable(String userID) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<String[]> result = null;
        try {
            if (conn != null){
            	pst = conn.prepareStatement("SELECT PLACE.NAME,LOCATIONDATA.tsTime,CATEGORY.NAME " +
            			"FROM PLACE,LOCATIONDATA,CATEGORY,PLACE_CATEGORY " +
            			"WHERE LOCATIONDATA.userID = ? AND " +
            			"LOCATIONDATA.Placeid = PLACE.ID AND " +
            			"LOCATIONDATA.Placeid = PLACE_CATEGORY.PLACEID AND " +
            			"PLACE_CATEGORY.CATID = CATEGORY.ID");
            	
            	pst.setString(1, userID);
            	
            	rs = pst.executeQuery();
            	
            	if(rs.first()) {
            		// there is a record for the place name
            		result = new ArrayList<String[]>();
            		
            		while(!rs.isAfterLast()) {
            			result.add(new String[] {rs.getString(1), rs.getString(2), rs.getString(3)});
            			rs.next();
            		}
            	}
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
        
        return result;
	}
	
	/**
	 * return the place Foursquare ID
	 * @return fsID
	 */
	public String getPlaceFSID(int placeID) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
        try {
            if (conn != null){
            	pst = conn.prepareStatement("SELECT fsID FROM PLACE WHERE ID = ?");
            	
            	pst.setInt(1, placeID);
            	
            	rs = pst.executeQuery();
            	
            	if(rs.first()) {
            		// there is a record for the place name
            		return rs.getString(1);
            		
            	}
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
        
        return null;
	}
	
	/**
	 * return the place name
	 * @return fsID
	 */
	public String getPlaceName(int placeID) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		 
        try {
            if (conn != null){
            	pst = conn.prepareStatement("SELECT NAME FROM PLACE WHERE ID = ?");
            	
            	pst.setInt(1, placeID);
            	
            	rs = pst.executeQuery();
            	
            	if(rs.first()) {
            		// there is a record for the place name
            		return rs.getString(1);
            		
            	}
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
        
        return null;
	}
	
	/**
	 * Return the frequency of visit of the user to the given place
	 * @param user
	 * @param longitude
	 * @param latitude
	 * @param timestamp
	 * @param placeName
	 */
	public int getHaveBeenEntry(int user, int place){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            pst = conn.prepareStatement("SELECT FREQUENCY FROM HAVEBEEN WHERE USERID = ? AND PLACEID = ?");

            pst.setInt(1, user);
            pst.setInt(2, place);

            rs = pst.executeQuery();
            
            if (rs.first()) {
            	return rs.getInt(1);
            }

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
        
        return 0;
        
	}
	
	/**
	 * Return the SUM of all the frequencies of visit of the user to the all the places he/she had visited
	 * @param user
	 * @param longitude
	 * @param latitude
	 * @param timestamp
	 * @param placeName
	 */
	public int getSumHaveBeenEntry(int user){
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            pst = conn.prepareStatement("SELECT SUM(FREQUENCY) FROM HAVEBEEN WHERE USERID = ?");

            pst.setInt(1, user);

            rs = pst.executeQuery();
            
            if (rs.first()) {
            	return rs.getInt(1);
            }

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
        
        return 0;
        
	}
	
	/**
	 * Return the total number of places in the DB
	 * 
	 * @return int
	 */
	public int getTotalNumPlaces() {
        
		Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            pst = conn.prepareStatement("SELECT COUNT(*) FROM PLACE");

            rs = pst.executeQuery();
            
            if (rs.first()) {
            	return rs.getInt(1);
            }

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
        
        return 0;
        
	}
}
