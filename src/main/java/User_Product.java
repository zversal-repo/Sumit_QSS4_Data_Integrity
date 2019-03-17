import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

//Functions return null if Exception occurs during query execution
public class User_Product {

	// Function for getting all products for a particular user
	public static ArrayList<Long> getProductsForUser_id(Connection conn, String user_id) {

		String query = "SELECT DISTINCT product_id FROM user_product WHERE user_id =" + user_id
				+ " ORDER BY product_id";

		return MyFunctions.getList(conn, query, "product_id", 1L);
	}

	// Function for getting all products for every user
	public static HashMap<Long, ArrayList<Long>> getUsersWithProducts(Connection conn) {

		String query = "SELECT DISTINCT user_id,product_id FROM user_product";
		return MyFunctions.getMap(conn, query, "user_id", "product_id", 1L, 1L);

	}
}
