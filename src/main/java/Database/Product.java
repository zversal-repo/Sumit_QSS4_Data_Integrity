package Database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

//Functions return null if Exception occurs during query execution
public class Product {

	// Function for getting all available products in the database
	public static ArrayList<Long> getProducts() throws IOException, SQLException {

		String query = "SELECT DISTINCT product_id FROM products ORDER BY product_id";
		return Utilities.getList(query, "product_id", 1L);
	}

}
