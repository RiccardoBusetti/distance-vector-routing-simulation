import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NetworkParser {

    private static final String NODE_1 = "NODE_1";
    private static final String NODE_2 = "NODE_2";
    private static final String COST = "COST";

    private String topologyFileName;
    private String messagesOrderFileName;

    public NetworkParser withTopologyFileName(String topologyFileName) {
        this.topologyFileName = topologyFileName;

        return this;
    }

    public NetworkParser withMessagesOrderFileName(String messagesOrderFileName) {
        this.messagesOrderFileName = messagesOrderFileName;

        return this;
    }

    public Network parse() {
        return createNetwork(new Network());
    }

    private Network createNetwork(Network network) {
        eachLine(topologyFileName, line -> {
            Map<String, String> components = parseConnection(line);
            Node node1 = network.addNodeIfAbsent(new Node(components.get(NODE_1)));
            Node node2 = network.addNodeIfAbsent(new Node(components.get(NODE_2)));

            int cost = Integer.parseInt(components.get(COST));
            node1.addNeighbour(node2, cost);
            node2.addNeighbour(node1, cost);

            return true;
        });

        eachLine(messagesOrderFileName, line -> {
            network.addMessagesOrder(parseMessagesOrder(line)
                    .stream()
                    .map(network::getNodeById)
                    .collect(Collectors.toList()));

            return true;
        });

        return network;
    }

    private Map<String, String> parseConnection(String line) {
        String[] components = line.split(" ");

        Map<String, String> values = new HashMap<>();
        values.put(NODE_1, components[0]);
        values.put(NODE_2, components[1]);
        values.put(COST, components[2]);

        return values;
    }

    private List<String> parseMessagesOrder(String line) {
        String[] messagesOrder = line.split(" ");
        return Arrays.asList(messagesOrder);
    }

    private void eachLine(String fileName, Function<String, Boolean> block) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                block.apply(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
