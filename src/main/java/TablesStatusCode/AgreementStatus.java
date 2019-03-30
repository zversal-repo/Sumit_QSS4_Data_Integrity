package TablesStatusCode;

public enum AgreementStatus {
	Accepted(0),
	Approved(1),
	Disapproved(2);
	
	private final int statusCode;
	
	private AgreementStatus(int status){
		this.statusCode=status;
	}
	
	public int getStatus(){
		return statusCode;
	}

}
