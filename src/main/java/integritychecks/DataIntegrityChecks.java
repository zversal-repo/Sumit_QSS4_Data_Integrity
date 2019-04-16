package integritychecks;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import database.AgreementService;
import database.AgreementSignDates;

import database.UserProduct;

import database.Users;
import utilities.beans.Agreement;
import utilities.beans.Product;
import utilities.beans.User;
import utilities.tablestatuscode.AgreementStatus;
import utilities.tablestatuscode.ProductStatus;
import utilities.tablestatuscode.UserStatus;

public class DataIntegrityChecks {

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

	public static ArrayList<Product<Agreement>> productCheck(Long user, HashMap<Long, Integer> products,
			HashMap<Long, HashSet<Long>> agreementList, HashMap<Long, HashMap<Long, Integer>> agreementStatusList,
			HashMap<Long, HashMap<Long, HashMap<Long, Boolean>>> approvalsStatusList) throws IOException, SQLException {

		ArrayList<Product<Agreement>> list = new ArrayList<>();

		try {
			for (Long product : products.keySet()) {

				Integer productStatus = products.get(product);
				ArrayList<Agreement> temp1 = new ArrayList<>();

				try {
					for (Long agreement : agreementList.get(product)) {

						if (productStatus == ProductStatus.Access.getStatus()) {
							String string = "";
							try {
								Integer agreementStatus = agreementStatusList.get(user).get(agreement);

								if (agreementStatus != null
										&& agreementStatus != AgreementStatus.Approved.getStatus()) {

									string = String.valueOf(agreementStatus);
									temp1.add(new Agreement(agreement, string));
								}
							} catch (NullPointerException e) {

							}

							try {
								boolean b = approvalsStatusList.get(user).get(product).containsKey(agreement);

								if (!temp1.isEmpty() && temp1.get(temp1.size() - 1).getAgreemenyId() == agreement
										&& b) {
									string = string + " but Agreement is still present in approvals table";
									temp1.get(temp1.size() - 1).setStatus(string);
								} else {
									string = string + " is still present in approvals table";
									temp1.add(new Agreement(agreement, string));
								}

							} catch (NullPointerException e) {

							}

						} else if (productStatus == ProductStatus.Cancelled.getStatus()) {
							try {
								boolean b = approvalsStatusList.get(user).get(product).containsKey(agreement);
								if (b) {
									temp1.add(new Agreement(agreement, " is still present in approvals table"));
								}
							} catch (NullPointerException e) {

							}
						} else {
							try {
								list.add(new Product<Agreement>(product, productStatus, null));
								Integer agreementStatus = agreementStatusList.get(user).get(agreement);

								boolean b = approvalsStatusList.get(user).get(product).containsKey(agreement);

								if (!b && agreementStatus != null
										&& agreementStatus == AgreementStatus.Approved.getStatus()) {
									temp1.add(new Agreement(agreement,
											"Agreement should be in approvals table or shoould not be approved yet "));
								}

							} catch (NullPointerException e) {

							}
						}
					}
					if (!temp1.isEmpty()) {
						list.add(new Product<Agreement>(product, productStatus, temp1));
					}
				} catch (NullPointerException e) {
					// e.printStackTrace();
				}

			}

		} catch (NullPointerException e) {
			System.out.println("User does not have any products");
			return null;
		}

		return list;

	}
}
