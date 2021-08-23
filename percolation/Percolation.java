import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private final WeightedQuickUnionUF uf;
    private final int loc;
    private final boolean[] open;
    private int numOpen = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) throw new IllegalArgumentException("n must be grater than 0");

        loc = n;
        uf = new WeightedQuickUnionUF((n * n) + 3);
        for (int i = 1; i <= n; i++) {
            uf.union(0, i);
            uf.union((n * n) + 1, (n * n) - n + i);
        }
        open = new boolean[n * n + 1];
    }

    // two dimensional point to 1-D
    private int twoToOne(int row, int col) {
        return (loc * row) - loc + col;
    }

    private boolean validIndex(int row, int col) {
        if (row < 1 || col < 1 || row > loc || col > loc)
            return false;

        return twoToOne(row, col) > 0 && twoToOne(row, col) < open.length;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!validIndex(row, col))
            throw new IllegalArgumentException("Index for (" + row + ", " + col + ") is out of bounds.");

        int p = twoToOne(row, col);
        if (!isOpen(row, col)) {
            open[p] = true;
            numOpen++;
            if (validIndex(row - 1, col) && open[twoToOne(row - 1, col)])
                uf.union(p, p - loc);
            if (validIndex(row + 1, col) && open[twoToOne(row + 1, col)])
                uf.union(p, p + loc);
            if (validIndex(row, col + 1) && open[twoToOne(row, col + 1)])
                uf.union(p, p + 1);
            if (validIndex(row, col - 1) && open[twoToOne(row, col - 1)])
                uf.union(p, p - 1);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!validIndex(row, col)) {
            throw new IllegalArgumentException("Index for (" + row + ", " + col + ") is out of bounds.");
        }

        int p = twoToOne(row, col);
        return open[p];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!validIndex(row, col))
            throw new IllegalArgumentException("Index for (" + row + ", " + col + ") is out of bounds.");

        int p = twoToOne(row, col);
        return uf.find(p) == uf.find(0) && isOpen(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        if (!isOpen(1, 1) && loc == 1)
            return false;
        return uf.find(0) == uf.find((loc * loc) + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        /*
        Percolation p = new Percolation(2);
        System.out.println(p.isFull(1, 1));
        p.open(1, 1);
        System.out.println(p.percolates());
        p.open(1, 2);
        p.open(2, 2);
        p.open(3, 2);
        System.out.println(p.numberOfOpenSites());
        System.out.println(p.percolates());
         */
    }
}
