package Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

import Connection_DB.Connect;

//Generic Functions
//If any exception occurs while executing a query return a null
public class Utilities {

	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> getList(String query, String item, T args) throws IOException, SQLException {

		Connection conn = Connect.getConnection();
		ArrayList<T> list = new ArrayList<>();

		try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				list.add((T) rs.getObject(item));
			}

		} catch (SQLException e) {
			System.out.println(e.toString());
			return null;
		}

		return list;
	}

	// Function for One-One Relationship
	@SuppressWarnings("unchecked")
	public static <T1, T2> HashMap<T1, T2> getOne_OneMap(String query, String item1, String item2, T1 arg1, T2 arg2)
			throws IOException, SQLException {

		Connection conn = Connect.getConnection();
		HashMap<T1, T2> map = new HashMap<>();

		try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				T1 param1 = (T1) rs.getObject(item1);
				T2 param2 = (T2) rs.getObject(item2);

				map.put(param1, param2);
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
			return null;
		}

		return map;
	}

	// Function for One-Many Relationship
	@SuppressWarnings("unchecked")
	public static <T1, T2> HashMap<T1, ArrayList<T2>> getMap(String query, String item1, String item2, T1 arg1, T2 arg2)
			throws IOException, SQLException {

		Connection conn = Connect.getConnection();
		HashMap<T1, ArrayList<T2>> map = new HashMap<>();

		try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

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
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	public static <T1, T2, T3> HashMap<T1, HashMap<T2, T3>> getMap(String query, String item1, String item2,
			String item3, T1 arg1, T2 arg2, T3 arg3) throws IOException, SQLException {

		Connection conn = Connect.getConnection();
		HashMap<T1, HashMap<T2, T3>> map = new HashMap<>();

		try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
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
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getValue(String query, String item1, T arg1) throws IOException, SQLException {

		Connection conn = Connect.getConnection();
		T param1 = null;

		try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				param1 = (T) rs.getObject(item1);
			}

		} catch (SQLException e) {
			System.out.println(e.toString());
			return null;
		}

		return param1;
	}

}
