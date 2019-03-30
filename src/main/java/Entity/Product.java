package Entity;

import java.util.ArrayList;

public class Product {

	private Long productId;
	private Integer status;
	private ArrayList<Service> services;
	
	public Product(Long productId, Integer status, ArrayList<Service> services) {
		this.productId = productId;
		this.status = status;
		this.services = services;
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
	public ArrayList<Service> getServices() {
		return services;
	}

	
}
