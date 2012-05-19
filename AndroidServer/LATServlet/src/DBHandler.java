


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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

	public void addUserNeed(String user, String type, String numTrans, String health, String timespan){
		Connection con = null;
		PreparedStatement pst = null;

		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

			pst = con.prepareStatement("INSERT INTO UserNeed(userID, type, numTrans, health, timespan) VALUES(?,?,?,?,?)");
			pst.setInt(1, getUserId(user));
			pst.setString(2, type);
			pst.setInt(3, Integer.valueOf(numTrans));
			pst.setString(4, health);
			pst.setLong(5, Long.valueOf(timespan));
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

	public void addLocationEntry(String user, String longitude, String latitude, String timestamp, String placeName, String health){
		//Connection con = null;
		PreparedStatement pst = null;

		try {
			//con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

			/*pst = con.prepareStatement(
            		"INSERT INTO LocationData(Userid, longitude, latitude, timestamp, Placeid) " +
            		"SELECT Users.id, ?, ?, ?, Places.id " +
            		"FROM Users, Places" +
            		"WHERE Users.name = ? OR Places.name = ?");*/
			pst = conn.prepareStatement("INSERT INTO LocationData(userID, longitude, latitude, tsTime, Placeid, health)" +
					"VALUES(?,?,?,?,?,?)");

			if (!isValidPlace(placeName))
				addPlace(placeName, longitude, latitude);
			int placeID = getPlaceId(placeName);

			pst.setInt(1, getUserId(user));
			pst.setString(2, longitude);
			pst.setString(3, latitude);
			pst.setTimestamp(4, new Timestamp(Long.valueOf(timestamp)));
			pst.setInt(5, placeID);
			pst.setInt(6, Integer.valueOf(health));
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

	public ArrayList<String>[] getLocationHistory(String user){
		PreparedStatement pst = null;
		ResultSet rs = null;
		@SuppressWarnings("unchecked")
		ArrayList<String>[] history = new ArrayList[3];
		history[0] = new ArrayList<String>();
		history[1] = new ArrayList<String>();
		history[2] = new ArrayList<String>();

		try {
			System.out.println("Getting History for User " + user);

			pst = conn.prepareStatement("SELECT * FROM LocationData LD " +
					"LEFT JOIN Users U ON LD.userID=U.Id " +
					"LEFT JOIN Place P ON LD.PlaceID = P.ID " +
					"WHERE U.name=? " +
					"ORDER BY tsTime DESC");

			pst.setString(1, user);

			rs = pst.executeQuery();


			while (rs.next()){
				System.out.println(rs.getTimestamp(5));
				history[0].add(String.valueOf(rs.getTimestamp(5)));
				System.out.println(rs.getString(11));
				history[1].add(rs.getString(11));
				System.out.println(rs.getInt(7));
				history[2].add(String.valueOf(rs.getInt(7)));
			}
			return history;

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DBHandler.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DBHandler.class.getName());
				lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
		return null;
	}

	public void addPlace(String name, String longitude, String latitude){
		//Connection con = null;
		PreparedStatement pst = null;

		try {
			//con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

			System.out.println("Adding Place " + name + " to database");
			pst = conn.prepareStatement("INSERT INTO Place(name, longitude, latitude) VALUES(?,?,?)");
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
				//if (con != null) {
					//    con.close();
					//}

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
				if (rs.first()){
					System.out.println(user +" is valid!");
					return rs.first();
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
		System.out.println(user + " is not valid");
		return false;
	}

	public boolean isValidPlace(String place){
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			if (conn != null){
				pst = conn.prepareStatement("SELECT Id FROM Place WHERE name = ?");
				pst.setString(1, place);
				rs = pst.executeQuery();

				System.out.println("Checking if " + place + " is valid place");
				if (rs.first()){
					System.out.println(place +" is valid!");
					return rs.first();
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
		System.out.println(place + "is not valid");
		return false;
	}

	public int getUserId(String user){
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			if (conn != null){
				pst = conn.prepareStatement("SELECT Id FROM Users WHERE name = ?");
				pst.setString(1, user);
				rs = pst.executeQuery();

				System.out.println("Getting ID for  " + user);
				rs.first();
				return Integer.valueOf(rs.getString(1));
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
		return 0;
	}

	public int getPlaceId(String place){
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			if (conn != null){
				pst = conn.prepareStatement("SELECT Id FROM Place WHERE name = ?");
				pst.setString(1, place);
				rs = pst.executeQuery();

				System.out.println("Getting Place ID for " + place);
				rs.first();
				return rs.getInt(1);
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
		return 0;
	}

	public ArrayList<String>[] getAlerts(String user) {
		PreparedStatement pst = null;
		ResultSet needs = null;
		ResultSet locData = null;

		try {
			System.out.println("Getting Alerts for User " + user);

			pst = conn.prepareStatement("SELECT * FROM LocationData LD WHERE LD.userId = ? ORDER BY tsTime DESC");
			pst.setInt(1, getUserId(user));

			locData = pst.executeQuery();

			pst = conn.prepareStatement("SELECT * FROM UserNeed UN WHERE UN.userID = ?");
			pst.setInt(1, getUserId(user));

			needs = pst.executeQuery();

			return getAlerts(needs, locData);

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DBHandler.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DBHandler.class.getName());
				lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}

		return null;
	}

	private ArrayList<String>[] getAlerts(ResultSet needs, ResultSet locData) {
		@SuppressWarnings("unchecked")
		ArrayList<String>[] alerts = new ArrayList[2];
		alerts[0] = new ArrayList<String>();
		alerts[1] = new ArrayList<String>();
		String message;
		String firstDate = null;

		try {
			long now = System.currentTimeMillis();
			while (needs.next()){
				String type = needs.getString("type");
				int numTrans = needs.getInt("numTrans");
				int health = needs.getInt("health");
				long timespan = needs.getLong("timespan");

				int count = 0;
				while (locData.next()){
					Timestamp tsTime = locData.getTimestamp("tsTime");
					int locHealth = locData.getInt("health");

					if (type.equals("timespan")){
						if ((locHealth == health) && ((now - timespan) <= tsTime.getTime())){
							if (firstDate == null)
								firstDate = String.valueOf(tsTime);
							count++;
						}
						if (count >= numTrans){
							if (type.equals("row")){
								if (health > 2){
									message = "You had " + numTrans + " healthy meals in a row! Good Job!!!";
								}
								else if (health < -2){
									message = "You had " + numTrans + " unhealthy meals in a row! Time to hit the gym!";
								}
								else{
									message = "You had " + numTrans + " okay meals in a row. Don't start slipping now!";
								}
							}
							else{
								if (health > 2){
									message = "You had " + numTrans + " healthy meals this week! Good Job!!!";
								}
								else if (health < -2){
									message = "You had " + numTrans + " unhealthy meals this week! Time to hit the gym!";
								}
								else{
									message = "You had " + numTrans + " okay meals this week. Don't start slipping now!";
								}
							}
							alerts[0].add(message);
							alerts[1].add(firstDate);
							count = 0;
							firstDate = null;
						}
					}
					else{
						if ((locHealth == health)){
							count++;
							if (firstDate == null)
								firstDate = String.valueOf(tsTime);
							if (count >= numTrans){
								if (type.equals("row")){
									if (health > 2){
										message = "You had " + numTrans + " healthy meals in a row! Good Job!!!";
									}
									else if (health < -2){
										message = "You had " + numTrans + " unhealthy meals in a row! Time to hit the gym!";
									}
									else{
										message = "You had " + numTrans + " okay meals in a row. Don't start slipping now!";
									}
								}
								else{
									if (health > 2){
										message = "You had " + numTrans + " healthy meals this week! Good Job!!!";
									}
									else if (health < -2){
										message = "You had " + numTrans + " unhealthy meals this week! Time to hit the gym!";
									}
									else{
										message = "You had " + numTrans + " okay meals this week. Don't start slipping now!";
									}
								}
								alerts[0].add(message);
								alerts[1].add(firstDate);
								count = 0;
								firstDate = null;
							}
						}
						else{
							count = 0;
							firstDate = null;
						}
					}


				}
				count = 0;
				firstDate = null;
				locData.first();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return alerts;
	}
}
