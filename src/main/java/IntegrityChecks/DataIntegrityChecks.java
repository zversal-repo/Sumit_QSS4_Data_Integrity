package IntegrityChecks;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import Database.User_Product;
import Database.Users;
import TablesStatusCode.UserStatus;
import TablesStatusCode.User_ProductStatus;

public class DataIntegrityChecks {

	// Function return list of data_firm_id=3 users which are active and but does
	// not contain all
	// mandatory products{1,2,7,8} or some of them are not active
	// Function return null if any Exception occurs during Query Execution

	public static ArrayList<Long> Karma_product_check(Connection conn) {

		// ArrayList for Karma users who do not have atleast one out of{1,2,7,8}
		ArrayList<Long> user = new ArrayList<>();
		ArrayList<Long> activeUsers = new ArrayList<>();
		ArrayList<Long> product = new ArrayList<>(); // ArrayList for product {1,2,7,8}
		HashMap<Long, Integer> status; // HashMap for user status
		HashMap<Long, ArrayList<Long>> products;
		HashMap<Long, HashMap<Long, Integer>> user_product_status;
		ArrayList<Long> users;

		product.add(1L);
		product.add(2L);
		product.add(7L);
		product.add(8L);

		if (((users = Users.getUsersForData_firm_id(conn, "3")) != null)
				&& ((status = Users.getUsers_Status(conn)) != null)
				&& ((products = User_Product.getUsersWithProducts(conn)) != null)
				&& ((user_product_status = User_Product.getUsersProductCurrentStatus(conn)) != null)) {

			for (Long i : users) {
				if (status.get(i) == UserStatus.Active.getStatusCode()) { // Check whether the user is active or not
					if (products.get(i).containsAll(product)) { // Check whether the user have all mandatory products
						int flag = 1;
						for (Long j : product) { // Check whether all products are in active state
							if (!(user_product_status.get(i).get(j) == User_ProductStatus.Access.getStatusCode())) {
								flag = flag * 0;
							}
						}
						if (flag == 1) {
							user.add(i);
						}

					}
					activeUsers.add(i);
				}
			}
			activeUsers.removeAll(user);
			return users;
		} else {
			return null;
		}

	}
}
