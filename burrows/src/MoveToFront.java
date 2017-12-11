import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        int n = s.length();
        char[] list = s.toCharArray();

        int[] index = new int[R];
        for (int k = 0; k < index.length; k++) {
            index[k] = k;
        }

        char[] copy = new char[n];
        for (int k = 0; k < n; k++) {
            if (list[k] < 0 || list[k] > 255) {
                throw new IllegalArgumentException("encode with error at index " + k + " value " + list[k]);
            }

            for (int j = 0; j < index.length; j++) {
                if (index[j] == list[k]) {
                    copy[k] = (char) j;
                    for (int i = j; i >= 1; i--) {
                        index[i] = index[i - 1];
                    }
                    index[0] = list[k];
                    break;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(copy[i], 8);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        String s = BinaryStdIn.readString();
        int n = s.length();
        char[] list = s.toCharArray();

        int[] index = new int[R];
        for (int k = 0; k < index.length; k++) {
            index[k] = k;
        }

        char[] copy = new char[list.length];
        for (int k = 0; k < list.length; k++) {
            int save = index[list[k]];
            copy[k] = (char) save;
            for (int j = list[k]; j > 0; j--) {
                index[j] = index[j - 1];
            }
            index[0] = save;
        }

        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(copy[i], 8);
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            throw new IllegalArgumentException("Input args null");
        }

        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
