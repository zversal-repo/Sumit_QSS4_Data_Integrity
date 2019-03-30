package Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import Connection_DB.Connect;

//Generic Functions
//If any exception occurs while executing a query return a null
public class Utilities {

	private static Connection conn;
	private static Statement stmt;
	private static ResultSet rs;

	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> getList(String query, String item, T args) throws SQLException, IOException {

		ArrayList<T> list = new ArrayList<>();

		try {
			conn = Connect.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				list.add((T) rs.getObject(item));
			}

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* do nothing */
				}

			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					/* do nothing */
				}
			}
		}

		return list;
	}

	// Function for One-One Relationship
	@SuppressWarnings("unchecked")
	public static <T1, T2> HashMap<T1, T2> getOne_OneMap(String query, String item1, String item2, T1 arg1, T2 arg2)
			throws IOException, SQLException {

		HashMap<T1, T2> map = new HashMap<>();

		try {
			conn = Connect.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				T1 param1 = (T1) rs.getObject(item1);
				T2 param2 = (T2) rs.getObject(item2);

				map.put(param1, param2);
			}

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* do nothing */
				}

			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					/* do nothing */
				}
			}
		}

		return map;
	}

	// Function for One-Many Relationship
	@SuppressWarnings("unchecked")
	public static <T1, T2> HashMap<T1, ArrayList<T2>> getMap(String query, String item1, String item2, T1 arg1, T2 arg2)
			throws IOException, SQLException {

		HashMap<T1, ArrayList<T2>> map = new HashMap<>();

		try {
			conn = Connect.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				T1 param1 = (T1) rs.getObject(item1);
				T2 param2 = (T2) rs.getObject(item2);

				if (map.containsKey(param1)) {
					map.get(param1).add(param2);
				} else {
					ArrayList<T2> list = new ArrayList<>();
					list.add(param2);
					map.put(param1, list);
				}
			}

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* do nothing */
				}

			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					/* do nothing */
				}
			}
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	public static <T1, T2, T3> HashMap<T1, HashMap<T2, T3>> getMap(String query, String item1, String item2,
			String item3, T1 arg1, T2 arg2, T3 arg3) throws IOException, SQLException {

		HashMap<T1, HashMap<T2, T3>> map = new HashMap<>();

		try {
			conn = Connect.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				T1 param1 = (T1) rs.getObject(item1);
				T2 param2 = (T2) rs.getObject(item2);
				T3 param3 = (T3) rs.getObject(item3);

				if (map.containsKey(param1)) {
					map.get(param1).put(param2, param3);
				} else {
					HashMap<T2, T3> list = new HashMap<>();
					list.put(param2, param3);
					map.put(param1, list);
				}
			}
			

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* do nothing */
				}

			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					/* do nothing */
				}
			}
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	public static <T1, T2, T3, T4> HashMap<T1, HashMap<T2, HashMap<T3, ArrayList<T4>>>> getMap(String query, String item1,
			String item2, String item3, String item4, T1 arg1, T2 arg2, T3 arg3, T4 arg4)
			throws SQLException, IOException {

		HashMap<T1, HashMap<T2, HashMap<T3, ArrayList<T4>>>> map = new HashMap<>();

		try {
			conn = Connect.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				T1 param1 = (T1) rs.getObject(item1);
				T2 param2 = (T2) rs.getObject(item2);
				T3 param3 = (T3) rs.getObject(item3);
				T4 param4 = (T4) rs.getObject(item4);

				if (map.containsKey(param1)) {
					if ((map.get(param1).containsKey(param2))) {
						if (map.get(param1).get(param2).containsKey(param3)) {

							map.get(param1).get(param2).get(param3).add(param4);

						} else {
							ArrayList<T4> temp = new ArrayList<>();
							temp.add(param4);

							map.get(param1).get(param2).put(param3, temp);
						}
					} else {
						ArrayList<T4> temp1 = new ArrayList<>();
						temp1.add(param4);
						HashMap<T3, ArrayList<T4>> temp2 = new HashMap<>();
						temp2.put(param3, temp1);
						map.get(param1).put(param2, temp2);
					}
				} else {
					ArrayList<T4> temp1 = new ArrayList<>();
					temp1.add(param4);
					HashMap<T3, ArrayList<T4>> temp2 = new HashMap<>();
					temp2.put(param3, temp1);
					HashMap<T2, HashMap<T3, ArrayList<T4>>> temp3 = new HashMap<>();
					temp3.put(param2, temp2);
					map.put(param1, temp3);
				}
			}

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* do nothing */
				}

			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					/* do nothing */
				}
			}
		}

		return map;

	}

	@SuppressWarnings("unchecked")
	public static <T> T getValue(String query, String item1, T arg1) throws IOException, SQLException {

		T param1 = null ;

		try {
			conn = Connect.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				param1 = (T) rs.getObject(item1);
			}

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* do nothing */
				}

			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					/* do nothing */
				}
			}
		}

		return param1;
	}

}
