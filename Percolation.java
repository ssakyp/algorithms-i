import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final boolean[] open;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufNoBottom;
    private final int top;
    private final int bottom;
    private int openCount;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");
        this.n = n;
        int size = n * n;
        open = new boolean[size];
        uf = new WeightedQuickUnionUF(size + 2); // + top + bottom
        ufNoBottom = new WeightedQuickUnionUF(size + 1); // + top
        top = size;
        bottom = size + 1;
        openCount = 0;
    }

    public void open(int row, int col) {
        validate(row, col);
        int idx = xyTo1D(row, col);
        if (open[idx]) return;
        open[idx] = true;
        openCount++;

        // connect to neighbors
        if (row == 1) {
            uf.union(idx, top);
            ufNoBottom.union(idx, top);
        }
        if (row == n) uf.union(idx, bottom);

        // up
        if (row > 1 && isOpen(row - 1, col)) {
            uf.union(idx, xyTo1D(row - 1, col));
            ufNoBottom.union(idx, xyTo1D(row - 1, col));
        }
        // down
        if (row < n && isOpen(row + 1, col)) {
            uf.union(idx, xyTo1D(row + 1, col));
            ufNoBottom.union(idx, xyTo1D(row + 1, col));
        }
        // left
        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(idx, xyTo1D(row, col - 1));
            ufNoBottom.union(idx, xyTo1D(row, col - 1));
        }
        // right
        if (col < n && isOpen(row, col + 1)) {
            uf.union(idx, xyTo1D(row, col + 1));
            ufNoBottom.union(idx, xyTo1D(row, col + 1));
        }
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return open[xyTo1D(row, col)];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        int idx = xyTo1D(row, col);
        return open[idx] && ufNoBottom.find(idx) == ufNoBottom.find(top);
    }

    public int numberOfOpenSites() {
        return openCount;
    }

    public boolean percolates() {
        return uf.find(top) == uf.find(bottom);
    }

    private int xyTo1D(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    private void validate(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("row and col must be between 1 and n");
    }

    public static void main(String[] args) {
        // optional client; leave empty to satisfy API
    }
}
