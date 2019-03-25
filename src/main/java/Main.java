import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import Connection_DB.ConnectionDB;
import IntegrityChecks.DataIntegrityChecks;

public class Main {

	public static void main(String[] args) {

		HashMap<Long, ArrayList<Long>> map;
		ConnectionDB connection = new ConnectionDB();
		Connection conn = connection.getConnection();

		if (conn != null) {

			if ((map = DataIntegrityChecks.productAgreementCheck(conn)) != null) {

				if (map.isEmpty() == true) {
					System.out.println("No result");
				}
			}

			connection.closeConnection(conn);
		} else {
			System.out.println("Failed");
		}
	}
}
