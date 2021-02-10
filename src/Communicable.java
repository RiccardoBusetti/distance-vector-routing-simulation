public interface Communicable {

    void receive(Node from);

    void send(Node to);
}
