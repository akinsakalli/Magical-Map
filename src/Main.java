import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String nodesFile = args[0];
        String edgesFile = args[1];
        String missionFile = args[2];
        String outputFile = args[3];

        Management.scanLandFile(nodesFile, outputFile);
        Management.scanEdgesFile(edgesFile);
        Management.scanMissionFile(missionFile);
    }
}