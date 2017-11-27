import edu.princeton.cs.algs4.*;

// Compute team elimination using
// max flow problem formulation.
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
        if (triviallyEliminated(x) != null) {
            return true;
        }

        LinearProbingHashST<String, Integer> vertices = makeNetworkVertices(n, x);
        FlowNetwork network = new FlowNetwork(vertices.size());
        setupFlowNetwork(x, vertices, network);
        int s = vertices.get("s");
        int t = vertices.get("t");
        FordFulkerson maxflow = new FordFulkerson(network, s, t);
        for (FlowEdge edge : network.adj(s)) {
            if (edge.flow() < edge.capacity()) {
                return true;
            }
        }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        int x = teams.get(team);
        // team y trivially eliminated team x
        String teamY = triviallyEliminated(x);
        if (teamY != null) {
            Queue<String> queue = new Queue<>();
            queue.enqueue(teamY);
            return queue;
        }

        LinearProbingHashST<String, Integer> vertices = makeNetworkVertices(n, x);
        FlowNetwork network = new FlowNetwork(vertices.size());
        setupFlowNetwork(x, vertices, network);
        int s = vertices.get("s");
        int t = vertices.get("t");
        FordFulkerson maxflow = new FordFulkerson(network, s, t);
        for (FlowEdge edge : network.adj(s)) {
            if (edge.flow() < edge.capacity()) {
                // find the set R of teams that eliminate x
                System.out.println(maxflow.value());
                System.out.println(maxflow.inCut(0));
            }
        }
        return null;
    }

    // Set up flow network to test if x is eliminated.
    private void setupFlowNetwork(int x, LinearProbingHashST<String, Integer> vertices, FlowNetwork network)
    {
        int s = vertices.get("s");
        int t = vertices.get("t");
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
    }

    // Return the team name that trivially eliminated team x
    // Return null if x is not trivially eliminated
    private String triviallyEliminated(int x) {
        for (String team : teams.keys()) {
            int i = teams.get(team);
            if (i != x && win[x] + remain[x] < win[i]) {
                return team;
            }
        }
        return null;
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

    // Do unit testing with assert
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}