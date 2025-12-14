import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final boolean solvable;
    private final SearchNode goalNode;

    private static class SearchNode implements Comparable<SearchNode> {
        final Board board;
        final int moves;
        final SearchNode prev;
        final int priority; // manhattan + moves

        SearchNode(Board b, int m, SearchNode p) {
            this.board = b;
            this.moves = m;
            this.prev = p;
            this.priority = b.manhattan() + m;
        }

        public int compareTo(SearchNode other) {
            if (this.priority != other.priority) return this.priority - other.priority;
            return this.board.manhattan() - other.board.manhattan();
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("null initial");

        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();

        pq.insert(new SearchNode(initial, 0, null));
        pqTwin.insert(new SearchNode(initial.twin(), 0, null));

        SearchNode found = null;
        boolean foundSolvable = false;

        while (true) {
            // step original
            if (pq.isEmpty()) break;
            SearchNode node = pq.delMin();
            if (node.board.isGoal()) { found = node; foundSolvable = true; break; }
            for (Board nb : node.board.neighbors()) {
                if (node.prev != null && nb.equals(node.prev.board)) continue;
                pq.insert(new SearchNode(nb, node.moves + 1, node));
            }

            // step twin
            if (pqTwin.isEmpty()) break;
            SearchNode nodeT = pqTwin.delMin();
            if (nodeT.board.isGoal()) { found = null; foundSolvable = false; break; }
            for (Board nb : nodeT.board.neighbors()) {
                if (nodeT.prev != null && nb.equals(nodeT.prev.board)) continue;
                pqTwin.insert(new SearchNode(nb, nodeT.moves + 1, nodeT));
            }
        }

        this.solvable = foundSolvable;
        this.goalNode = found;
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return solvable ? goalNode.moves : -1;
    }

    public Iterable<Board> solution() {
        if (!solvable) return null;
        Deque<Board> path = new ArrayDeque<>();
        SearchNode cur = goalNode;
        while (cur != null) {
            path.addFirst(cur.board);
            cur = cur.prev;
        }
        return path;
    }

    public static void main(String[] args) {
        if (args == null || args.length == 0) return;
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);
        if (!solver.isSolvable()) StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board b : solver.solution()) StdOut.println(b);
        }
    }
}
