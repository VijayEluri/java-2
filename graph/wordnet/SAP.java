// File: SAP.java
// Desc: Data type to compute the common ancestor of 2 vertices or 2 set of vertices

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private Digraph g;
    private int distFromV[];
    private int distFromW[];

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        g = G;
        distFromV = new int[g.V()];
        distFromW = new int[g.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        reset();
        computeDistances(v, distFromV, null);
        int ancestor = computeDistances(w, distFromW, distFromV);
        if (ancestor > -1) {
            return distFromV[ancestor] + distFromW[ancestor];
        }
        return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        reset();
        computeDistances(v, distFromV, null);
        int ancestor = computeDistances(w, distFromW, distFromV);
        if (ancestor > -1) {
            return ancestor;
        }
        return -1;
    }

    // length of shortest ancestral path between any vertex in v and any
    // vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no
    // such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    // Reset
    private void reset() {
        for (int x = 0; x < g.V(); x++) {
            distFromV[x] = -1;
            distFromW[x] = -1;
        }
    }

    // Compute distance from x to other vertices using BFS
    // and store values in distFromX
    // 1. if distFromY is null then the function run BFS for all reachable vertices
    //    then return -1
    // 2. if distFromY is given then 2 possibilities
    //    if reachable vertex v from x is not reachable from y
    //      i.e. distFromY[v] = -1 for all v reachable from x
    //        then return -1
    //    if one reachable vertex v from x is also reachable from y
    //      i.e. distFromY[v] > -1 for any v reachable from y
    //        then return v
    private int computeDistances(int x, int distFromX[], int distFromY[]) {
        Queue<Integer> q = new Queue<>();
        q.enqueue(x);
        distFromX[x] = 0;
        while (!q.isEmpty()) {
            int v = q.dequeue();
            if (distFromY != null) {
                assert distFromY.length == g.V();
                if (distFromY[v] > -1) {
                    return v;
                }
            }
            for (int w : g.adj(v)) {
                // vertex w hasn't been visited
                if (distFromX[w] == -1) {
                    q.enqueue(w);
                    distFromX[w] = distFromX[v] + 1;
                }
            }
        }
        return -1;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}