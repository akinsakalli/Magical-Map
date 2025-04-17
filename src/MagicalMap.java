import java.io.*;
import java.util.ArrayList;
public class MagicalMap {
    private static Node[][] map;
    private static int xSize;
    private static int ySize;
    private static BufferedWriter writer;
    public static void initialiseMap(int x, int y, BufferedWriter fileWriter) {
        map = new Node[y][x];
        writer = fileWriter;
        xSize = x;
        ySize = y;
    }
    public static void addNode(int x, int y, int type) {
        map[y][x] = new Node(x, y, type);
    }
    public static Node getNode(int x, int y) {
        return map[y][x];
    }
    private static ArrayList<Integer> chosenNumbers = new ArrayList<>();
    public static void moveToSite(Node startingNode, Node targetNode, int radius) throws IOException {
        // We construct the fastest path using the information we have up to this point.
        DijkstraResult data = dijkstra(map, startingNode, targetNode, -1);
        // Then we start walking on this path.
        walk(data, radius);
    }
    public static int wizardHelp(Node startingNode, Node targetNode, int[] allowedTypes) {
        double minDistance = Double.POSITIVE_INFINITY;
        int minDistanceType = -1;

        // We try each of the alternative helps the wizard might give us.
        for (int i = 0; i < allowedTypes.length; i++) {
            // While going to the destination, we can pass through the nodes which has the type that is equal to the current help.
            double distance = dijkstra(map, startingNode, targetNode, allowedTypes[i]).getDistance();

            // If we have chosen this number before in one of the helps, we do not choose it again
            // because all the nodes with that type is already converted to type 0.
            if (distance < minDistance && !chosenNumbers.contains(allowedTypes[i])) {
                minDistance = distance;
                minDistanceType = allowedTypes[i];
            }
        }

        // All nodes with type number equal to the chosen help are converted to type 0.
        for (int j = 0; j < map.length; j++) {
            for (int i = 0; i < map[0].length; i++) {
                if (map[j][i].getType() == minDistanceType) {
                    map[j][i].setType(0);
                }
            }
        }
        chosenNumbers.add(minDistanceType); // We will not choose the same number again for help.
        return minDistanceType;
    }

    /**
     * The algorithm that finds the shortest path between two nodes.
     * @param map is the map containing all the information about the nodes and their edges.
     * @param startingNode is the node we start the path from.
     * @param targetNode is the node the path will be finished.
     * @param allowedType is the type of the nodes which passing is allowed for that iteration.
     * @return a pair which contains the shortest distance and the corresponding path between the given nodes.
     */
    public static DijkstraResult dijkstra(Node[][] map, Node startingNode, Node targetNode, int allowedType) {
        int targetX = targetNode.getX();
        int targetY = targetNode.getY();
        MyMinHeap<Node> queue = new MyMinHeap<>();

        for (int j = 0; j < ySize; j++) {
            for (int i = 0; i < xSize; i++) {
                map[j][i].setDistance(Double.POSITIVE_INFINITY);
                map[j][i].setPredecessor(null);
                map[j][i].setVisited(false);
            }
        }
        startingNode.setDistance(0);
        startingNode.setVisited(true);

        queue.insert(startingNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.deleteMin();
            currentNode.setVisited(true);

            // Target node is reached. Algorithm terminates.
            if (currentNode.getX() == targetX && currentNode.getY() == targetY) {
                ArrayList<Node> path = constructPath(currentNode);
                return new DijkstraResult(currentNode.getDistance(), path);
            }

            ArrayList<Node> neighbors = currentNode.getNeighborNodes();
            ArrayList<Double> neighborTravelTimes = currentNode.getNeighborTravelTimes();

            // We iterate through the passable unvisited neighbors of the current node according to the current information.
            for (int i = 0; i < neighbors.size(); i++) {
                Node neighbor = neighbors.get(i);
                Double travelTimeToNeighbor = neighborTravelTimes.get(i);

                if (!neighbor.isVisited() && isPassable(neighbor, allowedType)) {
                    if (currentNode.getDistance() + travelTimeToNeighbor < neighbor.getDistance()) {
                        neighbor.setDistance(currentNode.getDistance() + travelTimeToNeighbor);
                        neighbor.setPredecessor(currentNode);
                        if (!queue.contains(neighbor)) {
                            queue.insert(neighbor);
                        }
                        else {
                            // If this node was already in the heap, its priority is changed.
                            queue.percolateUp(neighbor);
                        }
                    }
                }
            }
        }
        return null;
    }
    private static class DijkstraResult {
        private double distance;
        private ArrayList<Node> path;
        public DijkstraResult(double distance, ArrayList<Node> path) {
            this.distance = distance;
            this.path = path;
        }
        public double getDistance() { return distance; }
        public ArrayList<Node> getPath() { return this.path; }
    }

    /**
     * The given path is followed and the nodes that are in the line of sight is revealed in each step.
     * If an impassable node that is on the path is encountered, a new path is created.
     * @param data the pair containing the path and the distance of that path.
     * @param radius is the maximum distance that can be seen.
     * @throws IOException if the path does not exist.
     */
    public static void walk(DijkstraResult data, int radius) throws IOException {
        ArrayList<Node> path = data.getPath();

        // Traversal to the starting node is not printed.
        path.remove(path.size()-1);

        while (!path.isEmpty()) {
            Node node = path.remove(path.size()-1);

            // We traverse to the next node.
            int x = node.getX();
            int y = node.getY();
            writer.write("Moving to " + x + "-" + y);
            writer.newLine();

            // We reveal the nodes that came into our line of sight.
            if (seeAround(node, radius, path)) {
                writer.write("Path is impassable!");
                writer.newLine();
                // If there is an impassable node on the path, we construct a new path according to the new information.
                data = dijkstra(map, node, path.get(0), -1);
                path = data.getPath();
                path.remove(path.size()-1);
            }
        }
    }
    public static boolean seeAround(Node node, int radius, ArrayList<Node> path) {
        int x = node.getX();
        int y = node.getY();
        boolean impassableNode = false;
        MyHashMap<Node> hashMap = new MyHashMap<>();
        for (int i=0; i < path.size(); i++) {
            hashMap.insert(path.get(i));
        }

        // The map around the current node is revealed.
        for (int j = y - radius; j < y + radius + 1; j++) {
            for (int i = x - radius; i < x + radius + 1; i++) {
                if (isInsideMap(i, j, xSize, ySize)) {
                    if (Math.pow(x - i, 2) + Math.pow(y - j, 2) <= Math.pow(radius, 2)) {
                        map[j][i].setSeen(true);
                        if (!isPassable(map[j][i], -1) && hashMap.contains(map[j][i])) {
                            // Return true if an impassable node is encountered on the map.
                            impassableNode = true;
                        }
                    }
                }
            }
        }
        return impassableNode;
    }
    private static boolean isInsideMap(int x, int y, int xSize, int ySize) {
        if (x < 0 || x >= xSize || y < 0 || y >= ySize)
            return false;
        return true;
    }
    private static boolean isPassable(Node node, int allowedType) {
        if (node.getType() == 1) {
            return false;
        }
        else if (node.getType() == 0) {
            return true;
        }
        // If a node has a type greater than or equal to 2 and is not seen yet, it is passable.
        else if (!node.isSeen()) {
            return true;
        }
        // Nodes with type equal to the wizard's help are considered passable.
        else if (node.getType() == allowedType) {
            return true;
        }
        else {
            return false;
        }
    }
    private static ArrayList<Node> constructPath(Node node) {
        ArrayList<Node> path = new ArrayList<>();
        path.add(node);

        // The given node is backtracked up to the starting node.
        while (node.getPredecessor() != null) {
            path.add(node.getPredecessor());
            node = node.getPredecessor();
        }
        return path;
    }
}
