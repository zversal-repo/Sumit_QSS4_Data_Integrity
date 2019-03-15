import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Users {

	// Function for getting all users in the database
	public static ArrayList<Long> getAllUsers(Connection conn) throws SQLException {

		String query = "SELECT user_id FROM users ORDER BY user_id";

		return MyFunctions.getList(conn, query, "user_id", 1L);
	}

	// Function for getting all users for particular data_firm_id
	public static ArrayList<Long> getAllUsersOfDataFirm(Connection conn, String data_firm_id) throws SQLException {

		String query = "SELECT user_id FROM users WHERE data_firm_id =" + data_firm_id + " ORDER BY user_id";

		return MyFunctions.getList(conn, query, "user_id", 1L);
	}

	// Function for getting all users for particular user status
	public static ArrayList<Long> getStatusOfUsers(Connection conn, String status) throws SQLException {

		String query = "SELECT user_id FROM users WHERE status =" + status + " ORDER BY user_id";

		return MyFunctions.getList(conn, query, "user_id", 1L);
	}

	// Function for getting all products of a user
	public static ArrayList<Long> getAllProductsOfUser(Connection conn, String user_id) throws SQLException {

		String query = "SELECT DISTINCT product_id FROM user_product WHERE user_id =" + user_id
				+ " ORDER BY product_id";

		return MyFunctions.getList(conn, query, "product_id", 1L);
	}

	

	// Function for getting all products for every user
	public static HashMap<Long, ArrayList<Long>> getUsersWithProducts(Connection conn) throws SQLException {

		String query = "SELECT DISTINCT user_id,product_id FROM user_product";

		return MyFunctions.getMap(conn, query, "user_id", "product_id", 1L, 1L);

	}

	// Function for getting all users of the every data_firm
	public static HashMap<Long, ArrayList<Long>> getDataFirmsUsers(Connection conn) throws SQLException {

		String query = "SELECT data_firm_id,user_id FROM users ORDER BY data_firm_id,user_id";

		return MyFunctions.getMap(conn, query, "data_firm_id", "user_id", 1L, 1L);

	}

	// Function for getting all services comprising diff products
	public static HashMap<Long, ArrayList<Long>> getProductsServices(Connection conn) throws SQLException {

		String query = "SELECT product_id,service_id FROM product_service ORDER BY product_id ASC,service_id ASC";

		return MyFunctions.getMap(conn, query, "product_id", "service_id", 1L, 1L);

	}

}
