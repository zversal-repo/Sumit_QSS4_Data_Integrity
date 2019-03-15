import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Users {
	
	public static ArrayList<Integer> getAllUsers(Connection conn) throws SQLException{
		ArrayList<Integer> userIdList=new ArrayList<>();
		Statement stmt = null;
		String query="SELECT user_id from users";
                
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
            	userIdList.add(rs.getInt("user_id"));
            }
        } 
        catch (SQLException e ) {
            System.out.println(e.toString());
        } 
        finally {
            if (stmt != null) { 
            	stmt.close(); 
            }
        }
		
		return userIdList;
	}
	
}
