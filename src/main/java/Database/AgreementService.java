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

	public static HashMap<Long, HashMap<Long, HashMap<Long, ArrayList<Long>>>> getAllAgreements()
			throws IOException, SQLException {
		String query = "SELECT user_id,c.product_id,c.service_id,agreement_id FROM \r\n"
				+ "        (SELECT user_id,a.product_id,service_id FROM \r\n"
				+ "		(SELECT DISTINCT user_id,product_id FROM user_product )a \r\n" + "	    INNER JOIN\r\n"
				+ "	        (SELECT DISTINCT product_id,service_id FROM product_service ) b \r\n"
				+ "	ON a.product_id =b.product_id)c \r\n" + "	 \r\n" + "    INNER JOIN \r\n"
				+ "	(SELECT DISTINCT service_id,agreement_id FROM agreement_service)d \r\n"
				+ "    ON c.service_id=d.service_id";
		
		return Utilities.getMap(query, "user_id", "product_id", "service_id", "agreement_id", 1L, 1L, 1L, 1L);

	}
}
