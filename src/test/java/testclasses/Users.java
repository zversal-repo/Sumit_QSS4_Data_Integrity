package testclasses;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

import database.QueryProcessor;
import utilities.tablestatuscode.UserStatus;

public class Users {
	
	
		@Test
		public void getUsers() throws IOException, SQLException {
			assertTrue(database.Users.getUsers() instanceof HashSet);

		}

		@Test
		public void getUsers2() throws IOException, SQLException {
			assertTrue(database.Users.getUsersForStatus("4") instanceof HashSet);

		}
		
		@Test
		public void getUserStatus() throws IOException, SQLException {
			assertTrue(database.Users.getUserStatus() instanceof HashMap);

		}

		@Test
		public void getUsersAndData_firm_id() throws IOException, SQLException {
			assertTrue(database.Users.getUsersAndData_firm_id() instanceof HashMap);

		}

		@Test
		public void getUsersAndData_firm_id2() throws IOException, SQLException {
			assertTrue(database.Users.getUsersForData_firm_id("3") instanceof HashSet);

		}

		@Test
		public void getUsersAndData_firm_id3() throws IOException, SQLException {
			assertTrue(database.Users.getDataFirmsAndUsers() instanceof HashMap);

		}

		@Test
		public void getUsersAndData_firm_id4() throws IOException, SQLException {
			assertTrue(database.Users.getUserStatusForData_firm_id("3") instanceof HashMap);

		}

		@Test
		public void getUsersAndData_firm_id5() throws IOException, SQLException {
			assertTrue(database.Users.getActiveUsersForData_firm_id("3") instanceof HashSet);

		}


}
