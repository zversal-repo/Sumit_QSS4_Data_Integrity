package Database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import TablesStatusCode.UserStatus;

//Functions return null if Exception occurs during query execution
public class Users {

	// Function for getting all users in the database
	public static ArrayList<Long> getUsers(Connection conn) {

		String query = "SELECT user_id FROM users ORDER BY user_id";
		return Utilities.getList(conn, query, "user_id", 1L);

	}

	// Function for getting all users and their status
	public static HashMap<Long, Integer> getUserStatus(Connection conn) {

		String query = "SELECT user_id,status FROM users ORDER BY user_id";
		return Utilities.getOne_OneMap(conn, query, "user_id", "status", 1L, 1);

	}

	// Function for getting all users for particular user status
	public static ArrayList<Long> getUsers(Connection conn, String status) {

		String query = "SELECT user_id FROM users WHERE status =" + status + " ORDER BY user_id";
		return Utilities.getList(conn, query, "user_id", 1L);
	}

	// Function for getting all users and their data_firm_id
	public static HashMap<Long, Long> getUsersAndData_firm_id(Connection conn) {

		String query = "SELECT user_id,data_firm_id FROM users ORDER BY user_id";
		return Utilities.getOne_OneMap(conn, query, "user_id", "data_firm_id", 1L, 1L);
	}

	// Function for getting all users for particular data_firm_id
	public static ArrayList<Long> getUsersForData_firm_id(Connection conn, String data_firm_id) {

		String query = "SELECT user_id FROM users WHERE data_firm_id =" + data_firm_id + " ORDER BY user_id";
		return Utilities.getList(conn, query, "user_id", 1L);
	}

	// Function for getting all users of the every data_firm
	public static HashMap<Long, ArrayList<Long>> getDataFirmsAndUsers(Connection conn) {

		String query = "SELECT data_firm_id,user_id FROM users ORDER BY data_firm_id,user_id";
		return Utilities.getMap(conn, query, "data_firm_id", "user_id", 1L, 1L);
	}

	// Function for getting user status for a particular data_firm in the database
	public static HashMap<Long, Integer> getUserStatusForData_firm_id(Connection conn, String data_firm_id) {
		String query = "SELECT user_id,status FROM users WHERE data_firm_id = " + data_firm_id + " ORDER BY user_id";
		return Utilities.getOne_OneMap(conn, query, "user_id", "status", 1L, 1);
	}
	
	// Function for getting only active users for data_firm in the database
	public static ArrayList<Long> getActiveUsersForData_firm_id(Connection conn, String data_firm_id) {
		String query = "SELECT user_id FROM users WHERE data_firm_id = " + data_firm_id + " AND STATUS ="
				+ UserStatus.Active.getStatus() + " ORDER BY user_id";
		return Utilities.getList(conn, query, "user_id", 1L);
	}

}
