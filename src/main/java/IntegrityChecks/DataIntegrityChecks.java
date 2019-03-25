package IntegrityChecks;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import Database.AgreementService;
import Database.AgreementSignDates;
import Database.ProductService;
import Database.UserProduct;
import Database.Users;
import TablesStatusCode.AgreementStatus;

public class DataIntegrityChecks {

	/*
	 * Function return list of data_firm_id=3 users which are active and but does
	 * not contain all mandatory activeProducts{1,2,7,8} or some of them are not
	 * active Function return null if any Exception occurs during Query Execution
	 */

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

	/*
	 * Function to check whether for any non approved product does the product is
	 * active or not
	 */
	public static HashMap<Long, ArrayList<Long>> productAgreementCheck(Connection conn) {

		HashMap<Long, ArrayList<Long>> map = null;

		// For each user in the database
		for (Long user : Users.getUsers(conn)) {

			String user_id = String.valueOf(user);
			ArrayList<Long> activeProducts = UserProduct.getActiveProductsForUser_id(conn, user_id);
			System.out.println("User " + user_id);
			// For each product of particular user
			for (Long product : UserProduct.getProducts(conn, user_id)) {

				String product_id = String.valueOf(product);
				int flag = 1;

				for (Long service : ProductService.getServices(conn, product_id)) {
					String service_id = String.valueOf(service);
					ArrayList<Long> agreements;

					if ((agreements = AgreementService.getAgreementsForService_id(conn, service_id)) != null) {

						for (Long agreement : agreements) {
							String agreement_id = String.valueOf(agreement);
							Integer status;

							// If agreement is signed but is not approved set flag=0
							if ((status = AgreementSignDates.getAgreementStatusForUser_id(conn, user_id,
									agreement_id)) != null) {
								if (status != AgreementStatus.Approved.getStatus())
									flag = 0;
							} else {// If agreement is not yet signed
								flag = 0;
							}
						}
					}
				}

				// If every service is not approved but active products list for user contains
				// that product add to the map
				if (flag == 0 && activeProducts.contains(product)) {

					if (map == null) {
						map = new HashMap<>();
					}

					if (map.containsKey(user)) {
						map.get(user).add(product);
					} else {
						ArrayList<Long> list = new ArrayList<>();
						list.add(product);
						map.put(user, list);
					}
					System.out.println("Product_id  " + product_id + " is not approved but active");

				}
			}
		}

		return map;
	}
}
