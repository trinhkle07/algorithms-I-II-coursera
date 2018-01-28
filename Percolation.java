import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	
	WeightedQuickUnionUF uf;
	private boolean[][] opened;
	private int gridDim;
	private int numOpenSites = 0;
	
	public Percolation(int n) { // create n-by-n grid, with all sites blocked
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		gridDim = n;
		opened = new boolean[n][n];
		uf = new WeightedQuickUnionUF(n*n + 2); // virtual sites are at n*n and n*n + 1
	}
	
	
	public    void open(int row, int col) { // open site (row, col) if it is not open already
		
		if (row < 1 || row > gridDim || col < 1 || col > gridDim) {
			throw new IllegalArgumentException();
		}
		
		if (!isOpen(row, col)) {
			
			numOpenSites++;
			
			opened[row-1][col-1] = true;
			
			if (row == 1) {
				uf.union(index(row, col), gridDim * gridDim);
			}
			
			if (col == gridDim) {
				uf.union(index(row, col), gridDim * gridDim + 1);
			}
			
			if (col > 1 && isOpen(row, col - 1)) {
				uf.union(index(row, col), index(row, col - 1));
			}
			
			if (col < gridDim && isOpen(row, col + 1)) {
				uf.union(index(row, col), index(row, col + 1));
			}
			
			if (row > 1 && isOpen(row - 1, col)) {
				uf.union(index(row, col), index(row -1, col));
			}
			
			if (row < gridDim && isOpen(row + 1, col)) {
				uf.union(index(row, col), index(row + 1, col));
			}
		}
	}
	
	
	public boolean isOpen(int row, int col) { // is site (row, col) open?
		
		if (row < 1 || row > gridDim || col < 1 || col > gridDim) {
			throw new IllegalArgumentException();
		}
		
		return opened[row-1][col-1];
	}
	
	private int index(int row, int col) {
		return (row - 1) * gridDim + (col - 1);
	}
	
	public boolean isFull(int row, int col) { // is site (row, col) full?
		
		if (row < 1 || row > gridDim || col < 1 || col > gridDim) {
			throw new IllegalArgumentException();
		}
		
		return uf.connected(index(row, col), gridDim * gridDim);
	}
	
	
	public     int numberOfOpenSites() { // number of open sites
		return numOpenSites;
	}
	
	public boolean percolates() { // does the system percolate?
		return uf.connected(gridDim*gridDim, gridDim * gridDim + 1);
	}


}
