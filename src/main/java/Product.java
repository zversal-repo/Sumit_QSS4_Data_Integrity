import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Product {

	// Function for getting all available products in the database
	public static ArrayList<Long> getAllProducts(Connection conn) throws SQLException {

		String query = "SELECT product_id FROM products ORDER BY product_id";

		return MyFunctions.getList(conn, query, "product_id", 1L);
	}

	// Function for getting all services comprising different products
	public static HashMap<Long, ArrayList<Long>> getProductsServices(Connection conn) throws SQLException {

		String query = "SELECT product_id,service_id FROM product_service ORDER BY product_id ASC,service_id ASC";

		return MyFunctions.getMap(conn, query, "product_id", "service_id", 1L, 1L);

	}

}
