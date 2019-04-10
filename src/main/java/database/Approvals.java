package database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class Approvals {

	public static HashMap<Long, HashMap<Long, Boolean>> getApprovalStatus() throws IOException, SQLException {

		String query = "SELECT m1.* FROM approvals AS m1 INNER JOIN ( SELECT *,MAX(transaction_id) AS m3 FROM approvals WHERE update_time<NOW() GROUP BY user_id,product_id,service_id,agreement_id )m2 \r\n"
				+ "	ON (m1.user_id=m2.user_id AND m1.product_id=m2.product_id AND m1.service_id=m2.service_id AND m1.agreement_id=m2.agreement_id AND m1.transaction_id=m2.m3 )\r\n"
				+ "	ORDER BY user_id,product_id,service_id,agreement_id;";

		return QueryProcessor.getMap(query, "user_id", "agreement_id", "status", 1L, 1L, true);
	}

}
