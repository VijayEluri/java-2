// Data type to determines the topological order
// of a DAG

import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Topological {
  private Stack<Integer> order;
  private boolean visited[];
  private int rank[];

  public Topological(Digraph graph) {
    DirectedCycle checkCycle = new DirectedCycle(graph);
    if (checkCycle.hasCycle()) {
      StdOut.println("Cycle exists");
      return;
    }

    order = new Stack<>();
    rank = new int[graph.V()];
    visited = new boolean[graph.V()];

    for (int v = 0; v < graph.V(); v++)
      rank[v] = -1;

    for (int v = 0; v < graph.V(); v++) {
      if (!visited[v])
        dfs(graph, v);
    }

    int vRank = 0;
    for (int v : order) {
      rank[v] = vRank++;
    }
  }

  public Iterable<Integer> order() { return order; }

  public int rank(int v) { return rank[v]; }

  public boolean hasOrder() { return !(order == null); }

  private void dfs(Digraph g, int v) {
    visited[v] = true;
    for (int w : g.adj(v)) {
      if (!visited[w]) {
        dfs(g, w);
      }
    }
    order.push(v);
  }

  public static void main(String args[]) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);
    // StdOut.println(G);

    // Print out order
    Topological top = new Topological(G);
    if (top.hasOrder()) {
      StdOut.print("Topological order: ");
      for (int v : top.order()) {
        StdOut.print(v + " ");
      }
      StdOut.println();
    }
  }
}
