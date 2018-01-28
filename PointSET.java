import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

// represents a set of points in the unit square by using red-black BST
// Brute-force algorithm
public class PointSET {
	
	private TreeSet<Point2D> pointSet;
	
	public PointSET() { // construct an empty set of points
		pointSet = new TreeSet<Point2D>();
	}

	public           boolean isEmpty() { // is the set empty?
		return pointSet.isEmpty();
	}

	public               int size() { // number of points in the set
		return pointSet.size();
	}

	public              void insert(Point2D p) { // add the point to the set (if it is not already in the set)
		pointSet.add(p);
	}

	public           boolean contains(Point2D p) { // does the set contain point p?
		return pointSet.contains(p);
	}

	public void draw() { // draw all points to standard draw
		for (Point2D p : pointSet) {
			StdDraw.filledCircle(p.x(), p.y(), 0.003);
		}
	}

	public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
		SET<Point2D> set = new SET<Point2D>();
		
		for (Point2D p : pointSet) {
			if (rect.contains(p)) {
				set.add(p);
			}
		}
		
		return set;
	}

	public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
		double nearestDist = Double.MAX_VALUE;
		Point2D nearestPoint = null;
		for (Point2D point : pointSet) {
			double dist = point.distanceSquaredTo(p);
			if (dist < nearestDist) {
				nearestDist = dist;
				nearestPoint = point;
			}
		}
		return nearestPoint;
	}

	public static void main(String[] args) { // unit testing of the methods (optional)

	}
}
