import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FliteTrakr {
	 
	public static void main(String[] args) throws IOException {
		// Exit value 1: more than one input argument was provided
		// Exit value 2: Input data couldn't be read
		// Exit value 3: Incorrect input connections at first line
		// Exit value 4: No input data

		List<String> inputCad = new ArrayList<String>();
		if(args.length==0) {				//Data input through command line
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
				System.exit(2);
			}
		}
		else {							//Data should be entered through a text file
			if(args.length==1) {			//File name provided
				inputCad= readInputTextFile(args[0]);
			}
			else {						//Only a file name should be input as parameter
				System.out.println("Usage: java -jar app.jar input.txt or cat input.txt | java -jar app.jar");
				System.exit(1);
			}
		}
		//Work with the input data
		if(inputCad.size()>0) {			//Existing input text
			String[] strConnections = getConnectionString(inputCad.get(0));
			if(strConnections.length==0) {
				System.out.println("Not a valid connection string. Input: "+ inputCad.get(0));
				System.out.println("Usage: java -jar app.jar input.txt or cat input.txt | java -jar app.jar");
				System.out.println("Example: Connections: AMS-PDX-617,NUE-AMS-123, AMS-LHR-43,LHR-HKG-1235, NUR-FRA-61, FRA-HKG-1087");
				System.exit(3);
			}
			else {
				Map<String, AirportConnections> connections = getAirportConnections(strConnections);
				for(String line : inputCad) {
					if(line.matches("^#\\d+: What is the price of the connection \\D{3}(-\\D{3})+\\?")) {
						String qnum = line.split(" ")[0];
						String route = line.split(" ")[8];
						
						route = route.substring(0, route.length()-1);
						Integer price = getPrice(route, connections);
						if(price==-1)
							System.out.println(qnum + " No such connection found!");
						else
							System.out.println(qnum + " " + price);
					}
					else{
						if(line.matches("^#\\d+: What is the cheapest connection from \\D{3} to \\D{3}\\?")) {
							String qnum = line.split(" ")[0];
							String src = line.split(" ")[7];
							String dst = line.split(" ")[9];
							
							dst = dst.substring(0, dst.length()-1);
							List<String> routes = getAllConnections(src,dst, connections, 0, 0);
							if(routes.isEmpty())
								System.out.println(qnum + " No such connection found!");
							else
								System.out.println(qnum + " " + routes.get(0));
						}
						else {
							if(line.matches("^#\\d+: How many different connections with maximum \\d+ stops exists between \\D{3} and \\D{3}\\?")) {
								String qnum = line.split(" ")[0];
								String src = line.split(" ")[11];
								String dst = line.split(" ")[13];
								String amount = line.split(" ")[7];
								
								dst = dst.substring(0, dst.length()-1);
								List<String> routes = getAllConnections(src,dst, connections, 1, Integer.parseInt(amount));
								System.out.println(qnum + " " + routes.size());
							}
							else {
								if(line.matches("^#\\d+: How many different connections with exactly \\d+ stop exists between \\D{3} and \\D{3}\\?")) {
									String qnum = line.split(" ")[0];
									String src = line.split(" ")[11];
									String dst = line.split(" ")[13];
									String amount = line.split(" ")[7];
									
									dst = dst.substring(0, dst.length()-1);
									List<String> routes = getAllConnections(src,dst, connections, 2, Integer.parseInt(amount));
									System.out.println(qnum + " " + routes.size());
								}
								else {
									if((line.matches("^#\\d+: Find all connections from \\D{3} to \\D{3} below \\d+Euros\\!"))) {
										String qnum = line.split(" ")[0];
										String src = line.split(" ")[5];
										String dst = line.split(" ")[7];
										String amount = line.split(" ")[9];
										
										List<String> routes = getAllConnections(src,dst, connections, 3, Integer.parseInt(amount.substring(0, amount.indexOf("Euros"))));
										if(routes.isEmpty())
											System.out.println(qnum + " No such connection found!");
										else {
											routes.sort( (r1,r2) ->{
												if(Integer.parseInt(r1.substring(r1.lastIndexOf("-")+1))<Integer.parseInt(r2.substring(r2.lastIndexOf("-")+1)) )
													return -1;
												else
													return 1;
											});
											System.out.println(qnum + " " + String.join(", ", routes));
										}
									}
								}
							}
						}
					}
				}
			}
		}
		else {							//No data input
			System.out.println("Empty input, no connections found");
			System.out.println("Usage: java -jar app.jar input.txt or cat input.txt | java -jar app.jar");
			System.out.println("Example: Connections: AMS-PDX-617,NUE-AMS-123, AMS-LHR-43,LHR-HKG-1235, NUR-FRA-61, FRA-HKG-1087");
			System.exit(4);
		}
		
	}
	
	
	
	
	private static List<String> readInputTextFile(String fileName) throws IOException {
		// This function reads a text file and returns its content as a list of strings
		// Input: Filename as a String
		// Output: A List of String
	    return Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
	  }
	
	public static String[] getConnectionString(String initialString) {
		// This function checks that the first input line matches the specification and returns the airport connections string
		// Matching pattern: "Connections: <code-of-departure-airport>-<code-of-arrival-airport>-<price-in-euro>" each connection separated by a comma with an optional space
		// Input: first line of the file as a String
		// Output: Array of Strings with each airport connection
		
		String[] conns = {};
		
		if(initialString.matches("^(Connections: \\D{3}-\\D{3}-\\d+)(,\\s?\\D{3}-\\D{3}-\\d+)*")){
			String instring = initialString.split("Connections:")[1].trim();
			conns = instring.split(",\\s?");
			//System.out.println("Input connection string: "+ initialString);
			//System.out.println("Connections found: " + conns.length);
		}

		return conns;
	}
	
	public static Map<String, AirportConnections> getAirportConnections(String[] strconn){
		// This function loads an array of strings containing all airport connections into a Hash map
		//     for fast reference. The key is the source airport and the value is an airport connection
		//     object that contains all its destinations and its prices.
		// Input: array of airport connection strings
		// Output: Hashmap of each airport with its destinations
		
		Map<String, AirportConnections> conns = new HashMap<String, AirportConnections>();
		for(int i=0;i<strconn.length;i++) {
			String src = strconn[i].split("-")[0];
			String dst = strconn[i].split("-")[1];
			String cst = strconn[i].split("-")[2];
			if(conns.containsKey(src)) {
				AirportConnections tmp = new AirportConnections(src, conns.get(src).destinations, conns.get(src).costs);
				tmp.destinations.add(dst);
				tmp.costs.add(Integer.parseInt(cst));
				conns.put(src, tmp);
			}
			else {
				AirportConnections tmp = new AirportConnections(src, dst, Integer.parseInt(cst));
				conns.put(src, tmp);
			}
		}
		return conns;
	}
	
	public static Integer getConnectionPrice(String src, String dst, Map<String, AirportConnections> conns) {
		// This function returns the price of a direct airport connection from source to destination
		// Input: source airport string, destination airport string and the hashmap of airport connections
		// Output: price of such connection as an integer. If the connection doesn't exist it returns -1
		
		if(conns.containsKey(src)) {
			AirportConnections tmp = conns.get(src);
			if(tmp.destinations.contains(dst)) {
				return tmp.costs.get(tmp.destinations.indexOf(dst));
			}
			else {
				return -1;
			}
		}
		else {
			return -1;
		}
	}
	
	public static Integer getPrice(String route, Map<String, AirportConnections> conns) {
		// The objective of this function is given a traveling route string it will return the price of such. 
		// The travel route string will be like "<airport>-<airport>-....<airport>"
		// Input: travel route as a string and hashmap with all airport connections
		// Output: Price of such route as an integer. It'll return -1 in case that no such route exists
		
		int price = 0;
		String[] airports = route.split("-");
		
		for(int i=0;i<airports.length-1;i++) {
			int tmp = getConnectionPrice(airports[i], airports[i+1], conns);
			if(tmp == -1) {
				return -1;
			}
			else {
				price += tmp;
			}
		}
		return price;
	}

	public static List<String> getAllConnections(String src, String dst, Map<String, AirportConnections> conns, Integer stopType, Integer stopValue){
		// This function will return the routes between source and destination that match the specified search condition 
		// Stop condition type 0: which is the cheapest connection
		// Stop condition type 1: How many connections with max n stops
		// Stop condition type 2: How many connections with exact n stops
		// Stop condition type 3: all connections below n euros
		// Input: source airport string, destination airport string, hashmap with all airport connections, the search type as an integer and the search condition value as an integer.
		// Output: It'll return the a list of strings containing all routes that match the search condition
		
		List<String> routes = new ArrayList<String>();
		List<String> openRoutes = new ArrayList<String>();
		
		if(stopType==0)
			stopValue=Integer.MAX_VALUE;
		if(stopValue>3 && stopValue<0) {
			System.out.println("Invalid stop condition type!");
			return null;
		}
		
		openRoutes.add(src);
		while(openRoutes.size()>0) {
			List<String> tmp = new ArrayList<String>(openRoutes);
			openRoutes.clear();
			for(int i=0;i<tmp.size();i++) {
				String routeStr = tmp.get(i);
				String lastNode = routeStr.substring(routeStr.length()-3);
				if(conns.get(lastNode)!=null) {
					List<String> nodeDestinations = conns.get(lastNode).destinations;
				
					for(int j=0;j<nodeDestinations.size();j++) {
						if(nodeDestinations.get(j).equals(dst)) {
							switch(stopType) {
								case 0:
									if(getPrice(routeStr + "-"+nodeDestinations.get(j), conns)<stopValue) {
										routes.clear();
										stopValue=getPrice(routeStr + "-"+nodeDestinations.get(j), conns);
										routes.add(routeStr + "-"+nodeDestinations.get(j)+"-"+stopValue);
									}
									break;
								case 1:
									routes.add(routeStr + "-"+nodeDestinations.get(j)+"-"+getPrice(routeStr + "-"+nodeDestinations.get(j), conns));
									if((routeStr + "-"+nodeDestinations.get(j)).split("-").length<=(stopValue+2)) {
										openRoutes.add(routeStr + "-"+nodeDestinations.get(j));
									}
									break;
								case 2:
									if((routeStr + "-"+nodeDestinations.get(j)).split("-").length==(stopValue+2)) {
										routes.add(routeStr + "-"+nodeDestinations.get(j)+"-"+getPrice(routeStr + "-"+nodeDestinations.get(j), conns));
									}
									break;
								case 3:
									if(getPrice(routeStr + "-"+nodeDestinations.get(j), conns)<stopValue) {
										routes.add(routeStr + "-"+nodeDestinations.get(j)+"-"+getPrice(routeStr + "-"+nodeDestinations.get(j), conns));
										openRoutes.add(routeStr + "-"+nodeDestinations.get(j));
									}
									break;
								default:
										break;
							}	
						}
						else {
							switch(stopType) {
								case 0:
									if(getPrice(routeStr + "-"+nodeDestinations.get(j), conns)<stopValue) {
										openRoutes.add(routeStr + "-"+nodeDestinations.get(j));
									}
									break;
								case 1:
									if((routeStr + "-"+nodeDestinations.get(j)).split("-").length<=(stopValue+2)) {
										openRoutes.add(routeStr + "-"+nodeDestinations.get(j));
									}
									break;
								case 2:
									if((routeStr + "-"+nodeDestinations.get(j)).split("-").length<(stopValue+2)) {
										openRoutes.add(routeStr + "-"+nodeDestinations.get(j));
									}
									break;
								case 3:
									if(getPrice(routeStr + "-"+nodeDestinations.get(j), conns)<stopValue) {
										openRoutes.add(routeStr + "-"+nodeDestinations.get(j));
									}
								default:
									break;
							}
						}
					}
				}
			}
		}

		return routes;
	}
	
}


class AirportConnections {
	// Airport connection class that contains the airport name, it's available destinations and the price for each connection
	String airportName;
	List<String> destinations;
	List<Integer> costs;
	
	public AirportConnections(String name, String destination, int cost) {
		destinations = new ArrayList<String>();
		costs = new ArrayList<Integer>();
		
		airportName = name;
		destinations.add(destination);
		costs.add(cost);
	}
	public AirportConnections(String name, List<String> dests, List<Integer> csts) {
		airportName = name;
		destinations = dests;
		costs = csts;
	}
}