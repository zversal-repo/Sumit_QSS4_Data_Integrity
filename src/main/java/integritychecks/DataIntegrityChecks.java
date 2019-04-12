package integritychecks;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import database.AgreementService;
import database.AgreementSignDates;
import database.Approvals;
import database.UserProduct;
import database.Users;
import utilities.beans.Agreement;
import utilities.beans.Product;
import utilities.beans.User;
import utilities.tablestatuscode.AgreementStatus;
import utilities.tablestatuscode.ApprovalStatus;
import utilities.tablestatuscode.DataFirmId;
import utilities.tablestatuscode.ProductStatus;
import utilities.tablestatuscode.UserStatus;

public class DataIntegrityChecks {

	/*
	 * Returns an ArrayList of User object that can then be used to accessed
	 * multiple fields of User class.
	 **/

	public static ArrayList<User<Long>> checkForKarmaUsers() throws IOException, SQLException {

		ArrayList<User<Long>> list = new ArrayList<>();

		ArrayList<Long> mandatoryProducts = new ArrayList<>(
				Arrays.asList(1L, 2L, 7L, 8L)); /* ArrayList for product {1,2,7,8} */

		HashSet<Long> activeUsers = Users.getActiveUsersForData_firm_id(String.valueOf(DataFirmId.KARMA.getId()));

		HashMap<Long, HashSet<Long>> activeProducts = UserProduct.getActiveProducts();

		for (Long user : activeUsers) {

			try {
				HashSet<Long> activeProductsList = activeProducts.get(user);
				if (!activeProductsList.containsAll(mandatoryProducts)) {

					ArrayList<Long> temp = new ArrayList<>(mandatoryProducts);
					temp.removeAll(activeProductsList);
					list.add(new User<Long>(user, UserStatus.Active.getStatus(), temp));

				}
			} catch (NullPointerException e) {
				list.add(new User<Long>(user, UserStatus.Active.getStatus(), mandatoryProducts));
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

		ArrayList<User<Product<Agreement>>> list = new ArrayList<>();

		HashMap<Long, Integer> userStatus = Users.getUserStatus();
		HashMap<Long, HashSet<Long>> activeProducts = UserProduct.getActiveProducts();
		HashMap<Long, HashSet<Long>> agreements = AgreementService.getProductAgreements();
		HashMap<Long, HashMap<Long, Integer>> agreementStatus = AgreementSignDates.getAgreementStatus();

		for (Long user : userStatus.keySet()) {

			ArrayList<Product<Agreement>> tempProducts = new ArrayList<>();

			try {
				for (Long product : activeProducts.get(user)) {

					ArrayList<Agreement> temp = new ArrayList<>();

					try {
						for (Long agreement : agreements.get(product)) {

							Integer status;

							try {
								status = agreementStatus.get(user).get(agreement);

								if (status != AgreementStatus.Approved.getStatus())
									temp.add(new Agreement(agreement, "Not approved"));

							} catch (NullPointerException e) {
								temp.add(new Agreement(agreement, "Not signed"));
							}

						}

					} catch (NullPointerException e) {

					}

					if (!temp.isEmpty())
						tempProducts.add(new Product<Agreement>(product, ProductStatus.Access.getStatus(), temp));

				}

			} catch (NullPointerException e) {

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

		return list;
	}

	/*
	 * Return a list of inactive users object with their status and list of products
	 * which are active
	 */
	public static ArrayList<User<Long>> checkInactiveUsersHavingActiveProducts() throws IOException, SQLException {

		ArrayList<User<Long>> list = new ArrayList<>();

		HashMap<Long, Integer> users = Users.getUserStatus();
		HashMap<Long, HashSet<Long>> activeProducts = UserProduct.getActiveProducts();

		for (Long user : users.keySet()) {

			Integer status = users.get(user);

			if (status != UserStatus.Active.getStatus()) {
				try {

					ArrayList<Long> activeProductList = new ArrayList<>(activeProducts.get(user));
					if (!activeProductList.isEmpty()) {

						User<Long> tempUser = new User<>(user, status, activeProductList);
						list.add(tempUser);
					}

				} catch (NullPointerException e) {

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

	/*
	 * public static ArrayList<User<Agreement>>
	 * checkAgreementStatusAndApprovalsSync() throws IOException, SQLException {
	 * 
	 * ArrayList<User<Agreement>> list = new ArrayList<>();
	 * 
	 * HashMap<Long, Integer> userStatus = Users.getUserStatus(); HashMap<Long,
	 * HashMap<Long, Integer>> agreements = AgreementSignDates.getAgreementStatus();
	 * HashMap<Long,HashMap<Long, HashMap<Long, Boolean>>> approvals =
	 * Approvals.getApprovalStatus();
	 * 
	 * for (Long user : approvals.keySet()) {
	 * 
	 * Temporary List ArrayList<Agreement> temp = new ArrayList<>();
	 * 
	 * for (Long agreement : approvals.get(user).keySet()) {
	 * 
	 * String string = null;
	 * 
	 * Boolean approval = approvals.get(user).get(agreement);
	 * 
	 * try {
	 * 
	 * int agreementStatus = agreements.get(user).get(agreement);
	 * 
	 * if (approval == ApprovalStatus.WAIT_FOR_AGREEMENT.getStatus()) { string =
	 * "Wait_for_Agreement state in Approvals table\n"; if (agreementStatus ==
	 * AgreementStatus.Accepted.getStatus()) { string = string +
	 * "Accepted state in AgreementSignDates Table"; }
	 * 
	 * else if (agreementStatus == AgreementStatus.Approved.getStatus()) { string =
	 * string + "Approved state in AgreementSignDates Table"; }
	 * 
	 * else { string = string + "Disapproved state in AgreementSignDates Table"; } }
	 * else {
	 * 
	 * if (agreementStatus == AgreementStatus.Approved.getStatus()) { string =
	 * "Wait_for_Approval state in Approvals table\n"; string = string +
	 * "Approved state in AgreementSignDates Table"; }
	 * 
	 * else if (agreementStatus == AgreementStatus.Disapproved.getStatus()) { string
	 * = "Wait_for_Approval state in Approvals table\n"; string = string +
	 * "Disapproved state in AgreementSignDates Table"; }
	 * 
	 * } if (string != null) { temp.add(new Agreement(agreement, string)); }
	 * 
	 * } catch (NullPointerException e) { do nothing }
	 * 
	 * } if (!temp.isEmpty()) { list.add(new User<Agreement>(user,
	 * userStatus.get(user), temp)); }
	 * 
	 * }
	 * 
	 * if (!list.isEmpty()) { for (User<Agreement> user : list) {
	 * System.out.println("\"" + user.getUserId() + "\" with status" +
	 * user.getStatus() + " :{"); for (Agreement agreement : user.getList()) {
	 * System.out.println("\t\"" + agreement.getAgreemenyId() + "\":\"" +
	 * agreement.getStatus() + "\","); } System.out.println("},"); }
	 * System.out.println(); } else { System.out.println("No Default case"); }
	 * 
	 * return list; }
	 */

}
