import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class DataIntegrityChecks {

	// Function return list of data_firm_id=3 users which are active and contain all
	// mandatory products{1,2,7,8}
	// Function return null if any Exception occurs during Query Execution
	public static ArrayList<Long> Karma_product_check(Connection conn) {
		ArrayList<Long> user = new ArrayList<>(); // ArrayList for Karma users who do not have atleast one out
													// of{1,2,7,8}
		ArrayList<Long> product = new ArrayList<>(); // ArrayList for product {1,2,7,8}
		HashMap<Long, Integer> status; // HashMap for user status
		HashMap<Long, ArrayList<Long>> products;
		ArrayList<Long> users;

		product.add(1L);
		product.add(2L);
		product.add(7L);
		product.add(8L);

		if (((users = Users.getUsersForData_firm_id(conn, "3")) != null)
				&& ((status = Users.getUsers_Status(conn)) != null)
				&& ((products = User_Product.getUsersWithProducts(conn)) != null)) {

			for (Long i : users) {
				if (status.get(i) == 3) {
					if (products.get(i).containsAll(product)) {
						users.add(i);
					}
				}
			}
			return user;
		} else {
			return null;
		}

	}
}
