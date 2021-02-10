import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Node implements Communicable {

    private final String id;
    private final Map<Node, Integer> neighbours;
    private RoutingTable routingTable;

    public Node(String id) {
        this.id = id.toUpperCase();
        this.neighbours = new HashMap<>();
    }

    public void addNeighbour(Node node, Integer cost) {
        neighbours.putIfAbsent(node, cost);
        routingTable = RoutingTable.withNeighbours(this, neighbours);
    }

    public void notifyNeighbours() {
        neighbours.forEach((neighbour, cost) -> send(neighbour));
    }

    public String getId() {
        return id;
    }

    public RoutingTable getRoutingTable() {
        return routingTable;
    }

    @Override
    public void receive(Node from) {
        updateRoutingTable(from.getRoutingTable(), from);
    }

    private void updateRoutingTable(RoutingTable incomingRoutingTable, Node from) {
        incomingCostsStream(incomingRoutingTable, neighbours.get(from))
                .forEach(incomingCost -> routingTable.handleIncomingCost(from, incomingCost));
    }

    private Stream<RoutingTable.Cost> incomingCostsStream(RoutingTable incomingRoutingTable, Integer costFrom) {
        // We already feed into the routing table the dv with the cost + cost to the from node.
        return incomingRoutingTable.getCosts()
                .stream()
                .map(cost -> cost.clone().incrementCost(costFrom));
    }

    @Override
    public void send(Node to) {
        to.receive(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id.equals(node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
