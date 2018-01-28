import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
	
	private Node root;
	private int size;
	
	private static class Node {
		private Point2D p;      // the point
		private RectHV rect;    // the axis-aligned rectangle corresponding to this node
		private Node lb;        // the left/bottom subtree
		private Node rt;        // the right/top subtree
		
		public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
	}
	

	public KdTree() { // construct an empty set of points
		root = null;
		size = 0;
	}

	public boolean isEmpty() { // is the set empty?
		return size == 0;
	}

	public int size() { // number of points in the set
		return size;
	}

	public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
		root = insert(null, root, p.x(), p.y(), true, SIDE.NA); // true means x-coordinate, false means y-coordinate
	}
	
	private enum SIDE {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        NA
    }
	
	private Node insert(Node parent, Node node, double x, double y, boolean coordinate, SIDE side) {
		if (node == null) {
			size++;
			
			if (parent == null) {
				return new Node(new Point2D(x, y), new RectHV(0, 0, 1, 1));
			}
			
			RectHV rect = parent.rect;
			Point2D point = parent.p;
			RectHV newRect;
			switch (side) {
			case LEFT:
				newRect = new RectHV(
						rect.xmin(),
						rect.ymin(),
						point.x(),
						rect.ymax());
				break;
			case RIGHT:
				newRect = new RectHV(
						point.x(),
						rect.ymin(),
						rect.xmax(),
						rect.ymax());
				break;
			case TOP:
				newRect = new RectHV(
						rect.xmin(),
						point.y(),
						rect.xmax(),
						rect.ymax());
				break;
			case BOTTOM:
				newRect = new RectHV(
						rect.xmin(),
						rect.ymin(),
						rect.xmax(),
						point.y());
				break;
			default:
				throw new IllegalArgumentException();
			}
			
			return new Node(new Point2D(x, y), newRect);
		}
		
		if (x == node.p.x() && y == node.p.y()) return node;
		
		if (coordinate) {
			if (x < node.p.x()) node.lb = insert(node, node.lb, x, y, !coordinate, SIDE.LEFT);
			else if (x > node.p.x()) node.rt = insert(node, node.rt, x, y, !coordinate, SIDE.RIGHT);
		} else {
			if (y < node.p.y()) node.lb = insert(node, node.lb, x, y, !coordinate, SIDE.BOTTOM);
			else if (y > node.p.y()) node.rt = insert(node, node.rt, x, y, !coordinate, SIDE.TOP);
		}
		
		return node;
	}

	public boolean contains(Point2D p) { // does the set contain point p?
		return contains(root, p.x(), p.y(), true);
	}
	
	private boolean contains(Node node, double x, double y, boolean coordinate) {
		
		if (node == null) return false;
		
		if (x == node.p.x() && y == node.p.y()) return true;
		
		if (coordinate) {
			if (x < node.p.x()) return contains(node.lb, x, y, !coordinate);
			else if (x > node.p.x()) return contains(node.rt, x, y, !coordinate);
		} else {
			if (y < node.p.y()) return contains(node.lb, x, y, !coordinate);
			else if (y > node.p.y()) return contains(node.rt, x, y, !coordinate);
		}
		
		return false; // should not go here.
	}

	public void draw() { // draw all points to standard draw
		draw(root, true);
	}
	
	private void draw(Node node, boolean vertically) {
		
		if (node == null) return;
		
		StdDraw.setPenColor(Color.BLACK);
		StdDraw.setPenRadius(0.01);
		node.p.draw();
		
		if (vertically) {
			StdDraw.setPenColor(Color.RED);
			StdDraw.setPenRadius();
			StdDraw.line(node.p.x(),node.rect.ymin(),node.p.x(),node.rect.ymax()); // vertical
		} else {
			StdDraw.setPenColor(Color.BLUE);
			StdDraw.setPenRadius();
			StdDraw.line(node.rect.xmin(),node.p.y(),node.rect.xmax(),node.p.y()); // horizontal
		}
		
		draw(node.rt, !vertically);
		draw(node.lb, !vertically);
	}

	public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
		Set<Point2D> set = new HashSet<Point2D>();
		range(root, rect, set);
		return set;
	}
	
	private Set<Point2D> range(Node node, RectHV query, Set<Point2D> set) {
		if (node == null) return set;
		
		if (query.contains(node.p)) {
			set.add(node.p);
		}
		
		if (node.lb != null && query.intersects(node.lb.rect)) {
			set = range(node.lb, query, set);
		}
		
		if (node.rt != null && query.intersects(node.rt.rect)) {
			set = range(node.rt, query, set);
		}
		
		return set;
	}

	public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
		
		Point2D champion = root.p;
		return nearest(root, p, true, champion); // true means vertically		
	}
	
	private Point2D nearest(Node node, Point2D query, boolean vertically, Point2D champion) {
		
		if (node == null) return champion;
		
		if (query.distanceSquaredTo(node.p) < query.distanceSquaredTo(champion)) {
			champion = node.p;
		}
		
		if (node.rect.distanceSquaredTo(query) < query.distanceSquaredTo(champion)) { // if there're 2 possible subtrees that are < champion, this will be true.
			
            if ((vertically && query.x() < node.p.x()) ||
                    (!vertically && query.y() < node.p.y())) {
                // explore left bottom first
                champion = nearest(node.lb, query, !vertically, champion);
                champion = nearest(node.rt, query, !vertically, champion);
            } else {
                // query point is right, above, or equal to node point
                // explore right top first
                champion = nearest(node.rt, query, !vertically, champion);
                champion = nearest(node.lb, query, !vertically, champion);
            }

		}
		return champion;
	}

	public static void main(String[] args) { // unit testing of the methods (optional)
      KdTree t = new KdTree();
      StdOut.println(t.isEmpty());
      StdOut.println(t.size());
      t.insert(new Point2D(0.7, 0.2));
      t.insert(new Point2D(0.5, 0.4));
      t.insert(new Point2D(0.2, 0.3));
      t.insert(new Point2D(0.4, 0.7));
      t.insert(new Point2D(0.9, 0.6));
      StdOut.println(t.isEmpty());
      StdOut.println(t.size());
      StdOut.println(t.contains(new Point2D(0.5, 0.4)));
      StdOut.println(t.contains(new Point2D(0.7, 0.5)));
      t.draw();
	}
}
