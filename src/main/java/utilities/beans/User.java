package utilities.beans;

import java.util.HashSet;

public class User<T> {

	private Long userId;
	private Integer status;
	private HashSet<T> list;

	public User(Long userId, Integer status, HashSet<T> list) {
		this.userId = userId;
		this.status = status;
		this.list = list;
	}

	/**
	 * @return the user_id
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @return the products
	 */
	public HashSet<T> getList() {
		return list;
	}

	@Override
	public String toString() {
		String string = "User_id: " + userId + " with status " + status + " has :\n";
		for (T item : list) {
			string = string + item + "  ";
		}
		return string;
	}

}
