import java.util.*;

public class Train {

    static class Station{
        private final String name;
        private final HashSet<Direction> inEdges;
        private final HashSet<Direction> outEdges;
        private Station(String name) {
            this.name = name;
            inEdges = new HashSet<>();
            outEdges = new HashSet<>();
        }
        private Station addDirection(Station station, int distance){
            Direction s = new Direction(this, station, distance);
            outEdges.add(s);
            station.inEdges.add(s);
            return this;
        }
        @Override
        public String toString() {
            return name;
        }
    }

    static class Direction {
        private final Station from;
        private final Station to;
        private final int distance;
        private Direction(Station from, Station to, int distance) {
            this.from = from;
            this.to = to;
            this.distance = distance;
        }
    }


    public static void main(String[] args) {

        // Paths to the questions
        String[] path1 = {"A", "B", "C"};
        String[] path2 = {"A", "D"};
        String[] path3 = {"A", "D", "C"};
        String[] path4 = {"A", "E", "B", "C", "D"};
        String[] path5 = {"A", "E", "D"};
        String str = "C";
        String[] path7 = {"A", "C"};
        String[] path8 = {"A", "C"};
        String[] path9 = {"B", "B"};
        String[] path10 = {"C", "C"};

        // Create the graph stations and add the stations directions
        Station a = new Station("A");
        Station b = new Station("B");
        Station c = new Station("C");
        Station d = new Station("D");
        Station e = new Station("E");
        a.addDirection(b, 5);
        a.addDirection(d, 5);
        a.addDirection(e, 7);
        b.addDirection(c, 4);
        c.addDirection(d, 8);
        c.addDirection(e, 2);
        d.addDirection(c, 8);
        d.addDirection(e, 6);
        e.addDirection(b, 3);

        // Map all stations with a key of the name
        final HashMap<String, Station> mapStations = new HashMap<>();
        mapStations.put("A", a);
        mapStations.put("B", b);
        mapStations.put("C", c);
        mapStations.put("D", d);
        mapStations.put("E", e);
        Station[] allStations = {a, b, c, d, e};

        // Question 1
        Train.directPath(path1, mapStations);

        // Question 2
        Train.directPath(path2, mapStations);

        // Question 3
        Train.directPath(path3, mapStations);

        // Question 4
        Train.directPath(path4, mapStations);

        // Question 5
        Train.directPath(path5, mapStations);

        // Question 6
        Train.findStart(str, mapStations);

        // Question 7
        Train.numberOccurrence(path7, mapStations);

        // Question 8

        Train.shortRoute(path8, mapStations, allStations, false);

        // Question 9
        Train.shortRoute(path9, mapStations, allStations,  false);

        // Question 10
        Train.shortRoute(path10, mapStations, allStations, true);
    }

    private static void shortRoute(String[] path, HashMap<String, Station> map, Station[] allStations, boolean isNumberRoute){

        int short_path = 0;
        String end = path[1];
        Station startNode = map.get(path[0]);
        HashMap<String, Boolean> visited = new HashMap<>();
        ArrayList<Integer> neighborStationsDistances = new ArrayList<>();

        // Set all the stations to false
        for(Station s: allStations){
            visited.put(s.name, false);
        }
        visited.put(startNode.name, true);

        // Grab the neighbor nodes
        for (Direction direct : startNode.outEdges) {
            if (direct.to.toString().equals(end)){
               short_path = direct.distance;
            } else {
                short_path = shortRouteUtilOne(map.get(direct.to.toString()), direct.distance,
                        end, map, visited, neighborStationsDistances, isNumberRoute);
                for(Station s: allStations){
                    visited.put(s.name, false);
                }
            }
        }
        System.out.println(short_path);
    }

    private static int shortRouteUtilOne(Station s, int weight, String end, HashMap<String,Station> map,
                                        HashMap<String, Boolean> v, ArrayList<Integer> neighborStationsDistances,
                                        boolean isNumberRoute){
        int numberRoutes = 0;

        // Grab the to values the node and then put the weight and the current distance in a list
        for (Direction direct : s.outEdges) {
            if (!direct.to.toString().equals(end)) {
                neighborStationsDistances.add(shortRouteUtilTwo(map.get(direct.to.toString()),
                                        weight+direct.distance, end, map, v));
            } else {
                neighborStationsDistances.add(weight+direct.distance);
            }
        }

        // Get the values less than a distance of 30
        for(int i: neighborStationsDistances){
            if(i<30){
                numberRoutes += 1;
            }
        }

        if (!isNumberRoute) {
            return Collections.min(neighborStationsDistances);
        } else {
            return  numberRoutes;
        }
    }
    private static int shortRouteUtilTwo(Station s, int weight, String end, HashMap<String,Station> map,
                                        HashMap<String, Boolean> v){
        Queue<Station> queue = new LinkedList<>();
        v.put(s.name, true);

        // Grab the neighbors and if on the last element in path then add the distance to the next node recursively
        for (Direction direct : s.outEdges) {
            queue.add(map.get(direct.to.toString()));
            if (direct.to.toString().equals(end)) {
                return direct.distance + weight;
            } else {
                s = queue.poll();
                weight += direct.distance;
            }
        }

        return weight;
    }

    private static void numberOccurrence(String[] path, HashMap<String, Station> map){

        int count = -1;
        String end = path[1];
        Station startNode = map.get(path[0]);
        ArrayList<Integer> stationCount = new ArrayList<>();
        Queue<Station> queue = new LinkedList<>();
        Station currentStation = null;

        // Implement a BFS type algo, that push a node on the queue and check directions
        queue.add(startNode);
        while(!queue.isEmpty()) {
            currentStation = queue.poll();
            for (Direction direct : currentStation.outEdges) {
                if (direct.to.toString().equals(end)){
                    stationCount.add(count);
                } else {
                    queue.add(map.get(direct.to.toString()));
                    count +=1;
                }
            }
        }
        int finalCount=0;
        for(int co: stationCount){
            if(co == 4){
                finalCount +=1;
            }
        }
        System.out.println(finalCount);
    }

    private static void findStart(String str, HashMap<String, Station> map){
        int count = 0;
        int  finalCount = 0;
        Station stationNode = map.get(str);
        ArrayList<Integer> stationCount = new ArrayList<>();
        Queue<Station> queue = new LinkedList<>();
        Station currentStation = null;

        // Implement a BFS type algo, that push a node on the queue and check directions
        queue.add(stationNode);
        while(!queue.isEmpty()) {
            currentStation = queue.poll();
            for (Direction direct : currentStation.outEdges) {
                if (direct.to.toString().equals(str)){
                    stationCount.add(count);
                    count =0;
                    break;
                } else {
                    queue.add(map.get(direct.to.toString()));
                }
                count+=1;
            }
        }

        // Rally the counts
        for(int sc: stationCount){
            if (sc > 0 && sc <= 3){
                finalCount += 1;
            }
        }
        System.out.println(finalCount);
    }

    private static void directPath(String[] paths, HashMap<String, Station> map){
        Queue<String> queue = new LinkedList<>();
        ArrayList<Integer> distanceList = new ArrayList<>();
        ArrayList<Station> trainStopPath = new ArrayList<>();
        String next_path = null;
        int count = 1;
        int total = 0;

        // Get the nodes of the direct path
        for(String p: paths) {
            queue.add(p);
            trainStopPath.add(map.get(p));
        }

        // Pop the first path direction and compare the next path direction and grab the distance
        queue.poll() ;
        for (Station n : trainStopPath) {
            if (queue.isEmpty()){
                break;
            }
            next_path = queue.poll();
            for (Direction stop : n.outEdges) {
                if (stop.to.toString().equals(next_path)) {
                    distanceList.add(stop.distance);
                    count+=1;
                }
            }
        }

        for(int i: distanceList){
            total+=i;
        }

        // Check that each element in the path was processed
        if(count!=trainStopPath.size()){
            System.out.println("NO SUCH ROUTE");
        } else {
            System.out.println(total);
        }
    }
}