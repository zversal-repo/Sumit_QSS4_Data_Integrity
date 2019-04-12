package integritychecks;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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
import utilities.tablestatuscode.DataFirmId;
import utilities.tablestatuscode.ProductStatus;
import utilities.tablestatuscode.UserStatus;

public class NanexIntegrityCheck {

	private static HashMap<Long, Integer> userStatusList;
	private static HashMap<Long, HashMap<Long, Integer>> productList;
	private static HashMap<Long, HashSet<Long>> agreementList;
	private static HashMap<Long, HashMap<Long, Integer>> agreementStatusList;
	private static HashMap<Long, HashMap<Long, HashMap<Long, Boolean>>> approvalsStatusList;

	public static void dataIntegrityCheck() throws IOException, SQLException {

		userStatusList = Users.getUserStatus();
		productList = UserProduct.getProductCurrentStatus();
		agreementList = AgreementService.getProductAgreements();
		agreementStatusList = AgreementSignDates.getAgreementStatus();
		approvalsStatusList = Approvals.getApprovalStatus();

		ArrayList<Long> datafeedusersList = new ArrayList<>(
				Users.getDataFeedUsers(String.valueOf(DataFirmId.NANEX.getId())));
		Collections.sort(datafeedusersList);

		ArrayList<Long> prousersList = new ArrayList<>(Users.getProUsers(String.valueOf(DataFirmId.NANEX.getId())));
		Collections.sort(prousersList);

		ArrayList<Long> nonprousersList = new ArrayList<>(
				Users.getNonProUsers(String.valueOf(DataFirmId.NANEX.getId())));
		Collections.sort(nonprousersList);

		ArrayList<ArrayList<Long>> userList = new ArrayList<>();
		userList.add(datafeedusersList);
		userList.add(prousersList);
		userList.add(nonprousersList);

		ArrayList<Product<Agreement>> temp;

		for (int counter = 0; counter < 3; counter++) {
			if (counter == 0)
				System.out.println("DataFeed Users: ");
			else if (counter == 1)
				System.out.println("Pro Users: ");
			else
				System.out.println("Non Pro Users: ");

			for (Long user : userList.get(counter)) {

				int userStatus = userStatusList.get(user);
				System.out.println("User with user_id: " + user + " has " + userStatusString(userStatus) + " status");

				if (counter == 0) {
					// Datafeed Users
					if (userStatus == UserStatus.Active.getStatus()) {

						try {
							ArrayList<Long> t = new ArrayList<>();
							HashMap<Long, Integer> products = productList.get(user);
							boolean flag = true;
							for (Long product : products.keySet()) {
								if (products.get(product) != ProductStatus.Access.getStatus()) {
									flag = false;
									t.add(product);
								}
							}
							if (!flag) {
								System.out.println("\tUser does n't have all products in active state");
								System.out.print("\t");
								for (Long j : t) {
									System.out.print(j + " ,");
								}
								System.out.println("\n");
							}

						} catch (NullPointerException e) {

						}

					}

				}

				if (userStatus == UserStatus.Cancelled.getStatus()) {

					try {
						ArrayList<Long> t = new ArrayList<>();
						HashMap<Long, Integer> products = productList.get(user);
						boolean flag = true;

						for (Long product : products.keySet()) {
							if (products.get(product) != ProductStatus.Cancelled.getStatus()) {
								flag = false;
								t.add(product);
							}
						}
						if (!flag) {
							System.out.println("\tUser does n't have all products in cancelled state");
							System.out.print("\t");
							for (Long j : t) {
								System.out.print(j + " ,");
							}
							System.out.println("\n");
						}

					} catch (NullPointerException e) {

					}

				}

				if (userStatus == UserStatus.WaitUserForm.getStatus()) {

					try {
						ArrayList<Long> t = new ArrayList<>();
						HashMap<Long, Integer> products = productList.get(user);
						boolean flag = true;

						for (Long product : products.keySet()) {
							if (products.get(product) != ProductStatus.Wait.getStatus()) {
								flag = false;
								t.add(product);
							}
						}
						if (!flag) {
							System.out.println("\tUser does n't have all products in waiting state");
							System.out.print("\t");
							for (Long j : t) {
								System.out.print(j + " ,");
							}
							System.out.println("\n");
						}

					} catch (NullPointerException e) {

					}

				}

				temp = productCheck(user, productList.get(user));

				if (temp != null && !temp.isEmpty()) {
					for (Product<Agreement> product : temp) {
						System.out.println("\tProduct with product_id: " + product.getProductId() + " has "
								+ product.getStatusString() + " status");
						try {
							for (Agreement agreement : product.getList()) {
								System.out.println(
										"\t\tAgreement with id: " + agreement.getAgreemenyId() + agreement.getStatus());
							}
						} catch (NullPointerException e) {

						}
						System.out.println();
					}

				}
				System.out.println("\n");
			}
			System.out.println("\n\n");
		}

	}

	public static ArrayList<Product<Agreement>> productCheck(Long user, HashMap<Long, Integer> products)
			throws IOException, SQLException {

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

	public static String userStatusString(int status) {
		for (UserStatus i : UserStatus.values()) {
			if (status == i.getStatus()) {
				return i.name();
			}
		}
		return "";
	}

}
