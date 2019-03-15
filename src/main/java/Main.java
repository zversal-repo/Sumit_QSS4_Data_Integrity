import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	public static void main(String[] args) throws SQLException {
		Connection conn;
		if((conn=Connection_DB .getConnection("qss4_test"))!=null) {
			ArrayList<Long> list;
			if((list=Users.getStatusOfUsers(conn, "2")).isEmpty()) {
				System.out.println("No user");
			}
			else {
				for(Long i :list) {
					System.out.println(i);
					
				}
			}
			conn.close();
		}
		else {
			System.out.println("Failed");
		}
	}
}
