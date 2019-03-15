import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class MyFunctions {

	static Statement stmt = null;

	// Generic Functions

	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> getList(Connection conn, String query, String item, T args) throws SQLException {
		stmt = null;
		ArrayList<T> list = new ArrayList<>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				list.add((T) rs.getObject(item));
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		return list;
	}

	// Function for One-One Relationship
	@SuppressWarnings("unchecked")
	public static <T1, T2> HashMap<T1, T2> getOne_OneMap(Connection conn, String query, String item1, String item2,
			T1 arg1, T2 arg2) throws SQLException {
		stmt = null;
		HashMap<T1, T2> map = new HashMap<>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				T1 param1 = (T1) rs.getObject(item1);
				T2 param2 = (T2) rs.getObject(item2);

				map.put(param1, param2);
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		return map;
	}

	// Function for One-Many Relationship
	@SuppressWarnings("unchecked")
	public static <T1, T2> HashMap<T1, ArrayList<T2>> getMap(Connection conn, String query, String item1, String item2,
			T1 arg1, T2 arg2) throws SQLException {
		stmt = null;
		HashMap<T1, ArrayList<T2>> map = new HashMap<>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
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
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		return map;
	}

}
