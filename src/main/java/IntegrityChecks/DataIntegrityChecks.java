package IntegrityChecks;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import Database.AgreementService;
import Database.AgreementSignDates;
import Database.Approvals;
import Database.UserProduct;
import Database.Users;
import Entity.Agreement;
import Entity.Product;
import Entity.User;
import TablesStatusCode.AgreementStatus;
import TablesStatusCode.ApprovalStatus;
import TablesStatusCode.ProductStatus;
import TablesStatusCode.UserStatus;

public class DataIntegrityChecks {

	/*
	 * Function return map of data_firm_id=3 active users as key and list of
	 * products as value which they don't have out of {1,2,7,8}
	 */

	public static ArrayList<User<Long>> checkForKarmaUsers() throws IOException, SQLException {

		/*
		 * List of active users object with list of mandatory products they don't have
		 * to be returned
		 */
		ArrayList<User<Long>> list = new ArrayList<>();

		ArrayList<Long> mandatoryProducts = new ArrayList<>(
				Arrays.asList(1L, 2L, 7L, 8L)); /* ArrayList for product {1,2,7,8} */

		ArrayList<Long> activeProducts;
		ArrayList<Long> activeUsers = Users.getActiveUsersForData_firm_id("3");

		for (Long user : activeUsers) {

			/*
			 * For each active user fetch the list of active activeProducts Temporary List
			 */
			activeProducts = UserProduct.getActiveProductsForUser_id(String.valueOf(user));

			/* Check if the activeProducts list contains mandatory activeProducts */

			if (!activeProducts.containsAll(mandatoryProducts)) {
				ArrayList<Long> temp = new ArrayList<>(mandatoryProducts);
				temp.removeAll(activeProducts);

				list.add(new User<Long>(user, UserStatus.Active.getStatus(), temp));

			}

		}

		/* Printing the output */
		if (!list.isEmpty()) {

			System.out.println("OutPut:");
			for (User<Long> user : list) {
				System.out.println("User _id :" + user.getUserId() + " does not have following products");

				for (Long product : user.getList()) {
					System.out.print(product + "  ");
				}
				System.out.println("\n");
			}
			System.out.println();
		} else {
			System.out.println("No Default case");
		}

		return list;

	}

	/*
	 * Function to check whether for any non approved product does the product is
	 * active or not
	 */
	public static ArrayList<User<Product<Agreement>>> checkUnapprovedAgreementsHavingActiveProducts()
			throws IOException, SQLException {

		/* List to be returned */
		ArrayList<User<Product<Agreement>>> list = new ArrayList<>();

		/*Map b/w user and their status*/
		HashMap<Long, Integer> userStatus = Users.getUserStatus();

		/* Map For Relationship b/w product agreements */
		HashMap<Long, HashSet<Long>> agreements = AgreementService.getAllAgreements();

		for (Long user : userStatus.keySet()) {

			String user_id = String.valueOf(user);

			/* Temporary List */
			ArrayList<Product<Agreement>> tempProducts = new ArrayList<>();

			ArrayList<Long> activeProducts = UserProduct.getActiveProductsForUser_id(user_id);

			/* For each active product of particular user */
			for (Long product : activeProducts) {

				/* Temporary List */
				ArrayList<Agreement> temp = new ArrayList<>();

				try {
					for (Long agreement : agreements.get(product)) {

						String agreement_id = String.valueOf(agreement);

						Integer status;

						if ((status = AgreementSignDates.getAgreementStatus(user_id, agreement_id)) != null) {
							if (status != AgreementStatus.Approved.getStatus()) {

								temp.add(new Agreement(agreement, "Not approved"));

							}
						} else {

							temp.add(new Agreement(agreement, "Not signed"));
						}

					}
				} catch (NullPointerException e) {
					/* do nothing */
				}
				if (!temp.isEmpty())
					tempProducts.add(new Product<Agreement>(product, ProductStatus.Access.getStatus(), temp));

			}
			if (!tempProducts.isEmpty()) {
				list.add(new User<Product<Agreement>>(user, userStatus.get(user), tempProducts));
			}
		}

		/* Printing output */
		if (!list.isEmpty()) {
			for (User<Product<Agreement>> i : list) {
				System.out.println("User_id:\"" + i.getUserId() + "\": with status " + i.getStatus() + "{");
				for (Product<Agreement> j : i.getList()) {
					System.out.println("\t" + "Product_id:\" " + j.getProductId() + "\":{");

					for (Agreement k : j.getList()) {
						System.out.println("\t\t" + "Agreement_id:\"" + k.getAgreemenyId() + "\":" + k.getStatus());
					}

					System.out.println("\t},");
				}
				System.out.println("},");
			}
		} else {
			System.out.println("No Default case");
		}

		return null;
	}

	/*
	 * Return a list of inactive users object with their status and list of products
	 * which are active
	 */
	public static ArrayList<User<Long>> checkInactiveUsersHavingActiveProducts() throws IOException, SQLException {

		/* List to be returned */

		ArrayList<User<Long>> list = new ArrayList<>();

		/*Map b/w user and their status*/
		HashMap<Long, Integer> users = Users.getUserStatus();

		for (Long user : users.keySet()) {

			Integer status = users.get(user);

			if (status != UserStatus.Active.getStatus()) {

				String user_id = String.valueOf(user);
				ArrayList<Long> activeProducts = UserProduct.getActiveProductsForUser_id(user_id);

				if (!activeProducts.isEmpty()) {

					User<Long> tempUser = new User<>(user, status, activeProducts);
					list.add(tempUser);
				}
			}
		}

		/* Printing the output */

		if (!list.isEmpty()) {

			System.out.println("OutPut:");
			for (User<Long> user : list) {
				System.out.println(user.toString());
				System.out.println("\n");
			}
			System.out.println();
		} else {
			System.out.println("No Default case");
		}

		return list;
	}

	public static ArrayList<User<Agreement>> checkAgreementStatusAndApprovalsSync() throws IOException, SQLException {

		/*List to be returned*/
		ArrayList<User<Agreement>> list = new ArrayList<>();
		
		/*Map b/w user and their status*/
		HashMap<Long, Integer> userStatus = Users.getUserStatus();

		/*Map b/w user ,their agreements and their status in AgreementSignDates Table*/
		HashMap<Long, HashMap<Long, Integer>> agreements = AgreementSignDates.getAgreementStatus();

		/*Map b/w user ,their agreements and their status in Approvals Table*/
		HashMap<Long, HashMap<Long, Boolean>> approvals = Approvals.getApprovalStatus();

		

		for (Long user : approvals.keySet()) {

			/*Temporary List*/
			ArrayList<Agreement> temp = new ArrayList<>();

			for (Long agreement : approvals.get(user).keySet()) {

				String string = null;

				Boolean approval = approvals.get(user).get(agreement);

				try {

					int agreementStatus = agreements.get(user).get(agreement);

					if (approval == ApprovalStatus.WAIT_FOR_AGREEMENT.getStatus()) {
						string = "Wait_for_Agreement state in Approvals table\n";
						if (agreementStatus == AgreementStatus.Accepted.getStatus()) {
							string = string + "Accepted state in AgreementSignDates Table";
						}

						else if (agreementStatus == AgreementStatus.Approved.getStatus()) {
							string = string + "Approved state in AgreementSignDates Table";
						}

						else {
							string = string + "Disapproved state in AgreementSignDates Table";
						}
					} else {

						if (agreementStatus == AgreementStatus.Approved.getStatus()) {
							string = "Wait_for_Approval state in Approvals table\n";
							string = string + "Approved state in AgreementSignDates Table";
						}

						else if (agreementStatus == AgreementStatus.Disapproved.getStatus()) {
							string = "Wait_for_Approval state in Approvals table\n";
							string = string + "Disapproved state in AgreementSignDates Table";
						}

					}
					if (string != null) {
						temp.add(new Agreement(agreement, string));
					}

				} catch (NullPointerException e) {
					/* do nothing */
				}

			}
			if (!temp.isEmpty()) {
				list.add(new User<Agreement>(user, userStatus.get(user), temp));
			}

		}

		if (!list.isEmpty()) {
			for (User<Agreement> user : list) {
				System.out.println("\"" + user.getUserId() + "\" with status" + user.getStatus() + " :{");
				for (Agreement agreement : user.getList()) {
					System.out.println("\t\"" + agreement.getAgreemenyId() + "\":\"" + agreement.getStatus() + "\",");
				}
				System.out.println("},");
			}
			System.out.println();
		} else {
			System.out.println("No Default case");
		}

		return list;
	}

}
