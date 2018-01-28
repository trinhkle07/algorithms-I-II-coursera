import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

	private int numSegs = 0;
	private LineSegment[] lineSegments;
	
	public FastCollinearPoints(Point[] points) { // finds all line segments containing 4 or more points
		
		Arrays.sort(points);
		
		List<LineSegment> segList = new ArrayList<LineSegment>();
		
		for (int i = 0; i < points.length; i++) {
			Point p = points[i];
			
			int len = points.length - i - 1;
			Point[] tempPoints = new Point[len];
			for (int j = 0; j < len; j++) {
				tempPoints[j] = points[i + 1 + j];
			}
			
			Arrays.sort(tempPoints, p.slopeOrder());
			
			LineSegment lineSeg = null;
			for (int j = 0; j < len - 1; j++) {
				
				if (p.slopeTo(tempPoints[j]) == p.slopeTo(tempPoints[j + 1])) {
					lineSeg = new LineSegment(p, tempPoints[j + 1]);
				}
			}
			
			if (lineSeg != null) {
				segList.add(lineSeg);
				numSegs++;
			}
			
		}
		
		if (!segList.isEmpty()) {
			lineSegments = Arrays.copyOf(segList.toArray(), segList.size(), LineSegment[].class);
		}
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
	        System.out.println("IIIIIIIIIIIIIIIIi");
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
	    FastCollinearPoints collinear = new FastCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    
	    StdDraw.show();
	}
	
}