package TablesStatusCode;

public enum UserStatus {
	Remove(0), WaitUserForm(1), WaitForApproval(2), Disapprove(3), Active(4), Suspended(5), Cancelled(6);

	private final int statusCode;

	UserStatus(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

}
