import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connection_DB {
	
	static String userName="root";
	static String password="@$umitA621 @";
	static String serverName="localhost";
	static String portNumber="3306";
	
	public static Connection getConnection(String db){
		int counter =5;
		Connection conn =null;
		
		while(counter!=0) {
	    	try{	
	    		conn = DriverManager.getConnection(
	                    "jdbc:mysql://"+serverName+":"+portNumber+"/qss4_test",
                userName,password);
	    		
	    		break;
	    	}
	    	catch(SQLException e){
	    		System.out.println(e.toString());
	    		counter--;
	    		
	    	}	
	    	
	    }
		return conn;
		

	}
	
}
