package Entity;

import java.util.ArrayList;

public class Product<T> {

	private Long productId;
	private Integer status;
	private ArrayList<T> list;
	
	public Product(Long productId, Integer status, ArrayList<T> list) {
		this.productId = productId;
		this.status = status;
		this.list=list;
	}
	/**
	 * @return the productId
	 */
	public Long getProductId() {
		return productId;
	}
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @return the services
	 */
	public ArrayList<T> getList() {
		return list;
	}

	
}
