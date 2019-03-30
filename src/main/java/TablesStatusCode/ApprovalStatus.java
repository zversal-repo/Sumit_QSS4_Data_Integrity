package TablesStatusCode;

public enum ApprovalStatus {
	WAIT_FOR_AGREEMENT(false), WAIT_FOR_APPROVAL(true);

	private final boolean statusCode;

	private ApprovalStatus(boolean statusCode) {
		this.statusCode = statusCode;
	}

	public boolean getStatus(){
		return statusCode;
	}
	
}
