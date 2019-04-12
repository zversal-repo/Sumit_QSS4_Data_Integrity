package utilities.beans;

import java.util.ArrayList;

public class Service {

	private Long serviceId;
	private ArrayList<Agreement> agreements;

	public Service(Long serviceId, ArrayList<Agreement> agreements) {

		this.serviceId = serviceId;
		this.agreements = agreements;
	}

	/**
	 * @return the serviceId
	 */
	public Long getServiceId() {
		return serviceId;
	}

	/**
	 * @return the agreements
	 */
	public ArrayList<Agreement> getAgreements() {
		return agreements;
	}

}
