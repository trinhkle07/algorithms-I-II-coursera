import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
	
	private Picture picture;
	double[][] energy;
	double[][] distTo;
	int[][] edgeTo;
	
	public SeamCarver(Picture picture) { // create a seam carver object based on the given picture
		this.picture = picture;
	}
	
	public Picture picture() { // current picture
		return picture;
	}
	
	public     int width() { // width of current picture
		return picture.width();
	}
	
	public     int height() { // height of current picture
		return picture.height();
	}
	
	public  double energy(int x, int y) { // energy of pixel at column x and row y
		validateIndices(x, y);
		if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
			return 1000;
		}
		
		double deltaX2 = computeDeltaX2(x, y);
		double deltaY2 = computeDeltaY2(x, y);
		
		return Math.sqrt(deltaX2 + deltaY2);
	}
	
	public   int[] findHorizontalSeam() { // sequence of indices for horizontal seam
        // Transpose picture.
        Picture original = picture;
        Picture transpose = new Picture(original.height(), original.width());

        for (int w = 0; w < transpose.width(); w++) {
            for (int h = 0; h < transpose.height(); h++) {
                transpose.set(w, h, original.get(h, w));
            }
        }

        this.picture = transpose;

        // call findVerticalSeam
        int[] seam = findVerticalSeam();

        // Transpose back.
        this.picture = original;

        return seam;
		
	}
	
	private void initialize() {
		int W = width();
		int H = height();
		energy = new double[H][W];
		distTo = new double[H][W];
		edgeTo = new int[H][W];
		
		// Initialize distTo array to infinity and 0 for first row (sources), and energy grid
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				distTo[y][x] = Integer.MAX_VALUE;
				energy[y][x] = energy(x, y);
			}
		}
		
		for (int x = 0; x < W; x++) {
			distTo[0][x] = 0;
		}
	}
	
	public   int[] findVerticalSeam() { // sequence of indices for vertical seam
		// // Find All Possible Shortest Paths in Topological Sort order.
		int W = width();
		int H = height();
		
		initialize();
		
		for (int y = 0; y < H - 1; y++) { // We don't do the last row since that is the end.
			for (int x = 0; x < W; x++) { 
				
				if (x > 0) {
					relax(x, y, x - 1, y + 1);
				}

				relax(x, y, x, y + 1);
				
				if (x < W - 1) {
					relax(x, y, x + 1, y + 1);
				}
			}
		}
		
		// Find minimum path
		double minDist = Double.POSITIVE_INFINITY;
		int minIdx = Integer.MIN_VALUE;
		for (int x = 0; x < W; x++) {
			if (distTo[H-2][x] < minDist) {
				minDist = distTo[H-2][x];
				minIdx = x;
			}
		}
		
		// edgeTo[x][y] is x position of previous row.
		int[] minPath = new int[H];
		minPath[H - 1] = minIdx - 1;
		for (int y = H - 2; y >= 0; y--) {
			minPath[y] = minIdx;
			minIdx = edgeTo[y][minIdx];
		}
		
		return minPath;
	}
	
	private void relax(int vx, int vy, int wx, int wy) {
		if (distTo[wy][wx] > distTo[vy][vx] + energy[wy][wx]) {
			distTo[wy][wx] = distTo[vy][vx] + energy[wy][wx];
			edgeTo[wy][wx] = vx;
		}
    }
	
	public    void removeHorizontalSeam(int[] seam) { // remove horizontal seam from current picture
		if (seam == null || seam.length != picture.width() || picture.height() <= 1) {
			throw new IllegalArgumentException();
		}
		
		// Need to check if not valid seam as well (either entry is outisde the range or 2 adj entries differ by more than 1
		int W = picture.width();
		int H = picture.height() - 1;
		
		Picture carved = new Picture(W, H);
		for (int x = 0; x < carved.width(); x++) {
			
			for (int y = 0; y < seam[x]; y++) {
				carved.setRGB(x, y, picture.getRGB(x, y));
			}
			
			for (int y = seam[x]; y < carved.height(); y++) {
				carved.setRGB(x, y, picture.getRGB(x, y + 1));
			}
		}
		
		picture = carved;
	}
	
	public    void removeVerticalSeam(int[] seam) { // remove vertical seam from current picture
		if (seam == null || seam.length != picture.height() || picture.width() <= 1) {
			throw new IllegalArgumentException();
		}
		
		// TODO: Check if not valid seam as well (either entry is outisde the range or 2 adj entries differ by more than 1
		int W = picture.width() - 1;
		int H = picture.height();
		
		Picture carved = new Picture(W, H);
		for (int y = 0; y < carved.height(); y++) {
			
			for (int x = 0; x < seam[y]; x++) {
				carved.setRGB(x, y, picture.getRGB(x, y));
			}
			
			for (int x = seam[y]; x < carved.width(); x++) {
				carved.setRGB(x, y, picture.getRGB(x + 1, y));
			}
		}
		
		picture = carved;
		
	}
	
	private double computeDeltaX2(int x, int y) {
		// Rx2 + Gx2 + Bx2
		int rgb1 = picture.getRGB(x + 1, y);
		int r1 = (rgb1 >> 16) & 0xFF;
		int g1 = (rgb1 >>  8) & 0xFF;
		int b1 = (rgb1 >>  0) & 0xFF;
		
		int rgb2 = picture.getRGB(x - 1, y);
		int r2 = (rgb2 >> 16) & 0xFF;
		int g2 = (rgb2 >>  8) & 0xFF;
		int b2 = (rgb2 >>  0) & 0xFF;
		
		double Rx2 = (r1 - r2) * (r1 - r2);
		double Gx2 = (g1 - g2) * (g1 - g2);
		double Bx2 = (b1 - b2) * (b1 - b2);
		
		return Rx2 + Gx2 + Bx2;
	}
	
	private double computeDeltaY2(int x, int y) {
		// Ry2 + Gy2 + By2
		int rgb1 = picture.getRGB(x, y + 1);
		int r1 = (rgb1 >> 16) & 0xFF;
		int g1 = (rgb1 >>  8) & 0xFF;
		int b1 = (rgb1 >>  0) & 0xFF;
		
		int rgb2 = picture.getRGB(x, y - 1);
		int r2 = (rgb2 >> 16) & 0xFF;
		int g2 = (rgb2 >>  8) & 0xFF;
		int b2 = (rgb2 >>  0) & 0xFF;
		
		double Ry2 = (r1 - r2) * (r1 - r2);
		double Gy2 = (g1 - g2) * (g1 - g2);
		double By2 = (b1 - b2) * (b1 - b2);
		
		return Ry2 + Gy2 + By2;
	}
	
	private void validateIndices(int x, int y) {
		if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) {
			throw new IllegalArgumentException();
		}
	}
	
}
