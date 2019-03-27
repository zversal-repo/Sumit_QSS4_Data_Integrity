package IntegrityChecks;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import Database.AgreementService;
import Database.AgreementSignDates;
import Database.ProductService;
import Database.UserProduct;
import Database.Users;
import TablesStatusCode.AgreementStatus;
import TablesStatusCode.UserStatus;

public class DataIntegrityChecks {

	/*
	 * Function return list of data_firm_id=3 users which are active and but does
	 * not contain all mandatory activeProducts{1,2,7,8} or some of them are not
	 * active Function return null if any Exception occurs during Query Execution
	 */

	public static ArrayList<Long> checkForKarmaUsers() throws IOException, SQLException {

		// ArrayList for Karma users who have all products of{1,2,7,8}
		ArrayList<Long> user = new ArrayList<>();
		ArrayList<Long> activeUsers;
		ArrayList<Long> product = new ArrayList<>(); // ArrayList for product {1,2,7,8}
		ArrayList<Long> activeProducts;

		product.add(1L);
		product.add(2L);
		product.add(7L);
		product.add(8L);

		/* Check if SQL query is executed successfully */
		if ((activeUsers = Users.getActiveUsersForData_firm_id("3")) != null) {

			for (Long i : activeUsers) {
				System.out.println("User_id:" + i + "  has active activeProducts");
				System.out.println("\t");

				/* For each active user fetch the list of active activeProducts */
				if ((activeProducts = UserProduct.getActiveProductsForUser_id(String.valueOf(i))) != null) {

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

			/*
			 * Removing all the users from active users which have {1,2,7,8} products active
			 */

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
	public static HashMap<Long, HashMap<Long,String>> checkUnapprovedAgreementsHavingActiveProducts() throws IOException, SQLException {

		HashMap<Long, HashMap<Long,String>> map = null;
		HashMap<Long,ArrayList<Long>> services = ProductService.getServices();
		HashMap<Long,ArrayList<Long>> agreements = AgreementService.getAgreements();
		
		/* For each user in the database */
		for (Long user : Users.getUsers()) {

			String user_id = String.valueOf(user);
			ArrayList<Long> activeProducts = UserProduct.getActiveProductsForUser_id(user_id);
			System.out.println("User " + user_id);
			
			/* For each product of particular user */
			for (Long product : UserProduct.getProducts(user_id)) {

				int flag = 1;
				String string="";

				for (Long service : services.get(product) ) {
					ArrayList<Long> agreementlist;

					if ((agreementlist = agreements.get(service)) != null) {

						for (Long agreement : agreementlist) {
							String agreement_id = String.valueOf(agreement);
							Integer status;

							// If agreement is signed but is not approved set flag=0
							if ((status = AgreementSignDates.getAgreementStatus(user_id,
									agreement_id)) != null) {
								if (status != AgreementStatus.Approved.getStatus()) {
									flag = 0;
									string=string+"Service_id: "+service+" Agreement_Id: "+agreement_id+" is not approved\n";
								}
							} else {// If agreement is not yet signed
								flag = 0;
								string =string +"Service_id: "+service+" Agreement_Id: "+agreement_id+" is not signed yet\n";
							}
						}
					}
				}

				/*
				 * If every service is not approved but active products list for user contains
				 * that product add to the map
				 */
				if (flag == 0 && activeProducts.contains(product)) {

					if (map == null) {
						map = new HashMap<>();
					}

					if (map.containsKey(user)) {
						map.get(user).put(product,string);
						
					} else {
						HashMap<Long,String> temp = new HashMap<>();
						temp.put(product,string);
						map.put(user, temp);
					}
					System.out.println("Product_id  " + product + " is not approved but active because");
					System.out.println(string);
				}
			}
		}

		return map;
	}
	
	/*
	 * Return a map containing all users that are not active with corresponding
	 * active products
	 */

	public static HashMap<Long, ArrayList<Long>> checkInactiveUsersHavingActiveProducts() throws IOException, SQLException {

		HashMap<Long, ArrayList<Long>> map = null;
		ArrayList<Long> activeUsers = Users.getUsers(String.valueOf(UserStatus.Active.getStatus()));

		/* For each user in the database */

		for (Long user : Users.getUsers()) {

			String user_id = String.valueOf(user);
			ArrayList<Long> activeProducts = UserProduct.getActiveProductsForUser_id(user_id);
			//System.out.println("User " + user_id);

			/* if the user is not active and still has active products */

			if (!activeUsers.contains(user) && !activeProducts.isEmpty()) {
				if (map == null) {
					map = new HashMap<>();
				}

				map.put(user, activeProducts);

				System.out.println("User " + user_id + " is not active but has following product active:");
				for (Long product : activeProducts) {
					System.out.print(product + "  ");
				}
				System.out.println();
				System.out.println();

			}

		}
		return map;
	}
}
