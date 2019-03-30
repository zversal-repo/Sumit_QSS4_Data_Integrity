
import java.io.IOException;
import java.sql.SQLException;

import Connection_DB.Connect;
import IntegrityChecks.DataIntegrityChecks;

public class Main {

	public static void main(String[] args) {

		try {
			
			DataIntegrityChecks.checkUnapprovedAgreementsHavingActiveProducts();

		} catch(SQLException | IOException e){
			System.out.println("Cannot complete method invocation due to I/O or SQL Issues.Details are below");
			e.printStackTrace();
		}finally {
			try {
				Connect.closeConnection();
			}
			catch(SQLException e) {
				/* do nothing */
			}
		}

	}
}
