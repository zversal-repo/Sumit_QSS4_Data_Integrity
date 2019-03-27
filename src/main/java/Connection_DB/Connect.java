package Connection_DB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//
//import org.apache.commons.dbcp2.BasicDataSource;

import ConfigProperties.Configuration;

public class Connect{

	private static Connect connect =null;
	private Connection conn;
	
	private Connect() throws IOException, SQLException {
		Configuration config=Configuration.getInstance();
		String url="jdbc:"+config.getDataBaseSystem()+"://"+config.getServerName()+":"+config.getPort()+"/"+config.getDataBase();
		conn=DriverManager.getConnection(url, config.getUsername(),config.getPassword());
	}
 
	// To get Connection to the database
	public static Connection getConnection() throws SQLException, IOException {
		if(connect==null) {
			connect=new Connect();
		}
		return connect.conn;
	}
	
	public static void closeConnection() throws SQLException{
		if(connect.conn!=null) {
			connect.conn.close();
		}
	}

}
