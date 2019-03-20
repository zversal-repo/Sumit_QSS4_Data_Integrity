package TablesStatusCode;

public enum UserProductStatus {
	Wait(4), Access(3), Cancelled(6);

	private final int statusCode;

	UserProductStatus(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatus() {
		return this.statusCode;
	}
}
