import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Network {

    private final Set<Node> nodes;
    private final List<Node> messagesOrder;

    public Network() {
        this.nodes = new HashSet<>();
        this.messagesOrder = new ArrayList<>();
    }

    public void addMessagesOrder(List<Node> messagesOrder) {
        this.messagesOrder.addAll(messagesOrder);
    }

    public Node addNodeIfAbsent(Node newNode) {
        nodes.add(newNode);

        return nodes.stream()
                .filter(node -> node.equals(newNode))
                .findAny()
                .orElse(newNode);
    }

    public Node getNodeById(String id) {
        return nodes.stream()
                .filter(node -> node.getId().equals(id))
                .findAny()
                .orElse(null);
    }

    public List<RoutingTable> computeAllRoutingTables(Function<Pair<Node, List<RoutingTable>>, Boolean> onEachMessage) {
        if (messagesOrder.isEmpty())
            onEachMessage.apply(new Pair<>(null, getRoutingTables()));

        messagesOrder.forEach(node -> {
            node.notifyNeighbours();
            onEachMessage.apply(new Pair<>(node, getRoutingTables()));
        });

        return getRoutingTables();
    }

    private List<RoutingTable> getRoutingTables() {
        return nodes.stream()
                .map(Node::getRoutingTable)
                .collect(Collectors.toList());
    }
}
