package Database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class AgreementService {

	public HashMap<Long, ArrayList<Long>> getAgreementServices(Connection conn) {
		String query = "SELECT DISTINCT agreement_id,service_id FROM agreement_service";
		return Utilities.getMap(conn, query, "agreement_id", "service_id", 1L, 1L);

	}

}
