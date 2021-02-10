import javafx.util.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoutingTable {

    private final Node node;
    private final List<Cost> costs;

    public RoutingTable(Node node, List<Cost> costs) {
        this.node = node;
        this.costs = costs;
        beautify();
    }

    public static RoutingTable withNeighbours(Node node, Map<Node, Integer> adjacentNodes) {
        List<Cost> costs = adjacentNodes.entrySet()
                .stream()
                .map(pair -> new Cost(pair.getKey(), pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());
        // We add also the cost to itself
        costs.add(new Cost(node, 0));

        return new RoutingTable(node, costs);
    }

    public void handleIncomingCost(Node incomingFrom, Cost incomingCost) {
        Cost currentCost = findCostByDestination(incomingCost.getDestination());

        if (currentCost != null) {
            costs.remove(currentCost);
            costs.add(updatedCost(currentCost, incomingFrom, incomingCost));
        } else {
            costs.add(new Cost(incomingCost.destination, incomingFrom, incomingCost.getCostToNode()));
        }

        beautify();
    }

    private Cost updatedCost(Cost currentCost, Node incomingFrom, Cost incomingCost) {
        return incomingCost.getCostToNode() < currentCost.getCostToNode() ?
                new Cost(currentCost.destination, incomingFrom, incomingCost.getCostToNode()) :
                currentCost.clone();
    }

    private void beautify() {
        costs.sort(Comparator.comparing(o -> o.getDestination().getId()));
    }

    public List<Cost> getCosts() {
        return costs;
    }

    private Cost findCostByDestination(Node destination) {
        return costs.stream()
                .filter(cost -> cost.getDestination().equals(destination))
                .findAny()
                .orElse(null);
    }

    public Pair<String, String> serializeToFile() {
        return new Pair<>(node.getId(), costs.stream()
                .map(Cost::serializeToFile)
                .collect(Collectors.joining("\n")));
    }

    @Override
    public String toString() {
        return "Router " +
                node +
                ":" +
                "\n" +
                "Dest, Next hop, Cost" +
                "\n" +
                costs.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n"));
    }

    public static class Cost implements Comparable<Cost> {

        private final Node destination;
        private final Node nextHop;
        private Integer costToNode;

        public Cost(Node destination) {
            this.destination = destination;
            this.nextHop = null;
            this.costToNode = Integer.MAX_VALUE;
        }

        public Cost(Node destination, Integer costToNode) {
            this.destination = destination;
            this.nextHop = null;
            this.costToNode = costToNode;
        }

        public Cost(Node destination, Node nextHop, Integer costToNode) {
            this.destination = destination;
            this.nextHop = nextHop;
            this.costToNode = costToNode;
        }

        public Node getDestination() {
            return destination;
        }

        public Node getNextHop() {
            return nextHop;
        }

        public Integer getCostToNode() {
            return costToNode;
        }

        public Cost incrementCost(Integer by) {
            costToNode += by;
            return this;
        }

        @Override
        protected Cost clone() {
            return new Cost(destination, nextHop, costToNode);
        }

        @Override
        public int compareTo(Cost o) {
            return costToNode.compareTo(o.costToNode);
        }

        public String serializeToFile() {
            return destination.getId() + " " + (nextHop != null ? nextHop.getId() : "direct");
        }

        @Override
        public String toString() {
            return destination + "     " + (nextHop != null ? nextHop.getId() + "         " : "direct    ") + costToNode;
        }
    }
}
