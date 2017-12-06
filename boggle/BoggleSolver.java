import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

// Boggle solver, an immutable data type, that finds all valid words for given Boggle board
public class BoggleSolver {
    private class Cube {
        // row i, column j
        int i;
        int j;

        Cube(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    private Tst<Boolean> dict;

    // Initializes the data structure using the given array of strings as the dictionary.
    // Assume each word in the dictionary contains only the uppercase letters A through Z.
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null)
        {
            throw new IllegalArgumentException();
        }
        dict = new Tst<>();
        for (String word : dictionary) {
            dict.put(word, true);
        }
    }

    private Queue<Cube>[][] computeAdj(BoggleBoard board) {
        Queue<Cube>[][] adj = (Queue<Cube>[][]) new Queue[board.rows()][board.cols()];
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                Queue<Cube> queue = new Queue<>();
                if (i > 0) {
                    queue.enqueue(new Cube(i - 1, j));
                    if (j > 0) {
                        queue.enqueue(new Cube(i - 1, j - 1));
                    }
                    if (j < board.cols() - 1) {
                        queue.enqueue(new Cube(i - 1, j + 1));
                    }
                }

                if (i < board.rows() - 1) {
                    queue.enqueue(new Cube(i + 1, j));
                    if (j > 0) {
                        queue.enqueue(new Cube(i + 1, j - 1));
                    }

                    if (j < board.cols() - 1) {
                        queue.enqueue(new Cube(i + 1, j + 1));
                    }
                }
                if (j > 0) {
                    queue.enqueue(new Cube(i, j - 1));
                }
                if (j < board.cols() - 1) {
                    queue.enqueue(new Cube(i, j + 1));
                }
                adj[i][j] = queue;
            }
        }
        return adj;
    }

    // Returns the set of all valid words in the given Boggle board.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null)
        {
            throw new IllegalArgumentException();
        }
        int rows = board.rows();
        int cols = board.cols();
        Queue<Cube>[][] adj = computeAdj(board);
        SET<String> allValidWords = new SET<>();

        // for each letter c:
        //   marked c as visited // choose
        //   for each neighboring letter next to c:
        //      explore all words that could start with c's letter. // explore
        //   un-mark c as visited. // un-choose
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boolean[][] visited = new boolean[rows][cols];
                StringBuilder prefix = new StringBuilder(11);
                dfs(board, i, j, adj, visited, allValidWords, prefix);
            }
        }
        return allValidWords;
    }

    private void dfs(BoggleBoard board, int i, int j, Iterable<Cube>[][] adj, boolean[][] visited, SET<String> allValidWords, StringBuilder prefix) {
        visited[i][j] = true;
        char c = board.getLetter(i, j);
        if (c == 'Q') {
            prefix.append("QU");
        } else {
            prefix.append(c);
        }
        String testWord = prefix.toString();
        if (dict.isExists(testWord)) {
            if (testWord.length() > 2 && dict.get(testWord) != null) {
                allValidWords.add(testWord);
            }
            for (Cube x : adj[i][j]) {
                if (!visited[x.i][x.j]) {
                    dfs(board, x.i, x.j, adj, visited, allValidWords, prefix);
                    visited[x.i][x.j] = false;
                    prefix.deleteCharAt(prefix.length() - 1);
                    if (prefix.charAt(prefix.length() - 1) == 'Q') {
                        prefix.deleteCharAt(prefix.length() - 1);
                    }
                }
            }
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // Assume the word contains only the uppercase letters A through Z.
    public int scoreOf(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        if (!dict.contains(word)) {
            return 0;
        }

        switch (word.length()) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}