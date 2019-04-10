package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

	private static Configuration configuration = null;

	private String dbSystem;
	private String database;
	private String userName;
	private String password;
	private String serverName;
	private String port;

	/*
	 * static {
	 * 
	 * }
	 */
	private Configuration() throws IOException {
		InputStream input = null;
		try {

			// For getting the input stream to config files
			input = getClass().getClassLoader().getResourceAsStream("config.properties");

			// load a properties file
			Properties prop = new Properties();
			prop.load(input);

			// get the property values
			dbSystem = prop.getProperty("dbSystem");
			database = prop.getProperty("database");
			userName = prop.getProperty("userName");
			password = prop.getProperty("password");
			serverName = prop.getProperty("serverName");
			port = prop.getProperty("port");

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
	}

	public String getDataBase() {
		return database;
	}

	public String getDataBaseSystem() {
		return dbSystem;
	}

	public String getPassword() {
		return password;
	}

	public String getPort() {
		return port;
	}

	public String getServerName() {
		return serverName;
	}

	public String getUsername() {
		return userName;
	}

	public static Configuration getInstance() throws IOException {
		if (configuration == null) {
			configuration = new Configuration();
		}
		return configuration;
	}

}
