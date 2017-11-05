import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // Given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        // d[i] =  dist(Ai, A1)   +   dist(Ai, A2)   +   ...   +   dist(Ai, An)
        int[] d = new int[nouns.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (j != i) {
                    d[i] += wordnet.distance(nouns[i], nouns[j]);
                }
            }
        }
        int maxIdx = findMaxIndex(d);
        return nouns[maxIdx];
    }

    private int findMaxIndex(int[] d) {
        int maxIdx = 0;
        for (int i = 0; i < d.length; i++) {
            if (d[i] > d[maxIdx]) {
                maxIdx = i;
            }
        }
        return maxIdx;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
