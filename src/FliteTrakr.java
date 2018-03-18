import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FliteTrakr {

	public static void main(String[] args) throws IOException {
/*		if(args.length>1 && args[0].equals("Connections:")) {
			String inputvalues = args[1];
			for(int i=2;i<(args.length);i++) {
				inputvalues=inputvalues.concat(args[i]);
			}
			System.out.println("Input connections: " + inputvalues);
			String strConnections[] = inputvalues.split(",");
			if(strConnections.length>0) {
				FlightConnection[] Connections = new FlightConnection[strConnections.length];
				for(int i=0;i<strConnections.length;i++) {
					if(strConnections[i].matches("\\D{3}-\\D{3}-\\d+")) {
						System.out.println(strConnections[i]);
						String[] tmpFields = strConnections[i].split("-");
						System.out.println(tmpFields[0]+"   "+tmpFields[1]+"   "+tmpFields[2]);
						Connections[i] = new FlightConnection(tmpFields[0],tmpFields[1],Integer.parseInt(tmpFields[2]));
					}
					else {
						System.out.println("Usage: Connections: <code-of-departure-airport>-<code-of-arrival-airport>-<price-in-euro>");
						System.out.println("Example: Connections: AMS-PDX-617,NUE-AMS-123");
						System.exit(1);
					}
				}
				
			}
		}
		else {
			System.out.println("Usage: Connections: <code-of-departure-airport>-<code-of-arrival-airport>-<price-in-euro>");
			System.exit(1);
		} */
		//System.out.println(args.length);
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
			inputCad= readInputTextFile(args[0]);
		}
	}
	
	private static List<String> readInputTextFile(String fileName) throws IOException {
	    return Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
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