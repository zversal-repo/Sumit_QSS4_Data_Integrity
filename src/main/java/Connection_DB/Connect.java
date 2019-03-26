package Connection_DB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import ConfigProperties.Configuration;

public class Connect {

	private static BasicDataSource ds = null;
	
	public static void setDataSource() throws IOException {
		if (ds == null) {
			String dbSystem = Configuration.getInstance().getDataBaseSystem();
			String database = Configuration.getInstance().getDataBase();
			String serverName = Configuration.getInstance().getServerName();
			String port = Configuration.getInstance().getPort();

			ds = new BasicDataSource();
			ds.setUrl("jdbc:" + dbSystem + "://" + serverName +":"+port + "/" + database);
			ds.setUsername(Configuration.getInstance().getUsername());
			ds.setPassword(Configuration.getInstance().getPassword());
			ds.setMinIdle(100);
			ds.setMaxIdle(1000);
			ds.setMaxOpenPreparedStatements(200);
			
		}
	}
 
	// To get Connection to the database
	public static Connection getConnection() throws SQLException, IOException {
		setDataSource();
		return ds.getConnection();
	}

}
