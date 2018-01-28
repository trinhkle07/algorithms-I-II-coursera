import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
	
	private int numSegs = 0;
	private LineSegment[] lineSegments;
	
	public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points
		
		List<LineSegment> segList = new ArrayList<LineSegment>();
		Arrays.sort(points);
		
		for (int i = 0; i < points.length - 4; i++) {
			for (int j = i + 1; j < points.length; j++) {
				for (int k = j + 1; k < points.length; k++) {
					for (int l = k + 1; l < points.length; l++) {
						Point p = points[i];
						Point q = points[j];
						Point r = points[k];
						Point s = points[l];
						if (p.slopeTo(q) == p.slopeTo(r) && p.slopeTo(r) == p.slopeTo(s)) {
							segList.add(new LineSegment(p, s));
							numSegs++;
						}
					}
				}
			}
		}
		
		lineSegments = Arrays.copyOf(segList.toArray(), segList.size(), LineSegment[].class);
		
		
	}
	
	public int numberOfSegments() { // the number of line segments
		return numSegs;
	}
	
	public LineSegment[] segments() { // the line segments
		return lineSegments;
	}
	
	public static void main(String[] args) {
		// read the n points from a file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    Point[] points = new Point[n];
	    for (int i = 0; i < n; i++) {
	        int x = in.readInt();
	        int y = in.readInt();
	        points[i] = new Point(x, y);
	    }

	    // draw the points
	    StdDraw.enableDoubleBuffering();
	    StdDraw.setXscale(0, 32768);
	    StdDraw.setYscale(0, 32768);
	    for (Point p : points) {
	        p.draw();
	    }
	    StdDraw.show();

	    // print and draw the line segments
	    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    
	    StdDraw.show();
	}
	
}
