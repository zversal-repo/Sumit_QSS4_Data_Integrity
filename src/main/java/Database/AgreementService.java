package Database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

//Functions return null if Exception occurs during query execution
public class AgreementService {

	// Function for getting all available agreements and the services consisting
	// them present in the database
	public static HashMap<Long, ArrayList<Long>> getAgreements() throws IOException, SQLException {
		String query = "SELECT DISTINCT service_id,agreement_id FROM agreement_service";
		return Utilities.getMap(query, "service_id", "agreement_id", 1L, 1L);

	}

	// Function for getting all available agreements for a service_id in the
	// database
	public static ArrayList<Long> getAgreements(String service_id) throws IOException, SQLException {
		String query = "SELECT DISTINCT agreement_id FROM agreement_service WHERE service_id=" + service_id;
		return Utilities.getList(query, "agreement_id", 1L);

	}

}
