package Entity;

public class Agreement {
	
	private Long agreemenyId;
	private String status;
	
	public Agreement(Long agreeementId,String status) {
		this.agreemenyId=agreeementId;
		this.status=status;
	}

	/**
	 * @return the agreemenyId
	 */
	public Long getAgreemenyId() {
		return agreemenyId;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	
	
	
}
