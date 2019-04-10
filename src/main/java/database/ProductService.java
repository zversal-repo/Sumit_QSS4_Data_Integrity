package database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

//Functions return null if Exception occurs during query execution
public class ProductService {

	// Function for getting all services comprising different products
	public static HashMap<Long, HashSet<Long>> getServices() throws IOException, SQLException {

		String query = "SELECT product_id,service_id FROM product_service ORDER BY product_id ,service_id ";

		return QueryProcessor.getMap(query, "product_id", "service_id", 1L, 1L);

	}

	// Function for getting all services for a particular product in the database
	public static HashSet<Long> getServices(String product_id) throws IOException, SQLException {

		String query = "SELECT DISTINCT service_id FROM product_service WHERE product_id =" + product_id
				+ " ORDER BY service_id ";

		return QueryProcessor.getList(query, "service_id", 1L);

	}
}
