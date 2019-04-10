package utilities.tablestatuscode;

public enum ApprovalStatus {
	WAIT_FOR_AGREEMENT(false), WAIT_FOR_APPROVAL(true);

	private final boolean status;

	private ApprovalStatus(boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return status;
	}

}
