package utilities.tablestatuscode;

public enum UserType {
	NONPRO(0), PRO(1), DATAFEED(2);

	private final int type;

	private UserType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
