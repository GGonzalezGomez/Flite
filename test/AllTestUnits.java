import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class AllTestUnits {

	@Test
	public void testConnectionStringIsNotEmpty() throws IOException {
		FliteTrakr ft = new FliteTrakr();
		assertTrue("Connections String not empty", ft.getConnectionString("Connections: NUE-FRA-43, NUE-AMS-67, FRA-AMS-17, FRA-LHR-27, LHR-NUE-23").length>0);		
	}
	@Test
	public void testConnectionStringIsEqualValue() {
		FliteTrakr ft = new FliteTrakr();
		if(ft.getConnectionString("Connections: NUE-FRA-43, NUE-AMS-67, FRA-AMS-17, FRA-LHR-27, LHR-NUE-23").length>0) {
			assertEquals("Connection string is not valid", "NUE-AMS-67", ft.getConnectionString("Connections: NUE-FRA-43, NUE-AMS-67, FRA-AMS-17, FRA-LHR-27, LHR-NUE-23")[1]);
		}
		else {
			fail("Empty connection string");
		}
	}
	@Test
	public void testConnectionStringSize() {
		FliteTrakr ft = new FliteTrakr();
		assertEquals("Not all strings detected 1", 5, ft.getConnectionString("Connections: NUE-FRA-43,NUE-AMS-67, FRA-AMS-17, FRA-LHR-27, LHR-NUE-23").length);
		assertEquals("Not all strings detected 2", 5, ft.getConnectionString("Connections: NUE-FRA-43,NUE-AMS-67,FRA-AMS-17, FRA-LHR-27, LHR-NUE-23").length);
		assertEquals("Not all strings detected 3", 5, ft.getConnectionString("Connections: NUE-FRA-43,NUE-AMS-67,FRA-AMS-17,FRA-LHR-27, LHR-NUE-23").length);
		assertEquals("Not all strings detected 4", 5, ft.getConnectionString("Connections: NUE-FRA-43,NUE-AMS-67,FRA-AMS-17,FRA-LHR-27,LHR-NUE-23").length);
	}
	@Test
	public void testConnectionsExtracted() {
		FliteTrakr ft = new FliteTrakr();
		String[] strconns = new String[1];
		strconns[0] = "NUE-FRA-43";
		assertEquals("Destination expected FRA", "FRA", (ft.getConnections(strconns)[0].destination));
		assertEquals("Source expected NUE", "NUE", (ft.getConnections(strconns)[0].source));
		assertEquals("Cost expected 43", 43, (ft.getConnections(strconns)[0].cost));
	}
	@Test
	public void testPriceConnection() {
		FliteTrakr ft = new FliteTrakr();
		String[] strconns = new String[2];
		strconns[0] = "NUE-FRA-43";
		strconns[1] = "FRA-LHR-27";
		assertEquals("Incorrect Price of connection", 70, ft.getPriceConnection(ft.getConnections(strconns),"NUE-FRA-LHR"));
	}

}
