package Entity;

import java.util.ArrayList;

public class UserAndProduct {
	
	private Long userId;
	private Integer status;
	private ArrayList<Product> products;
	
	public UserAndProduct(Long userId,Integer status,ArrayList<Product> products){
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
	public ArrayList<Product> getProducts() {
		return products;
	}

}
