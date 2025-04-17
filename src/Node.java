import java.util.ArrayList;

public class Node implements Comparable<Node> {
    private int x;
    private int y;
    private double distance;
    private Node predecessor;
    private int type;
    private boolean isSeen;
    private ArrayList<Node> neighborNodes;
    private ArrayList<Double> neighborTravelTimes;
    private boolean isVisited; // For dijkstra
    public Node(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.isSeen = false;
        this.neighborNodes = new ArrayList<>();
        this.neighborTravelTimes = new ArrayList<>();
    }
    public void setSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }
    public boolean isSeen() {
        return isSeen;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }
    public boolean isVisited() {
        return this.isVisited;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || object.getClass() != this.getClass())
            return false;
        Node comparedNode = (Node) object;
        return (this.getX() == comparedNode.getX() && this.getY() == comparedNode.getY());
    }

    @Override
    public int compareTo(Node node) {
        if (this.distance > node.distance)
            return 1;
        else if (this.distance < node.distance)
            return -1;
        else
            return 0;
    }
    public void addNeighborNode(Node node, double time) {
        this.neighborNodes.add(node);
        this.neighborTravelTimes.add(time);
    }
    public ArrayList<Node> getNeighborNodes() {
        return this.neighborNodes;
    }
    public ArrayList<Double> getNeighborTravelTimes() {
        return this.neighborTravelTimes;
    }
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public double getDistance() { return this.distance; }
    public void setDistance(double distance) { this.distance = distance; }
    public Node getPredecessor() { return this.predecessor; }
    public void setPredecessor(Node predecessor) { this.predecessor = predecessor; }

}
