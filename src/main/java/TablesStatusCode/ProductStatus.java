package TablesStatusCode;

public enum ProductStatus {
	Wait(4), Access(3), Cancelled(6);

	private final int statusCode;

	ProductStatus(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatus() {
		return this.statusCode;
	}
}
