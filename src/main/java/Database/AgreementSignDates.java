package Database;

import java.sql.Connection;
import java.util.HashMap;

//Functions return null if Exception occurs during query execution
public class AgreementSignDates {

	// Function for getting all status of each agreement
	public static HashMap<Long, HashMap<Long, Integer>> getUserAgreementStatus(Connection conn) {
		String query = "SELECT user_id,agreement_id,status FROM agreementsigndates";
		return Utilities.getMap(conn, query, "user_id", "agreement_id", "status", 1L, 1L, 1);

	}
	
	// Function for getting status of agreements for a particular user
	public static HashMap<Long, Integer> getAgreementStatusForUser_id(Connection conn,String user_id) {
		String query = "SELECT agreement_id,status FROM agreementsigndates WHERE user_id =" +user_id ;
		return Utilities.getOne_OneMap(conn, query,"agreement_id", "status", 1L, 1);

	}
	
	// Function for getting status of particular agreement for a particular user
	public static Integer getAgreementStatusForUser_id(Connection conn,String user_id,String agreement_id) {
		String query = "SELECT status FROM agreementsigndates WHERE user_id =" +user_id+" AND agreement_id=" +agreement_id ;
		return Utilities.getValue(conn, query, "status",(Integer) 1);

	}

}
