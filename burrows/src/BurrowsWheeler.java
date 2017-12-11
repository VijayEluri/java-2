// Implementation of Burrows–Wheeler transform algorithm.
// https://www.cs.duke.edu/courses/cps100/fall09/assign/burrows/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // alphabet size of extended ASCII
    private static final int R = 256;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform()
    {
        // read the input
        String s = BinaryStdIn.readString();
        CircularSuffixArray suffixArray = new CircularSuffixArray(s);
        int n = suffixArray.length();
        int indices[] = new int[n];
        int first = 0;
        for (int i = 0; i < n; i++) {
            indices[i] = suffixArray.index(i);
            if (indices[i] == 0)
            {
                first = i;
            }
        }
        BinaryStdOut.write(first);
        for (int i : indices)
        {
            int idx = (indices[i] + n-1) % n;
            char c = s.charAt(idx);
            BinaryStdOut.write(c, 8);
        }
        BinaryStdOut.close();
        return;
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform()
    {
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args)
    {
        // to do quick sanity test, run this
        // java BurrowsWheeler - < abra.txt | java-algs4 edu.princeton.cs.algs4.HexDump  16
        // 00 00 00 03 41 52 44 21 52 43 41 41 41 41 42 42
        // 128 bits equivalent to:
        // 3
        // ARD!RCAAAABB

        if      (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}