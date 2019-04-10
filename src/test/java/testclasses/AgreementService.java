package testclasses;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

public class AgreementService {

	@Test
	public void getAgreements() throws IOException, SQLException {
		assertTrue(database.AgreementService.getAgreements() instanceof HashMap);

	}

	@Test
	public void getAgreements2() throws IOException, SQLException {
		assertTrue(database.AgreementService.getAgreements("301") instanceof HashSet);

	}

	@Test
	public void getProductAgreements() throws IOException, SQLException {
		assertTrue(database.AgreementService.getProductAgreements() instanceof HashMap);

	}

}
