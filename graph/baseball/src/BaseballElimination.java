import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;

public class BaseballElimination {

    // number of teams in the division
    private int n;

    // map team name to team id
    private LinearProbingHashST<String, Integer> teams;

    // win[i] number of wins team i has
    private int[] win;

    // loss[i] number of losses team i has
    private int[] loss;

    // remain[i] is number of remaining games team i has
    private int[] remain;

    // game[i][j] number of remaining games between team i and team j
    private int[][] game;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        if (in.hasNextChar()) {
            n = in.readInt();
            teams = new LinearProbingHashST<>(n);
            win = new int[n];
            loss = new int[n];
            remain = new int[n];
            game = new int[n][n];

            for (int i = 0; i < n; i++) {
                teams.put(in.readString(), i);
                win[i] = in.readInt();
                loss[i] = in.readInt();
                remain[i] = in.readInt();
                for (int j = 0; j < n; j++) {
                    game[i][j] = in.readInt();
                }
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return teams.keys();
    }


    // number of wins for given team
    public int wins(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException();
        }
        return win[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException();
        }
        return loss[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException();
        }
        return remain[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teams.contains(team1) || !teams.contains(team2)) {
            throw new IllegalArgumentException();
        }
        return game[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        return null;
    }

    // Do unit testing with assert
    public static void main(String[] args) {
        String filename = "teams29.txt";
        BaseballElimination test = new BaseballElimination(filename);
        assert test.wins("Houston") == 114;
        assert test.wins("Sacramento") == 124;
        assert test.losses("Miami") == 112;
        assert test.losses("Orlando") == 133;
        assert test.against("Atlanta", "Boston") == 7;
    }
}
