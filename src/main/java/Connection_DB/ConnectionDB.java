package Connection_DB;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionDB {

	// To get Connection to the database
	public Connection getConnection() {

		Properties prop = new Properties();
		InputStream input = null;
		String dbSystem;
		String database;
		String userName;
		String password;
		String serverName;
		String port;
		int counter = 5;
		Connection conn = null;

		try {

			// For getting the input stream to config files
			input = getClass().getClassLoader().getResourceAsStream("config.properties");

			// load a properties file
			prop.load(input);

			// get the property values
			dbSystem = prop.getProperty("dbSystem");
			database = prop.getProperty("database");
			userName = prop.getProperty("userName");
			password = prop.getProperty("password");
			serverName = prop.getProperty("serverName");
			port = prop.getProperty("port");

			// loop for try to connect to SQL server atleast 5 times
			while (counter != 0) {
				try {
					conn = DriverManager.getConnection(
							"jdbc:" + dbSystem + "://" + serverName + ":" + port + "/" + database, userName, password);
					System.out.println("Connection Build");
					break;

				} catch (SQLException e) {
					System.out.println(e.toString());
					counter--;

				}

			}
			return conn;

		} catch (IOException ex) {

			System.out.println(ex.toString());

		} finally {

			if (input != null) {
				// Closing the input stream to Configuration file
				try {
					input.close();
				} catch (IOException e) {
					System.out.println(e.toString());
				}
			}

		}
		return null;
	}

	// Function to close a connection
	public void closeConnection(Connection conn) {

		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
		}

	}

}
