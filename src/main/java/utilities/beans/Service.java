package utilities.beans;

import java.util.HashSet;

public class Service {

	private Long serviceId;
	private HashSet<Agreement> agreements;

	public Service(Long serviceId, HashSet<Agreement> agreements) {

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
	public HashSet<Agreement> getAgreements() {
		return agreements;
	}

}
