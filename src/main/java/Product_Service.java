import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

//Functions return null if Exception occurs during query execution
public class Product_Service {

	// Function for getting all services comprising different products
	public static HashMap<Long, ArrayList<Long>> getProductsServices(Connection conn) {

		String query = "SELECT product_id,service_id FROM product_service ORDER BY product_id ASC,service_id ASC";

		return MyFunctions.getMap(conn, query, "product_id", "service_id", 1L, 1L);

	}
}
