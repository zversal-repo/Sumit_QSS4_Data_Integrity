package testclasses;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import utilities.Connect;

public class ConnectTest {

	@Test
	public void getConnection() throws SQLException, IOException {

		assertTrue(Connect.getConnection() instanceof Connection);
	}

}
