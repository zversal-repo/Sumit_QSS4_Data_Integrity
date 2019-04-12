package utilities.tablestatuscode;

public enum UserStatus {
	Remove(0), WaitUserForm(1), WaitForApproval(2), Disapprove(3), Active(4), Suspended(5), Cancelled(6);

	private final int status;

	UserStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
	
	public String getStatusString(int status) {
		for(UserStatus userStatus:UserStatus.values())
			if(status==userStatus.getStatus())
				return userStatus.name();
		return null;
	}

}
