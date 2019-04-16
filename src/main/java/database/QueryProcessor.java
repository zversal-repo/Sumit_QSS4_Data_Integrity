package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

import utilities.Connect;

//Generic Functions
//If any exception occurs while executing a query return a null
public class QueryProcessor {

	private static Connection conn;
	private static Statement stmt;
	private static ResultSet rs;

	@SuppressWarnings("unchecked")
	public static <T> HashSet<T> getList(String query, String item, T args) throws SQLException, IOException {

		HashSet<T> list = new HashSet<>();

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
	public static <T1, T2> HashMap<T1, HashSet<T2>> getMap(String query, String item1, String item2, T1 arg1, T2 arg2)
			throws IOException, SQLException {

		HashMap<T1, HashSet<T2>> map = new HashMap<>();

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
					HashSet<T2> list = new HashSet<>();
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
	public static <T1, T2, T3, T4> HashMap<T1, HashMap<T2, HashMap<T3, T4>>> getMap(String query, String item1,
			String item2, String item3, String item4, T1 arg1, T2 arg2, T3 arg3, T4 arg4)
			throws IOException, SQLException {

		HashMap<T1, HashMap<T2, HashMap<T3, T4>>> map = new HashMap<>();

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
					if (map.get(param1).containsKey(param2)) {

						map.get(param1).get(param2).put(param3, param4);
					} else {
						HashMap<T3, T4> list = new HashMap<>();
						list.put(param3, param4);
						map.get(param1).put(param2, list);
					}
				} else {
					HashMap<T2, HashMap<T3, T4>> list = new HashMap<>();
					HashMap<T3, T4> temp = new HashMap<>();
					temp.put(param3, param4);
					list.put(param2, temp);
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
}
