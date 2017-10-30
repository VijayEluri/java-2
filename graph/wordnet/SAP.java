import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;

public class SAP {

  private Digraph g;

  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) { g = G; }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {

    // vd[x] = distance from v to x
    int vd[] = new int[g.V()];

    // marked_v[x] = vertex x visited
    boolean marked_v[] = new boolean[g.V()];

    Queue<Integer> q = new Queue<>();
    q.enqueue(v);
    vd[v] = 0;
    while (!q.isEmpty()) {
      int x = q.dequeue();
      for (int y : g.adj(x)) {
        if (!marked_v[y]) {
          q.enqueue(y);
          marked_v[y] = true;
          vd[y] = vd[x] + 1;
        }
      }
    }

    int wd[] = new int[g.V()];

    // marked_w[x] = vertex x visited
    boolean marked_w[] = new boolean[g.V()];
    q.enqueue(w);
    wd[w] = 0;
    while (!q.isEmpty()) {
      int x = q.dequeue();

      // check if x = an ancestor of v
      if (marked_v[x]) {
        return vd[x] + wd[x];
      }

      for (int y : g.adj(x)) {
        if (!marked_w[y]) {
          q.enqueue(y);
          marked_w[y] = true;
          wd[y] = wd[x] + 1;
        }
      }
    }
    return -1;
  }

  // a common ancestor of v and w that participates in a shortest ancestral
  // path; -1 if no such path
  public int ancestor(int v, int w) { return 0; }

  // length of shortest ancestral path between any vertex in v and any
  // vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) { return 0; }

  // a common ancestor that participates in shortest ancestral path; -1 if no
  // such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) { return 0; }

  // do unit testing of this class
  public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    while (!StdIn.isEmpty()) {
      int v = StdIn.readInt();
      int w = StdIn.readInt();
      int length = sap.length(v, w);
      // int ancestor = sap.ancestor(v, w);
      // StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
      StdOut.printf("length = %d", length);
    }
  }
}
