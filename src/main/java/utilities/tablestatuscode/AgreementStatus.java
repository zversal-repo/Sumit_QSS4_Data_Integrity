package utilities.tablestatuscode;

public enum AgreementStatus {
	Accepted(0), Approved(1), Disapproved(2);

	private final int status;

	private AgreementStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
	

}
