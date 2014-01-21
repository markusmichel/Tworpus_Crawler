package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class DataBase {

	private Connection conn = null;
	private static String dbHost = "localhost";
	private static String dbPort = "8889";
	private static String database = "tweets";
	private static String dbUser = "tweets";
	private static String dbPassword = "tweets";
	
	public static void initDataBase(String host, String port, String db, String user, String password) {
		dbHost = host;
		dbPort = port;
		database = db;
		dbUser = user;
		dbPassword = password;
	}

	public DataBase() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + DataBase.dbHost + ":"
					+ DataBase.dbPort + "/" + DataBase.database + "?" + "user=" + DataBase.dbUser + "&"
					+ "password=" + DataBase.dbPassword);
		} catch (ClassNotFoundException e) {
			System.out.println("driver not found");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("could not connect");
		}
	}

	public synchronized int insertLang(String lang) {
		if (conn == null)
			return -1;
		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("INSERT INTO langs(_langcode) VALUES(?)");
			stmt.setString(1, lang);
			stmt.executeUpdate();
		} catch (MySQLIntegrityConstraintViolationException e) {
			
		} catch (SQLException e) {
			
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		stmt = null;
		return getLangID(lang);
	}

	public synchronized void insertTweetToTagRelation(long tweetid, int tagid) {
		if (conn == null)
			return;
		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("INSERT IGNORE INTO tweetstotags(_tweetid, _tagid) VALUES(?,?)");
			stmt.setLong(1, tweetid);
			stmt.setInt(2, tagid);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		stmt = null;
	}
	
	public synchronized int insertLocation(String location) {
		if (conn == null)
			return -1;
		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("INSERT IGNORE INTO locations(_location) VALUES(?)");
			stmt.setString(1, location);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		stmt = null;
		return getLocationID(location);
	}

	public synchronized int insertHashTag(String tag) {
		if (conn == null)
			return -1;
		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("INSERT IGNORE INTO tags(_tag) VALUES(?)");
			stmt.setString(1, tag);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		stmt = null;
		return getTagID(tag);
	}

	public synchronized int getLangID(String lang) {
		if (conn == null)
			return -1;
		int result_value = -1;
		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("SELECT _id FROM langs WHERE _langcode LIKE CONCAT('%', ? ,'%') LIMIT 1");
			stmt.setString(1, lang);
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				result_value =  result.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		stmt = null;
		return result_value;
	}

	public synchronized int getLocationID(String location) {
		if (conn == null)
			return -1;
		int result_value = -1;
		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("SELECT _id FROM locations WHERE _location LIKE CONCAT('%', ? ,'%') LIMIT 1");
			stmt.setString(1, location);
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				result_value =  result.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
 
		stmt = null;
		return result_value;
	}

	public synchronized int getTagID(String tag) {
		if (conn == null)
			return -1;
		int result_value = -1;
		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("SELECT _id FROM tags WHERE _tag LIKE CONCAT('%', ? ,'%') LIMIT 1");
			stmt.setString(1, tag);
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				result_value = result.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
 
		stmt = null;
		return result_value;
	}

	public synchronized void insertTweetAndCloseDatabase(long tweet_id, long user_id, long tweet_timestamp,
			int tweet_timezoneoffset, int tweet_charcount, int tweet_wordcount,
			int user_location, int user_lang, int tweet_lang, double tweet_latitude,
			double tweet_longitude) {
		
		if (conn == null)
			return;
		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("INSERT IGNORE INTO tweets(_id, _userid, _timestamp, _timezoneoffset, _charcount, _wordcount, _location, _userlang, _tweetlang, _latitude, _longitude) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setLong(1, tweet_id);
			stmt.setLong(2, user_id);
			stmt.setLong(3, tweet_timestamp);
			stmt.setInt(4, tweet_timezoneoffset);
			stmt.setInt(5, tweet_charcount);
			stmt.setInt(6, tweet_wordcount);
			stmt.setInt(7, user_location);
			stmt.setInt(8, user_lang);
			stmt.setInt(9, tweet_lang);
			stmt.setDouble(10, tweet_latitude);
			stmt.setDouble(11, tweet_longitude);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		stmt = null;
	}

	public synchronized void close() {
		if(conn == null) return;
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
