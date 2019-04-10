package database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;

//Functions return null if Exception occurs during query execution
public class Product {

	// Function for getting all available products in the database
	public static HashSet<Long> getProducts() throws IOException, SQLException {

		String query = "SELECT DISTINCT product_id FROM products ORDER BY product_id";
		return QueryProcessor.getList(query, "product_id", 1L);
	}

}
