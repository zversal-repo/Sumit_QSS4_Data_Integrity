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
import utilities.tablestatuscode.DataFirmId;
import utilities.tablestatuscode.ProductStatus;
import utilities.tablestatuscode.UserStatus;

public class NanexIntegrityCheck {

	public static void dataIntegrityCheck() throws IOException, SQLException {

		HashMap<Long, Integer> userStatusList = Users.getUserStatus();
		HashMap<Long, HashMap<Long, Integer>> productList = UserProduct.getProductCurrentStatus();
		HashMap<Long, HashSet<Long>> agreementList = AgreementService.getProductAgreements();
		HashMap<Long, HashMap<Long, Integer>> agreementStatusList = AgreementSignDates.getAgreementStatus();
		HashMap<Long, HashMap<Long, HashMap<Long, Boolean>>> approvalsStatusList = Approvals.getApprovalStatus();

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

							if (products.get(product) == ProductStatus.Access.getStatus()) {

								flag = false;
								t.add(product);
							}
						}
						if (!flag) {

							System.out.println(
									"\tUser does n't have all products in waiting state and List of the Active Products are:");

							System.out.print("\t");
							for (Long j : t) {
								System.out.print(j + " ,");
							}
							System.out.println("\n");
						}

					} catch (NullPointerException e) {

					}

				}

				temp = DataIntegrityChecks.productCheck(user, productList.get(user),agreementList,agreementStatusList,approvalsStatusList);

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

	public static String userStatusString(int status) {
		for (UserStatus i : UserStatus.values()) {
			if (status == i.getStatus()) {
				return i.name();
			}
		}
		return "";
	}

}
