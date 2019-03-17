import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) throws SQLException {
		ArrayList<Long> list;
		Connection_DB connection= new Connection_DB();
		Connection conn;
		if ((conn = connection.getConnection()) != null) {
			if ((list = DataIntegrityChecks.Karma_product_check(conn)) != null) {
				if (list.isEmpty() == true) {
					System.out.println("No result");
				} else {
					for (Long i : list) {
						System.out.println(i);
					}
				}
			}
		} else {
			System.out.println("Failed");
		}
	}
}
