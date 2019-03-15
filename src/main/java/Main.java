import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
	public static void main(String[] main) throws SQLException {
		Connection conn;
		if((conn=Connection_DB .getConnection("qss4_test"))!=null) {
			ArrayList<Integer> list;
			if((list=Users.getAllUsers(conn)).isEmpty()) {
				System.out.println("No user");
			}
			else {
				for(Integer i :list) {
					System.out.println(i);
				}
			}
			//conn.close();
		}
		else {
			System.out.println("Failed");
		}
	}
}
