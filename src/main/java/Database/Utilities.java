package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

//Generic Functions
//If any exception occurs while executing a query return a null
public class Utilities {

	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> getList(Connection conn, String query, String item, T args) {
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<T> list = new ArrayList<>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				list.add((T) rs.getObject(item));
			}

		} catch (SQLException e) {
			System.out.println(e.toString());
			return null;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* ignored */}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println(e.toString());
				}
			}
		}

		return list;
	}

	// Function for One-One Relationship
	@SuppressWarnings("unchecked")
	public static <T1, T2> HashMap<T1, T2> getOne_OneMap(Connection conn, String query, String item1, String item2,
			T1 arg1, T2 arg2) {
		Statement stmt = null;
		ResultSet rs = null;
		HashMap<T1, T2> map = new HashMap<>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				T1 param1 = (T1) rs.getObject(item1);
				T2 param2 = (T2) rs.getObject(item2);

				map.put(param1, param2);
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
			return null;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* ignored */}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println(e.toString());
				}
			}
		}

		return map;
	}

	// Function for One-Many Relationship
	@SuppressWarnings("unchecked")
	public static <T1, T2> HashMap<T1, ArrayList<T2>> getMap(Connection conn, String query, String item1, String item2,
			T1 arg1, T2 arg2) {
		Statement stmt = null;
		ResultSet rs = null;
		HashMap<T1, ArrayList<T2>> map = new HashMap<>();
		try {
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

		} catch (SQLException e) {
			System.out.println(e.toString());
			return null;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* ignored */}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println(e.toString());
				}
			}
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	public static <T1, T2, T3> HashMap<T1, HashMap<T2, T3>> getMap(Connection conn, String query, String item1,
			String item2, String item3, T1 arg1, T2 arg2, T3 arg3) {
		Statement stmt = null;
		ResultSet rs = null;
		HashMap<T1, HashMap<T2, T3>> map = new HashMap<>();
		try {
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

		} catch (SQLException e) {
			System.out.println(e.toString());
			return null;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* ignored */}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println(e.toString());
				}
			}
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getValue(Connection conn, String query, String item1, T arg1) {
		Statement stmt = null;
		ResultSet rs = null;
		T param1 = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				param1 = (T) rs.getObject(item1);
			}

		} catch (SQLException e) {
			System.out.println(e.toString());
			return null;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* ignored */}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println(e.toString());
				}
			}
		}

		return param1;
	}

}
