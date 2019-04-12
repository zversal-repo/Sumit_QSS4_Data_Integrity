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

	public static void dataFeedUserIntegrityCheck() throws SQLException, IOException {

		userStatusList = Users.getUserStatus();
		productList = UserProduct.getProductCurrentStatus();
		agreementList = AgreementService.getProductAgreements();
		agreementStatusList = AgreementSignDates.getAgreementStatus();
		approvalsStatusList = Approvals.getApprovalStatus();

		ArrayList<Long> datafeedusersList = new ArrayList<>(
				Users.getDataFeedUsers(String.valueOf(DataFirmId.NANEX.getId())));
		Collections.sort(datafeedusersList);

		for (Long user : datafeedusersList) {

			int userStatus = userStatusList.get(user);

			int i = -1;
			String string = "";
			if (userStatus == UserStatus.Active.getStatus()) {
				i = ProductStatus.Access.getStatus();
				string = "Active";
			} else if (userStatus == UserStatus.Cancelled.getStatus()) {
				i = ProductStatus.Cancelled.getStatus();
				string = "Cancelled";
			}

			System.out.println("User with user_id: " + user + " has status " + userStatus);

			if (userStatus == UserStatus.Active.getStatus() || userStatus == UserStatus.Cancelled.getStatus()) {

				try {
					ArrayList<Long> t = new ArrayList<>();
					HashMap<Long, Integer> products = productList.get(user);
					boolean flag = true;
					for (Long product : products.keySet()) {
						if (products.get(product) != i) {
							flag = false;
							t.add(product);
						}
					}
					if (!flag) {
						System.out.println("\tUser does n't have all products " + string);
						System.out.print("\t");
						for (Long j : t) {
							System.out.print(j + " ,");
						}
						System.out.println("\n");
					}

				} catch (NullPointerException e) {

				}

			}

			ArrayList<Product<Agreement>> temp = check(user, productList.get(user));
			if (!temp.isEmpty()) {
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

	}

	public static ArrayList<Product<Agreement>> check(Long user, HashMap<Long, Integer> products)
			throws IOException, SQLException {

		ArrayList<Product<Agreement>> list = new ArrayList<>();

		try {
			for (Long product : products.keySet()) {

				Integer productStatus = products.get(product);
				ArrayList<Agreement> temp1 = new ArrayList<>();

				if (productStatus == ProductStatus.Wait.getStatus()) {

					list.add(new Product<Agreement>(product, productStatus, null));

				} else {
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
							}
						}
					} catch (NullPointerException e) {
						// e.printStackTrace();
					}

				}
				if (!temp1.isEmpty()) {
					list.add(new Product<Agreement>(product, productStatus, temp1));
				}

			}
		} catch (NullPointerException e) {

		}
		return list;
	}

	
}
