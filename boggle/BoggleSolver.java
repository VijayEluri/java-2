import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.TST;

// Implement a Boggle solver, immutable data type, that finds all valid words in a given Boggle board,
// using a given dictionary.
public class BoggleSolver {
    TST<Boolean> dict;

    // Initializes the data structure using the given array of strings as the dictionary.
    // Assume each word in the dictionary contains only the uppercase letters A through Z.
    public BoggleSolver(String[] dictionary)
    {
        dict = new TST<>();
        for (String word : dictionary)
        {
            dict.put(word, true);
        }
    }

    private class Cube
    {
        // row i, column j
        int i;
        int j;
        Cube(int i, int j)
        {
            this.i = i;
            this.j = j;
        }

        Iterable<Cube> adj(BoggleBoard board, boolean[][] visited)
        {
            Queue<Cube> queue = new Queue<>();
            if (i == 0)
            {
                if (j == 0)
                {
                    if (!visited[i][j+1]) queue.enqueue(new Cube(i, j+1));
                    if (!visited[i+1][j]) queue.enqueue(new Cube(i+1, j));
                    if (!visited[i+1][j+1]) queue.enqueue(new Cube(i+1, j+1));
                    return queue;
                }
                if (j == board.cols()-1)
                {
                    queue.enqueue(new Cube(i, j-1));
                    queue.enqueue(new Cube(i+1, j-1));
                    queue.enqueue(new Cube(i+1, j));
                    return queue;
                }

                queue.enqueue(new Cube(i, j-1));
                queue.enqueue(new Cube(i, j+1));
                queue.enqueue(new Cube(i+1, j-1));
                queue.enqueue(new Cube(i+1, j));
                queue.enqueue(new Cube(i+1, j+1));
                return queue;
            }

            if (i == board.rows()-1)
            {
                if (j == 0)
                {
                    queue.enqueue(new Cube(i, j+1));
                    queue.enqueue(new Cube(i-1, j));
                    queue.enqueue(new Cube(i-1, j+1));
                    return queue;
                }
                if (j == board.cols()-1)
                {
                    queue.enqueue(new Cube(i, j-1));
                    queue.enqueue(new Cube(i-1, j-1));
                    queue.enqueue(new Cube(i-1, j));
                    return queue;
                }

                queue.enqueue(new Cube(i, j-1));
                queue.enqueue(new Cube(i, j+1));
                queue.enqueue(new Cube(i-1, j-1));
                queue.enqueue(new Cube(i-1, j));
                queue.enqueue(new Cube(i-1, j+1));
                return queue;
            }

            // i-1 and i + 1 are valid
            if (j == 0)
            {
                queue.enqueue(new Cube(i-1, j));
                queue.enqueue(new Cube(i-1, j+1));
                queue.enqueue(new Cube(i, j+1));
                queue.enqueue(new Cube(i+1, j));
                queue.enqueue(new Cube(i+1, j+1));
                return queue;
            }


            if (j == board.cols()-1)
            {
                queue.enqueue(new Cube(i-1, j-1));
                queue.enqueue(new Cube(i-1, j));
                queue.enqueue(new Cube(i, j-1));
                queue.enqueue(new Cube(i+1, j-1));
                queue.enqueue(new Cube(i+1, j));
                return queue;
            }

            // i-1 and i + 1 are valid
            // j -1 and j+1 are valid as well
            queue.enqueue(new Cube(i-1, j-1));
            queue.enqueue(new Cube(i-1, j));
            queue.enqueue(new Cube(i-1, j+1));
            queue.enqueue(new Cube(i, j-1));
            queue.enqueue(new Cube(i, j+1));
            queue.enqueue(new Cube(i+1, j-1));
            queue.enqueue(new Cube(i+1, j));
            queue.enqueue(new Cube(i+1, j+1));
            return queue;
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        int rows = board.rows();
        int cols = board.cols();
        boolean[][] visited = new boolean[rows][cols];
        Queue<String> allValidWords = new Queue<>();

        // for each letter c:
        //   marked c as visited // choose
        //   for each neighboring letter next to c:
        //      explore all words that could start with c's letter. // explore
        //   un-mark c as visited. // un-choose
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                char c = board.getLetter(i, j);
                dfs(board, i, j, visited, allValidWords);
            }
        }
        return allValidWords;
    }



    private void dfs(BoggleBoard board, int i, int j, boolean[][] visited , Queue<String> allValidWords)
    {
        visited[i][j] = true;
        char c = board.getLetter(i, j);
        Cube cube = new Cube(i, j);
        for (Cube x : cube.adj(board, visited))
        {
            dfs(board, x.i, x.j, visited, allValidWords);
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // Assume the word contains only the uppercase letters A through Z.
    public int scoreOf(String word)
    {
        if (word == null)
        {
            throw new IllegalArgumentException();
        }
        if (!dict.contains(word))
        {
            return 0;
        }

        switch (word.length())
        {
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
}
