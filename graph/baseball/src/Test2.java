import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.LinearProbingHashST;

/**
 * Class to test the Project selection problem given in this link
 * https://en.wikipedia.org/wiki/Max-flow_min-cut_theorem
 */
public class Test2 {
    private static LinearProbingHashST<String, Integer> makeNetworkVertices(int n) {
        LinearProbingHashST<String, Integer> vertices = new LinearProbingHashST<>();
        vertices.put("p1", 0);
        vertices.put("", 0);
        vertices.put("s", 0);
        vertices.put("s", 0);

        return vertices;
    }
    public static void main(String[] args) {
        LinearProbingHashST<String, Integer> vertices = makeNetworkVertices(7);
        FlowNetwork network = new FlowNetwork(vertices.size());
        // setupFlowNetwork(x, vertices, network);
        int s = vertices.get("s");
        int t = vertices.get("t");
        FordFulkerson maxflow = new FordFulkerson(network, s, t);
        for (FlowEdge edge : network.adj(s)) {
            if (edge.flow() < edge.capacity()) {
            }
        }
    }
}
