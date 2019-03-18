package Database;
import java.sql.Connection;
import java.util.ArrayList;

//Functions return null if Exception occurs during query execution
public class Product {

	// Function for getting all available products in the database
	public static ArrayList<Long> getAllProducts(Connection conn) {

		String query = "SELECT DISTINCT product_id FROM products ORDER BY product_id";
		return MyFunctions.getList(conn, query, "product_id", 1L);
	}

}
