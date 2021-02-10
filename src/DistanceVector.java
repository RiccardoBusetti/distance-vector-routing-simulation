import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DistanceVector {

    public static void main(String[] args) {
        String topologyFileName = args[0];
        String orderFileName = args[1];

        List<RoutingTable> routingTables = new NetworkParser()
                .withTopologyFileName(topologyFileName)
                .withMessagesOrderFileName(orderFileName)
                .parse()
                .computeAllRoutingTables(step -> printRoutingTables(step.getKey(), step.getValue()));

        saveRoutingTables(routingTables);
    }

    private static boolean printRoutingTables(Node source, List<RoutingTable> routingTables) {
        if (source != null)
            System.out.println("\n-- STATUS AFTER " + source + " HAS SENT HIS DISTANCE VECTOR. --");
        else
            System.out.println("\n-- INITIAL STATUS WITHOUT MESSAGE EXCHANGES. --");

        System.out.println(routingTables.stream()
                .map(RoutingTable::toString)
                .collect(Collectors.joining("\n\n")));

        return true;
    }

    private static void saveRoutingTables(List<RoutingTable> routingTables) {
        routingTables.stream()
                .map(RoutingTable::serializeToFile)
                .forEach(fileInformation -> {
                    String fileName = fileInformation.getKey();
                    String fileContent = fileInformation.getValue();
                    writeTxtFile(fileName, fileContent);
                });
    }

    private static void writeTxtFile(String fileName, String fileContent) {
        try {
            FileWriter writer = new FileWriter(fileName + ".txt");
            writer.write(fileContent);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing file " + fileName + ".txt" + ".");
        }
    }
}
