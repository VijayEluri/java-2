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
        int first = 0;
        for (int i = 0; i < n; i++) {
            if (suffixArray.index(i) == 0)
            {
                first = i;
            }
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < n; i++)
        {
            int idx = (suffixArray.index(i) + n-1) % n;
            char c = s.charAt(idx);
            BinaryStdOut.write(c, 8);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform()
    {
        // read the input
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int n = s.length();
        char[] list = s.toCharArray();
        int[] translate = new int[n];
        int[] sums = new int[R + 1];
        for(int k=0; k < list.length; k++){
            sums[list[k]+1]++;
        }
        for(int k=1; k < sums.length; k++){
            sums[k] += sums[k-1];
        }
        for(int k=0; k < list.length; k++){
            int index = sums[list[k]]++;
            translate[index] = k;
        }
        char[] inverse = new char[list.length];
        for (int k = 0; k <list.length; k++) {
            first = translate[first];
            inverse[k] = list[first];
        }
        for (char c : inverse) {
            BinaryStdOut.write(c, 8);
        }
        BinaryStdOut.close();
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
        if (args == null || args.length < 1) {
            throw new IllegalArgumentException("Input args null");
        }

        if      (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}