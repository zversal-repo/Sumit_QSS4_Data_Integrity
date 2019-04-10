
import java.io.IOException;
import java.sql.SQLException;

import integritychecks.DataIntegrityChecks;
import integritychecks.NanexIntegrityCheck;
import utilities.Connect;

public class Main {

	public static void main(String[] args) {

		try {

			NanexIntegrityCheck.userIntegrityCheck();

		} catch (SQLException | IOException e) {
			System.out.println("Cannot complete method invocation due to I/O or SQL Issues.Details are below");
			e.printStackTrace();
		} finally {
			try {
				Connect.closeConnection();
			} catch (SQLException e) {
				/* do nothing */
			}
		}

	}
}
