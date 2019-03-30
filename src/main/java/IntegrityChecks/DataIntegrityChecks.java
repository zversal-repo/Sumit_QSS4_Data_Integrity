package IntegrityChecks;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Database.AgreementService;
import Database.AgreementSignDates;
import Database.UserProduct;
import Database.Users;
import Entity.Agreement;
import Entity.Product;
import Entity.Service;
import Entity.User;
import Entity.UserAndProduct;
import TablesStatusCode.AgreementStatus;
import TablesStatusCode.UserStatus;

public class DataIntegrityChecks {

	/*
	 * Function return map of data_firm_id=3 active users as key and list of
	 * products as value which they don't have out of {1,2,7,8}
	 */

	public static HashMap<Long, ArrayList<Long>> checkForKarmaUsers() throws IOException, SQLException {

		ArrayList<Long> mandatoryProducts = new ArrayList<>(
				Arrays.asList(1L, 2L, 7L, 8L)); /* ArrayList for product {1,2,7,8} */

		HashMap<Long, ArrayList<Long>> map = new HashMap<>();
		ArrayList<Long> activeProducts;

		ArrayList<Long> activeUsers = Users.getActiveUsersForData_firm_id("3");

		for (Long user : activeUsers) {

			/* For each active user fetch the list of active activeProducts */
			activeProducts = UserProduct.getActiveProductsForUser_id(String.valueOf(user));

			/* Check if the activeProducts list contains mandatory activeProducts */

			if (!activeProducts.containsAll(mandatoryProducts)) {
				ArrayList<Long> temp = new ArrayList<>(mandatoryProducts);
				temp.remove(activeProducts);

				map.put(user, temp);

			}

		}

		/* Printing the output */
		if (map.isEmpty()) {
			System.out.println("No result");
		} else {
			System.out.println("OutPut:");
			for (Long user : map.keySet()) {
				System.out.println("User _id :" + user + " does not have following products");
				for (Long product : map.get(user)) {
					System.out.print(product + "  ");
				}
				System.out.println("\n");
			}
			System.out.println();
		}

		return map;

	}

	/*
	 * Function to check whether for any non approved product does the product is
	 * active or not
	 */
	public static ArrayList<UserAndProduct> checkUnapprovedAgreementsHavingActiveProducts()
			throws IOException, SQLException {

		/* List to be returned */

		ArrayList<UserAndProduct> list = new ArrayList<>();

		/* User Status for all users */
		HashMap<Long, Integer> userStatus = Users.getUserStatus();

		/* Product Current Status for all products of every user */
		HashMap<Long, HashMap<Long, Integer>> productCurrentStatus = UserProduct.getProductCurrentStatus();

		/* Map For Relationship b/w user,product,service and agreements */
		HashMap<Long, HashMap<Long, HashMap<Long, ArrayList<Long>>>> agreements = AgreementService.getAllAgreements();

		for (Long user : agreements.keySet()) {

			String user_id = String.valueOf(user);

			ArrayList<Product> products = new ArrayList<>();

			ArrayList<Long> activeProducts = UserProduct.getActiveProductsForUser_id(user_id);

			/* For each product of particular user */
			for (Long product : agreements.get(user).keySet()) {
				
				String string = "";
				
				ArrayList<Service> services = new ArrayList<>();
				
				for (Long service : agreements.get(user).get(product).keySet()) {

					ArrayList<Long> agreementlist = agreements.get(user).get(product).get(service);
										
					ArrayList<Agreement> agreementStatus = new ArrayList<>();

					for (Long agreement : agreementlist) {
						String agreement_id = String.valueOf(agreement);
						Integer status;

						
						if ((status = AgreementSignDates.getAgreementStatus(user_id, agreement_id)) != null) {
							if (status != AgreementStatus.Approved.getStatus()) {
															
								agreementStatus.add(new Agreement(agreement,"Not approved"));
								

							}
						} else {
											
							agreementStatus.add(new Agreement(agreement,"Not signed"));
						}
					}

					if(!agreementStatus.isEmpty()) 
						services.add(new Service(service,agreementStatus));

				}

				if ((!services.isEmpty()) && activeProducts.contains(product)) {

					Integer status = productCurrentStatus.get(user).get(product);

					products.add(new Product(product, status, services));

				}
				
			}
			if(!products.isEmpty()) {
				list.add(new UserAndProduct(user, userStatus.get(user), products));
			}
		}
		
		/*Printing output*/
		for(UserAndProduct i:list) {
			System.out.println("User_id: "+i.getUserId()+" with status " +i.getStatus());
			for(Product j:i.getProducts()) {
				System.out.println("\t"+"Product_id: "+j.getProductId()+" with status "+j.getStatus());
				for(Service k:j.getServices()) {
					System.out.println("\t\t"+"Service_id: "+k.getServiceId()+" has ");
					for(Agreement l:k.getAgreements()) {
						System.out.println("\t\t\t"+"Agreement_id: "+l.getAgreemenyId()+" "+l.getStatus());
					}
				}
				System.out.println();
			}
			System.out.println();
		}
		
		return list;
	}

	/*
	 * Return a list of inactive users object with their status and list of products
	 * which are active
	 */
	public static ArrayList<User> checkInactiveUsersHavingActiveProducts() throws IOException, SQLException {

		ArrayList<User> list = new ArrayList<>();
		HashMap<Long, Integer> users = Users.getUserStatus();

		/* For each user in the database */

		for (Long user : users.keySet()) {

			Integer status = users.get(user);

			if (status != UserStatus.Active.getStatus()) {

				String user_id = String.valueOf(user);
				ArrayList<Long> activeProducts = UserProduct.getActiveProductsForUser_id(user_id);

				if (!activeProducts.isEmpty()) {

					User tempUser = new User(user, status, activeProducts);
					list.add(tempUser);
				}
			}
		}

		/* Printing the output */

		if (list.isEmpty()) {
			System.out.println("No result");
		} else {
			System.out.println("OutPut:");
			for (User user : list) {
				System.out.println(user.toString());
				System.out.println("\n");
			}
			System.out.println();
		}

		return list;
	}
}
