package Database;
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

	public static HashMap<Long, HashMap<Long, Integer>> getUsersProductCurrentStatus(Connection conn) {
		String query = "SELECT m1.* FROM user_product AS m1 INNER JOIN (SELECT *,MAX(creation_time) AS m3 FROM user_product WHERE creation_time<NOW() GROUP BY user_id,product_id) m2 \r\n"
				+ "ON m1.user_id=m2.user_id AND m1.product_id=m2.product_id AND m1.creation_time=m2.m3 \r\n"
				+ "GROUP BY m1.user_id,m1.product_id HAVING m1.transaction_id = MIN(m1.transaction_id) ORDER BY user_id,product_id;";
		return MyFunctions.getMap(conn, query, "user_id", "product_id", "status", 1L, 1L, 1);

	}

}
