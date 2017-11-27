import edu.princeton.cs.algs4.*;

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
        int x = teams.get(team);
        LinearProbingHashST<String, Integer> vertices = makeNetworkVertices(n, x);
        int s = vertices.get("s");
        int t = vertices.get("t");

        FlowNetwork network = new FlowNetwork(vertices.size());

        for (int i = 0; i < n; i++) {
            if (i != x) {
                for (int j = i + 1; j < n; j++) {
                    if (j != x) {
                        int gameIj = vertices.get(i + "-" + j);
                        network.addEdge(new FlowEdge(s, gameIj, game[i][j]));

                        int teamI = vertices.get("t" + i);
                        int teamJ = vertices.get("t" + j);

                        network.addEdge(new FlowEdge(gameIj, teamI, Double.POSITIVE_INFINITY));
                        network.addEdge(new FlowEdge(gameIj, teamJ, Double.POSITIVE_INFINITY));
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (i != x) {
                int teamI = vertices.get("t" + i);
                network.addEdge(new FlowEdge(teamI, t, win[x] + remain[x] - win[i]));
            }
        }
        StdOut.println(network.toString());

        return false;
    }

    // n is the number of teams
    // x is the team to check for elimination.
    // Return map of vertex name and vertex number for all vertices of the test FlowNetwork
    private LinearProbingHashST<String, Integer> makeNetworkVertices(int n, int x) {
        LinearProbingHashST<String, Integer> vertices = new LinearProbingHashST<>();
        int vertex_id = 0;
        vertices.put("s", vertex_id++);
        for (int i = 0; i < n; i++) {
            if (i != x) {
                for (int j = i + 1; j < n; j++) {
                    if (j != x) {
                        String vertex_name = i + "-" + j;
                        vertices.put(vertex_name, vertex_id++);
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (i != x) {
                vertices.put("t" + i, vertex_id++);
            }
        }
        vertices.put("t", vertex_id++);
        return vertices;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        return null;
    }

    // Do unit testing with assert
    public static void main(String[] args) {
        String filename = "teams4.txt";
        BaseballElimination test = new BaseballElimination(filename);
        test.isEliminated("Philadelphia");
    }
}
