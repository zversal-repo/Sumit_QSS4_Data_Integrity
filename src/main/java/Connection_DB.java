
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connection_DB {

	
	public Connection getConnection() {

		Properties prop = new Properties();
		InputStream input = null;
		String database;
		String userName;
		String password;
		String serverName;
		String port;
		int counter = 5;
		Connection conn=null;

		try {

			input = getClass().getClassLoader().getResourceAsStream("config.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			database=prop.getProperty("database");
			userName=prop.getProperty("userName");
			password=prop.getProperty("password");
			serverName=prop.getProperty("serverName");
			port=prop.getProperty("port");
			

			while (counter != 0) {
				try {
					conn = DriverManager.getConnection("jdbc:mysql://" + serverName + ":" + port + "/"+database,
							userName, password);
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
				try {
					input.close();
				} catch (IOException e) {
					System.out.println(e.toString());
				}
			}
		}
		return null;
	}

}
