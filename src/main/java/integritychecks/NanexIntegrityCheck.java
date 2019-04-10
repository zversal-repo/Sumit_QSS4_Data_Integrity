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
import utilities.tablestatuscode.DataFirmId;
import utilities.tablestatuscode.ProductStatus;

public class NanexIntegrityCheck {

	public static void userIntegrityCheck() throws SQLException, IOException {

		ArrayList<User<Product<Agreement>>> list = new ArrayList<>();

		HashMap<Long, Integer> userStatusList = Users.getUserStatus();
		HashSet<Long> nonprousersList = Users.getNonProUsers(String.valueOf(DataFirmId.NANEX.getId()));
		HashSet<Long> prousersList = Users.getProUsers(String.valueOf(DataFirmId.NANEX.getId()));
		// HashSet<Long> datafeedusers =
		// Users.getDataFeedUsers(String.valueOf(DataFirmId.NANEX.getId()));
		HashMap<Long, HashMap<Long, Integer>> productList = UserProduct.getProductCurrentStatus();
		HashMap<Long, HashSet<Long>> agreementList = AgreementService.getProductAgreements();
		HashMap<Long, HashMap<Long, Integer>> agreementStatusList = AgreementSignDates.getAgreementStatus();

		for (Long user : prousersList) {
			try {
				HashMap<Long, Integer> products = productList.get(user);

				HashSet<Product<Agreement>> temp = new HashSet<>();

				for (Long product : products.keySet()) {

					Integer productStatus = products.get(product);
					HashSet<Agreement> temp1 = new HashSet<>();

					Integer agreementStatus = ((productStatus == ProductStatus.Access.getStatus())
							? AgreementStatus.Approved.getStatus()
							: ((productStatus == ProductStatus.Cancelled.getStatus())
									? AgreementStatus.Disapproved.getStatus()
									: AgreementStatus.Accepted.getStatus()));

					try {
						for (Long agreement : agreementList.get(product)) {

							try {
								Integer status = agreementStatusList.get(user).get(agreement);

								if (status!=null && status!= agreementStatus) {
									temp1.add(new Agreement(agreement, String.valueOf(status)));
								}
								

							} catch (NullPointerException e) {
								//e.printStackTrace();
							}
						}
					} catch (NullPointerException e) {
						//e.printStackTrace();
					}
					if (!temp1.isEmpty()) {
						temp.add(new Product<Agreement>(product, productStatus, temp1));
					}

				}
				if (!temp.isEmpty()) {
					list.add(new User<Product<Agreement>>(user, userStatusList.get(user), temp));
				}
			} catch (NullPointerException e) {
				//e.printStackTrace();
			}
		}

		if (!list.isEmpty()) {
			for (User<Product<Agreement>> user : list) {
				System.out.println(user.getUserId() + " " + user.getStatus());
				System.out.println();
				for (Product<Agreement> product : user.getList()) {
					System.out.println("\t" + product.getProductId() + " " + product.getStatus());
					System.out.println();
					for (Agreement agreement : product.getList()) {
						System.out.println("\t\t" + agreement.getAgreemenyId() + " " + agreement.getStatus());
					}
					System.out.println();
				}
				System.out.println();
			}
		} else {
			System.out.println("No Result");
		}

	}

}
