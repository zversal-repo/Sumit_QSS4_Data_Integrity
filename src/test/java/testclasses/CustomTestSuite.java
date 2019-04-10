package testclasses;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({ ConnectTest.class, AgreementService.class, Users.class })
public class CustomTestSuite {

}
