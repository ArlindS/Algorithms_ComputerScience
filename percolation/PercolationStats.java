import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    private static final double CON = 1.96;
    private final double[] data;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) throw new IllegalArgumentException("n and trials must be greater than 1");

        int total = trials;
        data = new double[trials];
        while (trials > 0) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int ranRow = StdRandom.uniform(1, n + 1);
                int ranCol = StdRandom.uniform(1, n + 1);
                p.open(ranRow, ranCol);
            }
            double nu = p.numberOfOpenSites();
            double d = (n * n);
            data[total - trials] = nu / d;
            trials--;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(data);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(data);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CON * stddev()) / Math.sqrt(data.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CON * stddev()) / Math.sqrt(data.length));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = 0;
        int trials = 0;
        if (args.length > 0) {
            try {
                n = Integer.parseInt(args[0]);
                trials = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer.");
            }
        }

        PercolationStats s = new PercolationStats(n, trials);
        System.out.println("Mean = \t\t\t\t" + s.mean() + "\nStddev = \t\t\t" + s.stddev());
        System.out.println("95% confidence interval = \t[" + s.confidenceLo() + ", " + s.confidenceHi() + "]");
    }
}
