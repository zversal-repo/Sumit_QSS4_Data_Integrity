package Entity;

import java.util.ArrayList;

public class User {

	private Long userId;
	private Integer status;
	private ArrayList<Long> products;
	
	public User(Long userId,Integer status,ArrayList<Long> products){
		this.userId=userId;
		this.status=status;
		this.products=products;
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
	public ArrayList<Long> getProducts() {
		return products;
	}
	
	@Override
	public String toString() {
		String string= "User_id: "+userId+" with status "+status+" has :\n";
		for(Long product:products) {
			string=string+product+"  ";
		}
		return string;
	}
	
	
	
	
}
