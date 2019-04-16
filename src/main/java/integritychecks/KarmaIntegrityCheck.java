package integritychecks;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import utilities.tablestatuscode.DataFirmId;
import utilities.tablestatuscode.ProductStatus;
import utilities.tablestatuscode.UserStatus;

public class KarmaIntegrityCheck {

	public static ArrayList<User<Long>> checkForKarmaUsers() throws IOException, SQLException {

		ArrayList<User<Long>> list = new ArrayList<>();

		ArrayList<Long> mandatoryProducts = new ArrayList<>(Arrays.asList(1L, 2L, 7L, 8L));

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

	public static void dataIntegrityCheck() throws IOException, SQLException {

		HashMap<Long, Integer> userStatusList = Users.getUserStatus();
		HashMap<Long, HashMap<Long, Integer>> productList = UserProduct.getProductCurrentStatus();
		HashMap<Long, HashSet<Long>> agreementList = AgreementService.getProductAgreements();
		HashMap<Long, HashMap<Long, Integer>> agreementStatusList = AgreementSignDates.getAgreementStatus();
		HashMap<Long, HashMap<Long, HashMap<Long, Boolean>>> approvalsStatusList = Approvals.getApprovalStatus();

		ArrayList<Long> userList = new ArrayList<>(
				Users.getUsersForData_firm_id(String.valueOf(DataFirmId.KARMA.getId())));
		Collections.sort(userList);

		ArrayList<Long> mandatoryProducts = new ArrayList<>(Arrays.asList(1L, 2L, 7L, 8L));

		ArrayList<Product<Agreement>> temp = new ArrayList<>();

		for (Long user : userList) {

			int userStatus = userStatusList.get(user);
			System.out.println("User with user_id: " + user + " has " +
			userStatusString(userStatus) + " status");

			if (userStatus == UserStatus.Active.getStatus()) {
				
				try {
					HashSet<Long> activeProductsList = UserProduct.getActiveProductsForUser_id(String.valueOf(user));
					if (!activeProductsList.containsAll(mandatoryProducts)) {
						System.out.println("User with user_id:" + user + "does not have all mandatory products");
						ArrayList<Long> temp1 = new ArrayList<>(mandatoryProducts);
						temp1.removeAll(activeProductsList);
						System.out.print("\t");
						for (Long i : temp1) {
							System.out.print(i + ",");
						}
						System.out.println();
					}
				} catch (NullPointerException e) {
					System.out.println("User with user_id:" + user + "does not have all mandatory products");
					System.out.print("\t");
					for (Long i : mandatoryProducts) {
						System.out.print(i + ",");
					}
					System.out.println();
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
						System.out.println("\tUser does n't have all products in waiting state and List of the Active Products are:");

						System.out.print("\t");
						for (Long j : t) {
							System.out.print(j + " ,");
						}
						System.out.println("\n");
					}

				} catch (NullPointerException e) {

				}

			}

			temp = DataIntegrityChecks.productCheck(user, productList.get(user),agreementList,agreementStatusList,approvalsStatusList );

			if (temp != null && !temp.isEmpty()) {
				for (Product<Agreement> product : temp) {
					
					System.out.println("\tProduct with product_id: " + product.getProductId() + " has " + product.getStatusString() + " status");
					try {
						for (Agreement agreement : product.getList()) {
							
							 System.out.println(
							 "\t\tAgreement with id: " + agreement.getAgreemenyId() +
							agreement.getStatus());
						}
					} catch (NullPointerException e) {

					}
					System.out.println();
				}

			}
			System.out.println("\n");
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
