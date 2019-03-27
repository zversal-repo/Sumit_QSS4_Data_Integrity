
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import Connection_DB.Connect;
import IntegrityChecks.DataIntegrityChecks;

public class Main {

	public static void main(String[] args) throws IOException, SQLException {

		HashMap<Long,ArrayList<Long>> map;

		if ((map = DataIntegrityChecks.productUserCheck()) != null) {

			if (map.isEmpty() == true) {
				System.out.println("No result");
			}
		}
		else {
			System.out.println("Application failed");
		}
		
		Connect.closeConnection();
	}
}
