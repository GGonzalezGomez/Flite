import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AllTestUnits {
	
	FliteTrakr ft;
	
	@BeforeClass public static void init() {
		FliteTrakr ft = new FliteTrakr();
	}

	@Test
	public void testConnectionStringIsNotEmpty() throws IOException {
		assertTrue("Connections String not empty", ft.getConnectionString("Connections: NUE-FRA-43, NUE-AMS-67, FRA-AMS-17, FRA-LHR-27, LHR-NUE-23").length>0);		
	}
	@Test
	public void testConnectionStringIsEqualValue() {
		if(ft.getConnectionString("Connections: NUE-FRA-43, NUE-AMS-67, FRA-AMS-17, FRA-LHR-27, LHR-NUE-23").length>0) {
			assertEquals("Connection string is not valid", "NUE-AMS-67", ft.getConnectionString("Connections: NUE-FRA-43, NUE-AMS-67, FRA-AMS-17, FRA-LHR-27, LHR-NUE-23")[1]);
		}
		else {
			fail("Empty connection string");
		}
	}
	@Test
	public void testConnectionStringSize() {
		assertEquals("Not all strings detected 1", 5, ft.getConnectionString("Connections: NUE-FRA-43,NUE-AMS-67, FRA-AMS-17, FRA-LHR-27, LHR-NUE-23").length);
		assertEquals("Not all strings detected 2", 5, ft.getConnectionString("Connections: NUE-FRA-43,NUE-AMS-67,FRA-AMS-17, FRA-LHR-27, LHR-NUE-23").length);
		assertEquals("Not all strings detected 3", 5, ft.getConnectionString("Connections: NUE-FRA-43,NUE-AMS-67,FRA-AMS-17,FRA-LHR-27, LHR-NUE-23").length);
		assertEquals("Not all strings detected 4", 5, ft.getConnectionString("Connections: NUE-FRA-43,NUE-AMS-67,FRA-AMS-17,FRA-LHR-27,LHR-NUE-23").length);
	}
	@Test
	public void testConnectionsExtracted() {
		String[] strconns = new String[4];
		strconns[0] = "NUE-FRA-43";
		strconns[1] = "FRA-LHR-27";
		strconns[2] = "LHR-NUE-23";
		strconns[3] = "NUE-AMS-67";
		List<String> expectedValue = Arrays.asList("FRA","AMS");
		List<String> obtainedValue = ft.getAirportConnections(strconns).get("NUE").destinations;
		expectedValue.sort(Comparator.naturalOrder());
		obtainedValue.sort(Comparator.naturalOrder());
		assertEquals("Destinations are not properly extracted", expectedValue,obtainedValue);
	}
	@Test
	public void testPriceConnection() {
		String[] strconns = new String[2];
		strconns[0] = "NUE-FRA-43";
		strconns[1] = "FRA-LHR-27";
		Integer obtainedValue = ft.getPrice("NUE-FRA-LHR", ft.getAirportConnections(strconns));
		Integer expectedValue = 70;
		assertEquals("Calculating Price of connection", expectedValue, obtainedValue);
	}

}
