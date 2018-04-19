# Adidas FliteTrakr Coding Challenge

This is a coding challenge from Adidas. You can find full details on <https://bitbucket.org/adigsd/backend-flitetrakr>.

The main purpose of the application is finding the best travel plans for a different set of airport connections. In order to do so, a set of flight details are provided and several questions can be answered, for example, which is the cheapest travel route between airports.

## Getting Started

You can download the code (created on Eclipse) to build and run the application, or directly run the included jar file (Flite.jar).

### Running the app

If you run the jar file, you'll need to provide the input information either as a text file or as direct input to it:

- java -jar Flite.jar input.txt
- cat input.txt | java -jar Flite.jar

The input information should be:

- First line containing the existing flight connections and their prices. Line should start by `Connections: ` and then each connection should have the format `<code-of-departure-airport>-<code-of-arrival-airport>-<price-in-euro>`. The connections will be separated by a comma and may have an optional space.
  - For example: `Connections: AMS-PDX-617,NUE-AMS-123, AMS-LHR-43,LHR-HKG-1235, NUR-FRA-61, FRA-HKG-1087`

#### Valid questions for the application
- **"What is the price of the connection <route>?".**  
`What is the price of the connection NUE-LHR-BOS?`
- **"What is the cheapest connection from <source> to <destination>?".**  
`What is the cheapest connection from AMS to DXB?"`
- **"How many different connections with maximum <n> stops exist between <source> and <destination>?".**  
`How many different connections with maximum 3 stops exist between PDX and BOS?`
- **"How many different connections with exactly <n> stops exist between <source> and <destination>?".**  
`How many different connections with exactly 2 stops exist between DXB and DXB?`
- **"Find all connections from <source> to <destination> below <n>Euros!".**  
`Find all connections from AMS to LHR below 2500Euros!`

Each input line should start by "#<n>" where n is the question number. This is used for reference when displaying the output:  
`#3: What is the price of the connection NUE-FRA-LHR-NUE?"`

#### Valid answers from the application
- **"What is the price of the connection <route>?"**  
If the route exists application will return the total cost of the route, with no formatting  
If the route **doesn't** exist application will return `No such connection found!`
- **"What is the cheapest connection from <source> to <destination>?"**  
Application will return the cheapest route including the total cost at the end of the route. For example, `AMS-LHR-FRA-DXB-1347`.  
If the route **doesn't** exist application will return `No such connection found!`.  
If the route source and destination airport is the same, it will look for the cheapeast route that starts and ends that airport, excluding not travelling. For example, it will return `FRA-LHR-FRA-312` instead of `FRA-FRA-0` for airport FRA.
- **"How many different connections with maximum <n> stops exist between <source> and <destination>?"**  
The application will return the amount of routes available, being 0 a valid answer.
- **"How many different connections with exactly <n> stops exist between <source> and <destination>?"**  
The application will return the amount of routes available, being 0 a valid answer.
- **"Find all connections from <source> to <destination> below <n>Euros!"**  
Application will print out all the available routes separated by comma and sorted by ascending price.


### Example

**Let's view an example, given the following input:**  

`Connections: NUE-FRA-43, NUE-AMS-67, FRA-AMS-17, FRA-LHR-27, LHR-NUE-23`  
`#1: What is the price of the connection NUE-FRA-LHR?`  
`#2: What is the price of the connection NUE-AMS-LHR?`  
`#3: What is the price of the connection NUE-FRA-LHR-NUE?`  
`#4: What is the cheapest connection from NUE to AMS?`  
`#5: What is the cheapest connection from AMS to FRA?`  
`#6: What is the cheapest connection from LHR to LHR?`  
`#7: How many different connections with maximum 3 stops exists between NUE and FRA?`  
`#8: How many different connections with exactly 1 stop exists between LHR and AMS?`  
`#9: Find all connections from NUE to LHR below 170Euros!`  

**This should print the following output:**  
`#1: 70`  
`#2: No such connection found!`  
`#3: 93`  
`#4: NUE-FRA-AMS-60`  
`#5: No such connection found!`  
`#6: LHR-NUE-FRA-LHR-93`  
`#7: 2`  
`#8: 1`  
`#9: NUE-FRA-LHR-70, NUE-FRA-LHR-NUE-FRA-LHR-163`

## Author

Guillermo Gonzalez - Zaragoza, Spain.

## Acknowledgments

Probably not the best and elegant solution, but hey, it works! (or at least I think so :| ) Hopefully I'll be able to look at this in a few years time and be able to improve it far more. I've been working on python for a while now, so this is a fun exercise.