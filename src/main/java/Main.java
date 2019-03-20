import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import Connection_DB.ConnectionDB;
import IntegrityChecks.DataIntegrityChecks;

public class Main {
	public static void main(String[] args) throws SQLException {

		ArrayList<Long> list;
		ConnectionDB connection = new ConnectionDB();
		Connection conn = connection.getConnection();

		if (conn != null) {

			if ((list = DataIntegrityChecks.Karma_product_check(conn)) != null) {

				if (list.isEmpty() == true) {
					System.out.println("No result");
				}
			}

			connection.closeConnection(conn);
		} else {
			System.out.println("Failed");
		}
	}
}
