package utilities.beans;

import java.util.ArrayList;

public class User<T> {

	private Long userId;
	private Integer status;
	private ArrayList<T> list;

	public User(Long userId, Integer status, ArrayList<T> list) {
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
	public ArrayList<T> getList() {
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
