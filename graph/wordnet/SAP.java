// File: SAP.java
// Desc: Data type to compute the common ancestor for two set of vertices
//       in a digraph

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;

// Class to compute Shortest Ancestral Path SAP
// for two set of synonym sets
public class SAP {
    private Digraph g;
    private int distFromV[];
    private int distFromW[];

    // Constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        g = G;
        distFromV = new int[g.V()];
        distFromW = new int[g.V()];
    }

    // Determine length of shortest ancestral path between v and w;
    // -1 if no such path
    public int length(int v, int w) {
        if (!(v >=0 && v < g.V())) {
            throw new IllegalArgumentException("Invalid vertex v " + v);
        }
        if (!(w >=0 && w < g.V())) {
            throw new IllegalArgumentException("Invalid vertex w " + w);
        }
        int ancestor_vertex = ancestor(v, w);
        if (ancestor_vertex == -1) {
            return -1;
        }
        return distFromV[ancestor_vertex] + distFromW[ancestor_vertex];
    }

    // Find common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        if (!(v >=0 && v < g.V())) {
            throw new IllegalArgumentException("Invalid vertex v " + v);
        }
        if (!(w >=0 && w < g.V())) {
            throw new IllegalArgumentException("Invalid vertex w " + w);
        }
        reset();
        Queue<Integer> q1 = new Queue<>();
        q1.enqueue(v);
        Queue<Integer> q2 = new Queue<>();
        q2.enqueue(w);
        computeDistances(q1, distFromV);
        computeDistances(q2, distFromW);
        return findAncestor(distFromV, distFromW);
    }

    // Determine length of shortest ancestral path between any vertex in v and any vertex in w;
    //  -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null) {
            throw new IllegalArgumentException("Iterable v is null");
        }
        if (w == null) {
            throw new IllegalArgumentException("Iterable w is null");
        }
        int ancestor_vertex = ancestor(v, w);
        if (ancestor_vertex == -1) {
            return -1;
        }
        return distFromV[ancestor_vertex] + distFromW[ancestor_vertex];
    }

    // Find a common ancestor that participates in shortest ancestral path;
    // -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null) {
            throw new IllegalArgumentException("Iterable v is null");
        }
        if (w == null) {
            throw new IllegalArgumentException("Iterable w is null");
        }
        reset();
        computeDistances(v, distFromV);
        computeDistances(w, distFromW);
        return findAncestor(distFromV, distFromW);
    }

    // Reset distance arrays to new state.
    private void reset() {
        for (int x = 0; x < g.V(); x++) {
            distFromV[x] = -1;
            distFromW[x] = -1;
        }
    }

    // Compute shortest distances from set x to other vertices using BFS
    // and store values in distFromX
    private void computeDistances(Iterable<Integer> s, int distFromX[]) {
        Queue<Integer> q = new Queue<>();
        for (int x : s) {
            q.enqueue(x);
            distFromX[x] = 0;
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : g.adj(v)) {
                // vertex w hasn't been visited
                if (distFromX[w] == -1) {
                    q.enqueue(w);
                    distFromX[w] = distFromX[v] + 1;
                }
            }
        }
    }

    // Find the shortest common ancestor hiven 2 computed distance arrays.
    private int findAncestor(final int dist1[], final int dist2[]) {
        int min_idx = -1;
        int min_dist = Integer.MAX_VALUE;
        for (int i = 0; i < dist1.length; i++) {
            if (dist1[i] > -1 && dist2[i] > -1 && min_dist > dist1[i] + dist2[i]) {
                min_dist = dist1[i] + dist2[i];
                min_idx = i;
            }
        }
        return min_idx;
    }

    // Do simple unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        while (!StdIn.isEmpty()) {
            StdOut.printf("Find ancestor for set(v1 v2) and set(w1, w2) \n");
            int v1 = StdIn.readInt();
            int v2 = StdIn.readInt();
            int w1 = StdIn.readInt();
            int w2 = StdIn.readInt();

            Queue<Integer> q1 = new Queue<>();
            q1.enqueue(v1);
            q1.enqueue(v2);
            Queue<Integer> q2 = new Queue<>();
            q2.enqueue(w1);
            q2.enqueue(w2);
            int length = sap.length(q1, q2);
            int ancestor = sap.ancestor(q1, q2);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}