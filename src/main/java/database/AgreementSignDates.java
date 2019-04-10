package database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

//Functions return null if Exception occurs during query execution
public class AgreementSignDates {

	// Function for getting all status of each agreement
	public static HashMap<Long, HashMap<Long, Integer>> getAgreementStatus() throws IOException, SQLException {
		String query = "SELECT user_id,agreement_id,status FROM agreementsigndates ORDER BY user_id,agreement_id";
		return QueryProcessor.getMap(query, "user_id", "agreement_id", "status", 1L, 1L, 1);

	}

	// Function for getting status of agreements for a particular user
	public static HashMap<Long, Integer> getAgreementStatus(String user_id) throws IOException, SQLException {
		String query = "SELECT agreement_id,status FROM agreementsigndates WHERE user_id =" + user_id
				+ " ORDER BY agreement_id";
		return QueryProcessor.getOne_OneMap(query, "agreement_id", "status", 1L, 1);

	}

}
