import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.princeton.cs.algs4.StdDraw;

public class Board {
    private final int n;
    private final int[][] tiles;
    private final int hamming;
    private final int manhattan;

    public Board(int[][] blocks) {
        if (blocks == null) throw new IllegalArgumentException("null argument");
        this.n = blocks.length;
        tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            if (blocks[i] == null || blocks[i].length != n) throw new IllegalArgumentException("invalid blocks");
            for (int j = 0; j < n; j++) tiles[i][j] = blocks[i][j];
        }
        int ham = 0;
        int man = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = tiles[i][j];
                if (val == 0) continue;
                int goalRow = (val - 1) / n;
                int goalCol = (val - 1) % n;
                if (goalRow != i || goalCol != j) ham++;
                man += Math.abs(goalRow - i) + Math.abs(goalCol - j);
            }
        }
        this.hamming = ham;
        this.manhattan = man;
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return hamming == 0;
    }

    public Board twin() {
        // swap any pair of tiles (not involving blank)
        int[][] copy = copyTiles();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (copy[i][j] != 0 && copy[i][j+1] != 0) {
                    int t = copy[i][j];
                    copy[i][j] = copy[i][j+1];
                    copy[i][j+1] = t;
                    return new Board(copy);
                }
            }
        }
        return new Board(copy);
    }

    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.n != that.n) return false;
        for (int i = 0; i < n; i++) if (!Arrays.equals(this.tiles[i], that.tiles[i])) return false;
        return true;
    }

    public Iterable<Board> neighbors() {
        List<Board> nb = new ArrayList<>();
        int blankR = -1, blankC = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) if (tiles[i][j] == 0) { blankR = i; blankC = j; break; }
            if (blankR != -1) break;
        }
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int[] d : dirs) {
            int nr = blankR + d[0];
            int nc = blankC + d[1];
            if (nr >= 0 && nr < n && nc >= 0 && nc < n) {
                int[][] copy = copyTiles();
                copy[blankR][blankC] = copy[nr][nc];
                copy[nr][nc] = 0;
                nb.add(new Board(copy));
            }
        }
        return nb;
    }

    private int[][] copyTiles() {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) System.arraycopy(tiles[i], 0, copy[i], 0, n);
        return copy;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(String.format(" %2d", tiles[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // simple unit test
        int[][] a = {{0,1,3},{4,2,5},{7,8,6}};
        Board b = new Board(a);
        System.out.println(b);
        System.out.println("dimension: " + b.dimension());
        System.out.println("hamming: " + b.hamming());
        System.out.println("manhattan: " + b.manhattan());
        for (Board nb : b.neighbors()) System.out.println(nb);
    }
}
