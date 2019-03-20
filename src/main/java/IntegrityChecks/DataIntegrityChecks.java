package IntegrityChecks;

import java.sql.Connection;
import java.util.ArrayList;

import Database.UserProduct;
import Database.Users;

public class DataIntegrityChecks {

	// Function return list of data_firm_id=3 users which are active and but does
	// not contain all
	// mandatory activeProducts{1,2,7,8} or some of them are not active
	// Function return null if any Exception occurs during Query Execution

	public static ArrayList<Long> Karma_product_check(Connection conn) {

		// ArrayList for Karma users who have all products of{1,2,7,8}
		ArrayList<Long> user = new ArrayList<>();
		ArrayList<Long> activeUsers;
		ArrayList<Long> product = new ArrayList<>(); // ArrayList for product {1,2,7,8}
		ArrayList<Long> activeProducts;

		product.add(1L);
		product.add(2L);
		product.add(7L);
		product.add(8L);

		// Check if SQL query is executed successfully
		if ((activeUsers = Users.getActiveUsersForData_firm_id(conn, "3")) != null) {

			for (Long i : activeUsers) {
				System.out.println("User_id:" + i + "  has active activeProducts");
				System.out.println("\t");

				// For each active user fetch the list of active activeProducts
				if ((activeProducts = UserProduct.getActiveProductsForUser_id(conn, String.valueOf(i))) != null) {

					for (Long j : activeProducts) {
						System.out.print(j + "\t");
					}

					// Check if the activeProducts list contains mandatory activeProducts
					if (activeProducts.containsAll(product)) { // Check whether the user have all mandatory
																// activeProducts
						user.add(i);

					}
					System.out.println();

				}
			}

			// Removing all the users from active users which have {1,2,7,8} products active

			activeUsers.removeAll(user);
			
			System.out.println(
					"\n\n" + "Following active users of data_firm_id = 3 doesn't have product_id ={1,2,7,8}\n");
			for (Long i : activeUsers) {
				System.out.println(i);
			}

			return activeUsers;

		} else {
			return null;
		}
	}
}
