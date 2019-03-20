package Database;

import java.sql.Connection;
import java.util.HashMap;

public class AgreementSignDates {

	public HashMap<Long, HashMap<Long, Integer>> getUserAgreementStatus(Connection conn) {
		String query = "SELECT user_id,agreement_id,status FROM agreementsigndates";
		return Utilities.getMap(conn, query, "user_id", "agreement_id", "status", 1L, 1L, 1);

	}

}
