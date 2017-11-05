import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.Digraph;

public class WordNet {

    private final SeparateChainingHashST<String, Integer> nounToSynNetId;
    private final SeparateChainingHashST<Integer, String> synNetIdToSynNet;
    private final Digraph wordNetGraph;
    private final SAP sap;

    // Constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {
        nounToSynNetId = new SeparateChainingHashST<>();
        synNetIdToSynNet = new SeparateChainingHashST<>();

        In synsetIn = new In(synsets);

        // Number of synsets
        int synsetNum = 0;
        while (synsetIn.hasNextLine())
        {
            String synsetLine = synsetIn.readLine();
            String[] tokens = synsetLine.split(",");

            int id = Integer.parseInt(tokens[0].trim());
            synNetIdToSynNet.put(id, tokens[1]);

            String[] nouns =  tokens[1].split(" ");
            for (String noun : nouns)
            {
                nounToSynNetId.put(noun, id);
            }
            synsetNum++;
        }
        synsetIn.close();

        wordNetGraph = new Digraph(synsetNum);
        In hypernymIn = new In(hypernyms);
        while (hypernymIn.hasNextLine())
        {
            String hypernymLine = hypernymIn.readLine();
            String[] tokens = hypernymLine.split(",");

            int v = Integer.parseInt(tokens[0].trim());
            for (int i = 1; i < tokens.length; i++)
            {
                if (tokens[i] != null && !(tokens[i].trim().isEmpty()))
                {
                    int w = Integer.parseInt(tokens[i]);
                    wordNetGraph.addEdge(v, w);
                }
            }
        }
        hypernymIn.close();
        sap = new SAP(wordNetGraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
        return nounToSynNetId.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        if (word == null || word.trim().isEmpty())
        {
            throw new IllegalArgumentException("Illegal argument word");
        }

        return nounToSynNetId.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        if (nounA == null || nounA.trim().isEmpty() || !nounToSynNetId.contains(nounA))
        {
            throw new IllegalArgumentException("Illegal argument nounA");
        }

        if (nounB == null || nounB.trim().isEmpty() || !nounToSynNetId.contains(nounB))
        {
            throw new IllegalArgumentException("Illegal argument nounB");
        }

        int v = nounToSynNetId.get(nounA);
        int w = nounToSynNetId.get(nounB);
        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        if (nounA == null || nounA.trim().isEmpty() || !nounToSynNetId.contains(nounA))
        {
            throw new IllegalArgumentException("Illegal argument nounA");
        }

        if (nounB == null || nounB.trim().isEmpty() || !nounToSynNetId.contains(nounB))
        {
            throw new IllegalArgumentException("Illegal argument nounB");
        }
        int v = nounToSynNetId.get(nounA);
        int w = nounToSynNetId.get(nounB);
        int ancestorId = sap.ancestor(v, w);
        return synNetIdToSynNet.get(ancestorId);
    }

    // Do unit testing of this class
    public static void main(String[] args)
    {
        String synsetsFileName = args[0];
        String hypernymsFileName = args[1];
        WordNet wordNet = new WordNet(synsetsFileName, hypernymsFileName);

        String nounA = args[2];
        String nounB = args[3];
        StdOut.printf("The distance between %s and %s is %3d", nounA, nounB, wordNet.distance(nounA, nounB));
        StdOut.printf("The common ancestor of %s and %s is %s", nounA, nounB, wordNet.sap(nounA, nounB));
    }
}
