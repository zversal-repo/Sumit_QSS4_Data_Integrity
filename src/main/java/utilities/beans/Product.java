package utilities.beans;

import java.util.HashSet;

public class Product<T> {

	private Long productId;
	private Integer status;
	private HashSet<T> list;

	public Product(Long productId, Integer status, HashSet<T> list) {
		this.productId = productId;
		this.status = status;
		this.list = list;
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
	public HashSet<T> getList() {
		return list;
	}

}
