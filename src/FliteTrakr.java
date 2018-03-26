import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FliteTrakr {
	 
	public static void main(String[] args) throws IOException {

		List<String> inputCad = new ArrayList<String>();
		if(args.length==0) {
			String tmp = "";
			try (InputStreamReader isr = new InputStreamReader(System.in)) {
				int ch;
				while((ch = isr.read()) != -1) {
					if((char)ch=='\n') {
						inputCad.add(tmp);
						tmp = "";
					}
					else {
						tmp += (char)ch;
					}
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		else {
			if(args.length==1) {
				inputCad= readInputTextFile(args[0]);
			}
			else {
				System.out.println("Usage: java -jar app.jar input.txt or cat input.txt | java -jar app.jar");
				System.exit(1);
			}
		}
		//Work with the input data
		String[] strConnections = getConnectionString(inputCad.get(0));
		FlightConnection[] flightConnections = getConnections(strConnections);
		int cst = getPriceConnection(flightConnections, "NUE-FRA-LHR");
		
	}
	
	private static List<String> readInputTextFile(String fileName) throws IOException {
	    return Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
	  }
	
	public static String[] getConnectionString(String initialString) {
		String[] conns = {};
		
		if(initialString.matches("^(Connections: \\D{3}-\\D{3}-\\d+)(,\\s?\\D{3}-\\D{3}-\\d+)*")){
			String instring = initialString.split("Connections:")[1].trim();
			conns = instring.split(",\\s?");
			System.out.println("Input connection string: "+ initialString);
			System.out.println("Connections found: " + conns.length);
		}
		else {
			System.out.println("Not a valid connection string. Input: "+ initialString);
			System.out.println("Usage: java -jar app.jar input.txt or cat input.txt | java -jar app.jar");
			System.out.println("Example: Connections: AMS-PDX-617,NUE-AMS-123, AMS-LHR-43,LHR-HKG-1235, NUR-FRA-61, FRA-HKG-1087");
			System.exit(2);
		}
		
		return conns;
	}
	
	public static FlightConnection[] getConnections(String[] strconns) {
		FlightConnection[] conns = new FlightConnection[strconns.length];
		for(int i=0;i<strconns.length;i++) {
			conns[i] = new FlightConnection(strconns[i].split("-")[0], strconns[i].split("-")[1], Integer.parseInt(strconns[i].split("-")[2]));
		}
		return conns;
	}

	public static int getPriceConnection(FlightConnection[] conns, String connection) {
		System.out.println(connection);
		int cost = -1;
		if(connection.matches("\\D{3}(-\\D{3})+")) {
			String[] places = connection.split("-");
			System.out.println(places.length);
			cost = 0;
			for(int i=1;i<places.length;i++) {
				cost += 0;
			}
		}
		return cost;
	}
	
}

class FlightConnection{
	String source;
	String destination;
	int cost;
	
	public FlightConnection(String src, String dst, int cst) {
		source = src;
		destination = dst;
		cost = cst;
	}
}