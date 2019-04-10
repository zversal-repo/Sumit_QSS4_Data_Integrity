package utilities.tablestatuscode;

public enum ProductStatus {
	Wait(4), Access(3), Cancelled(6);

	private final int status;

	ProductStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
