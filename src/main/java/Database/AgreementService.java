package Database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

//Functions return null if Exception occurs during query execution
public class AgreementService {

	// Function for getting all available agreements and the services consisting them present in the database
	public static HashMap<Long, ArrayList<Long>> getAgreementServices(Connection conn) {
		String query = "SELECT DISTINCT agreement_id,service_id FROM agreement_service";
		return Utilities.getMap(conn, query, "agreement_id", "service_id", 1L, 1L);

	}
	
	// Function for getting all available agreements for a service_id in the database	
	public static ArrayList<Long> getAgreementsForService_id(Connection conn,String service_id) {
		String query = "SELECT DISTINCT agreement_id FROM agreement_service WHERE service_id="+service_id;
		return Utilities.getList(conn, query, "agreement_id", 1L);

	}
	

}
