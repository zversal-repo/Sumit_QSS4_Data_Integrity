package utilities.tablestatuscode;

public enum DataFirmId {

	KARMA(3L), NANEX(4L);

	private final long firm_id;

	private DataFirmId(long firm_id) {
		this.firm_id = firm_id;
	}

	public Long getId() {
		return firm_id;
	}

}
