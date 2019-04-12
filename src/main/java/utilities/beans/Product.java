package utilities.beans;

import java.util.ArrayList;

import utilities.tablestatuscode.ProductStatus;

public class Product<T> {

	private Long productId;
	private Integer status;
	private ArrayList<T> list;

	public Product(Long productId, Integer status, ArrayList<T> list) {
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
	public ArrayList<T> getList() {
		return list;
	}
	
	public String getStatusString() {
		for(ProductStatus productStatus: ProductStatus.values()) {
			if(status==productStatus.getStatus()) {
				return productStatus.name();
			}
		}
		return null;
	}

}
