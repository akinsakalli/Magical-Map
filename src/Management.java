import java.io.*;
import java.util.ArrayList;

public class Management {
    private static BufferedReader reader;
    private static BufferedWriter writer;
    public static void scanLandFile(String fileName, String outputFileName) throws IOException {
        reader = new BufferedReader(new FileReader(fileName));
        writer = new BufferedWriter(new FileWriter(outputFileName));
        String[] line = reader.readLine().split(" ");
        int xSize = Integer.parseInt(line[0]);
        int ySize = Integer.parseInt(line[1]);
        int mapSize = xSize * ySize;
        MagicalMap.initialiseMap(xSize, ySize, writer);

        int x;
        int y;
        int type;

        // For each line in the input file, a node is created and added to the map with the given information.
        for (int i = 0; i < mapSize; i++) {
            line = reader.readLine().split(" ");
            x = Integer.parseInt(line[0]);
            y = Integer.parseInt(line[1]);
            type = Integer.parseInt(line[2]);
            MagicalMap.addNode(x, y, type);
        }
    }
    public static void scanEdgesFile(String fileName) throws IOException {
        reader = new BufferedReader(new FileReader(fileName));
        String input;
        String[] line;
        Node node;
        Node node2;

        // For each edge in the input file, given information is processed and nodes are made neighbors of each other.
        while ((input = reader.readLine()) != null) {
            line = input.split("[ ,-]+");
            int x1 = Integer.parseInt(line[0]);
            int y1 = Integer.parseInt(line[1]);
            int x2 = Integer.parseInt(line[2]);
            int y2 = Integer.parseInt(line[3]);
            double travelTime = Double.parseDouble(line[4]);

            node = MagicalMap.getNode(x1, y1);
            node2 = MagicalMap.getNode(x2, y2);

            // Because traversal is suitable between two nodes in each direction,
            // we set each of the nodes as the other node's neighbor.
            node.addNeighborNode(node2, travelTime);
            node2.addNeighborNode(node, travelTime);
        }
    }
    public static void scanMissionFile(String fileName) throws IOException {
        reader = new BufferedReader(new FileReader(fileName));

        int radius = Integer.parseInt(reader.readLine());

        String input = reader.readLine();
        String[] line = input.split(" ");
        int x = Integer.parseInt(line[0]);
        int y = Integer.parseInt(line[1]);
        Node startingNode = MagicalMap.getNode(x, y);

        // Before we start looking for a path, we can see the nodes in our line of sight around the starting node.
        MagicalMap.seeAround(startingNode, radius, new ArrayList<>());

        int objectiveNum = 1;
        boolean helping = false;
        int[] allowedTypes = new int[1];

        while ((input = reader.readLine()) != null) {
            line = input.split(" ");
            int targetX = Integer.parseInt(line[0]);
            int targetY = Integer.parseInt(line[1]);
            Node targetNode = MagicalMap.getNode(targetX, targetY);

            // If we have the wizard's help while going to the next objective, we choose the most useful one out of them.
            if (helping) {
                int allowedType = MagicalMap.wizardHelp(startingNode, targetNode, allowedTypes);
                writer.write("Number " + allowedType + " is chosen!");
                writer.newLine();
                helping = false; // We will not get the wizard's help while going to the next objective.
            }
            if (line.length >= 3) {
                MagicalMap.moveToSite(startingNode, targetNode, radius);
                writer.write("Objective " + objectiveNum + " reached!");
                writer.newLine();
                startingNode = targetNode; // We will start from this node while going to the next objective.

                // Wizard will help us while going to the next objective. We record the alternative helps the wizard might give us.
                allowedTypes = new int[line.length - 2];
                for (int i = 0; i < line.length-2; i++) {
                    allowedTypes[i] = Integer.parseInt(line[i + 2]);
                }
                helping = true;
            }
            else if (line.length == 2) {
                MagicalMap.moveToSite(startingNode, targetNode, radius);
                writer.write("Objective " + objectiveNum + " reached!");
                writer.newLine();
                startingNode = targetNode; // We will start from this node while going to the next objective.
            }
            objectiveNum++;
        }
        writer.flush();
        writer.close();
    }
}
