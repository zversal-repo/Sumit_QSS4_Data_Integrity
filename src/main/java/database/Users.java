package database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import utilities.tablestatuscode.UserStatus;
import utilities.tablestatuscode.UserType;

public class Users {

	public static HashSet<Long> getUsers() throws IOException, SQLException {

		String query = "SELECT user_id FROM users ORDER BY user_id";
		return QueryProcessor.getList(query, "user_id", 1L);

	}

	public static HashSet<Long> getUsersForStatus(String status) throws IOException, SQLException {

		String query = "SELECT user_id FROM users WHERE status =" + status + " ORDER BY user_id";
		return QueryProcessor.getList(query, "user_id", 1L);
	}

	public static HashMap<Long, Integer> getUserStatus() throws IOException, SQLException {

		String query = "SELECT user_id,status FROM users ORDER BY user_id";
		return QueryProcessor.getOne_OneMap(query, "user_id", "status", 1L, 1);

	}

	public static HashMap<Long, Long> getUsersAndData_firm_id() throws IOException, SQLException {

		String query = "SELECT user_id,data_firm_id FROM users ORDER BY user_id";
		return QueryProcessor.getOne_OneMap(query, "user_id", "data_firm_id", 1L, 1L);
	}

	public static HashSet<Long> getUsersForData_firm_id(String data_firm_id) throws IOException, SQLException {

		String query = "SELECT user_id FROM users WHERE data_firm_id =" + data_firm_id + " ORDER BY user_id";
		return QueryProcessor.getList(query, "user_id", 1L);
	}

	public static HashMap<Long, HashSet<Long>> getDataFirmsAndUsers() throws IOException, SQLException {

		String query = "SELECT data_firm_id,user_id FROM users ORDER BY data_firm_id,user_id";
		return QueryProcessor.getMap(query, "data_firm_id", "user_id", 1L, 1L);
	}

	public static HashMap<Long, Integer> getUserStatusForData_firm_id(String data_firm_id)
			throws IOException, SQLException {
		String query = "SELECT user_id,status FROM users WHERE data_firm_id = " + data_firm_id + " ORDER BY user_id";
		return QueryProcessor.getOne_OneMap(query, "user_id", "status", 1L, 1);
	}

	public static HashSet<Long> getActiveUsersForData_firm_id(String data_firm_id) throws IOException, SQLException {
		String query = "SELECT user_id FROM users WHERE data_firm_id = " + data_firm_id + " AND STATUS ="
				+ UserStatus.Active.getStatus() + " ORDER BY user_id";
		return QueryProcessor.getList(query, "user_id", 1L);
	}

	public static HashSet<Long> getProUsers(String data_firm_id) throws SQLException, IOException {
		String query = "SELECT user_id FROM users WHERE data_firm_id = " + data_firm_id + " AND user_type ="
				+ UserType.PRO.getType() + " ORDER BY user_id";
		return QueryProcessor.getList(query, "user_id", 1L);
	}
	
	public static HashSet<Long> getNonProUsers(String data_firm_id) throws SQLException, IOException {
		String query = "SELECT user_id FROM users WHERE data_firm_id = " + data_firm_id + " AND user_type ="
				+ UserType.NONPRO.getType() + " ORDER BY user_id";
		return QueryProcessor.getList(query, "user_id", 1L);
	}
	
	public static HashSet<Long> getDataFeedUsers(String data_firm_id) throws SQLException, IOException {
		String query = "SELECT user_id FROM users WHERE data_firm_id = " + data_firm_id + " AND user_type ="
				+ UserType.DATAFEED.getType() + " ORDER BY user_id";
		return QueryProcessor.getList(query, "user_id", 1L);
	}
}
