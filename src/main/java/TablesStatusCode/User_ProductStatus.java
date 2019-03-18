package TablesStatusCode;

public enum User_ProductStatus {
	Wait(4), Access(3), Cancelled(6);

	private final int statusCode;

	User_ProductStatus(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return this.statusCode;
	}
}
