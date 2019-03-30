package Database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import TablesStatusCode.UserProductStatus;

//Functions return null if Exception occurs during query execution
public class UserProduct {

	// Function for getting all products for a particular user
	public static ArrayList<Long> getProducts(String user_id) throws IOException, SQLException {

		String query = "SELECT DISTINCT product_id FROM user_product WHERE user_id =" + user_id
				+ " ORDER BY product_id";

		return Utilities.getList(query, "product_id", 1L);
	}

	// Function for getting all products for every user
	public static HashMap<Long, ArrayList<Long>> getProducts() throws IOException, SQLException {

		String query = "SELECT DISTINCT user_id,product_id FROM user_product";
		return Utilities.getMap(query, "user_id", "product_id", 1L, 1L);

	}

	// Function for getting current status for all available products in the
	// database
	public static HashMap<Long, HashMap<Long, Integer>> getProductCurrentStatus() throws IOException, SQLException {

		String query = "SELECT m3.* FROM (SELECT *,MAX(transaction_id) AS m2 FROM user_product WHERE creation_time < NOW()\r\n"
				+ "					GROUP BY user_id,product_id) m1 INNER JOIN user_product AS m3 \r\n"
				+ "				ON m3.user_id=m1.user_id AND m1.product_id=m3.product_id AND m1.m2 = m3.transaction_id ORDER BY user_id,product_id;";
		return Utilities.getMap(query, "user_id", "product_id", "status", 1L, 1L, 1);

	}

	// Function for getting all active products for a user in the database
	public static ArrayList<Long> getActiveProductsForUser_id(String user_id) throws IOException, SQLException {

		String query = "SELECT m3.* FROM (SELECT *,MAX(transaction_id) AS m2 FROM user_product WHERE creation_time < NOW() AND user_id = "
				+ user_id
				+ " GROUP BY product_id)m1 INNER JOIN user_product AS m3 ON m3.user_id=m1.user_id AND m1.product_id=m3.product_id AND m1.m2 = m3.transaction_id WHERE m3.status="
				+ String.valueOf(UserProductStatus.Access.getStatus()) + " ORDER BY product_id;";

		return Utilities.getList(query, "product_id", 1L);

	}


}
