import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {

//	   public PercolationStats(int n, int trials)    // perform trials independent experiments on an n-by-n grid
//	   public double mean()                          // sample mean of percolation threshold
//	   public double stddev()                        // sample standard deviation of percolation threshold
//	   public double confidenceLo()                  // low  endpoint of 95% confidence interval
//	   public double confidenceHi()                  // high endpoint of 95% confidence interval
//
//	   public static void main(String[] args)        // test client (described below)
	
	private double[] thresholds;
	private int trials;
	private Percolation percolation;
	
	public PercolationStats(int n, int trials) {
		if (n <= 0 || trials <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.trials = trials;
		thresholds = new double[trials];
		for (int i = 0; i < trials; i++) {
			percolation = new Percolation(n);
			while (!percolation.percolates()) {
				int row = StdRandom.uniform(1, n + 1);
				int col = StdRandom.uniform(1, n + 1);
				if (percolation.isOpen(row, col)) {
					continue;
				}
				percolation.open(row, col);
			}
			
			
			thresholds[i] = (double) percolation.numberOfOpenSites() / (n*n);
			
		}
	}
	
	public double mean() {
		return StdStats.mean(thresholds);
	}
	
	public double stddev() {
		return StdStats.stddev(thresholds);
	}
	
	public double confidenceLo() {
		return mean() - 1.96 * stddev() / Math.sqrt(trials);
	}
	
	public double confidenceHi() {
		return mean() + 1.96 * stddev() / Math.sqrt(trials);
	}
	
	public static void main(String[] args) { // test client (optional)
		int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats stats =  new  PercolationStats(n, t);
        StdOut.println( " mean = "  + stats.mean());
        StdOut.println( " stddev = "  + stats.stddev());
        StdOut.println( " 95% confidence interval = "  + stats.confidenceLo() +  " , "  + stats.confidenceHi());
	}
	
}
