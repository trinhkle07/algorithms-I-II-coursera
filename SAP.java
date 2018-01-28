import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

	private Digraph G;

	private DeluxeBFS bfs1;
	private DeluxeBFS bfs2;

	private LinkedHashMap<Set<Integer>, Integer> ancestorCache = new MyLinkedHashMap<Set<Integer>, Integer>(30*10/7, 0.7f, true);
	private LinkedHashMap<Set<Integer>, Integer> ancestorLengthCache = new MyLinkedHashMap<Set<Integer>, Integer>(30*10/7, 0.7f, true);
	
	private LinkedHashMap<Set<Iterable<Integer>>, Integer> ancestorIterCache = new MyLinkedHashMap<Set<Iterable<Integer>>, Integer>(30*10/7, 0.7f, true);
	private LinkedHashMap<Set<Iterable<Integer>>, Integer> ancestorLengthIterCache = new MyLinkedHashMap<Set<Iterable<Integer>>, Integer>(30*10/7, 0.7f, true);
	
	private class MyLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
			return size() > 30;
		}
		
		public MyLinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
			super(initialCapacity, loadFactor, accessOrder);
		}
	};
	

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		this.G = G;
		this.bfs1 = new DeluxeBFS();
		this.bfs2 = new DeluxeBFS();
	}

	private class DeluxeBFS {

		private static final int INFINITY = Integer.MAX_VALUE;
		private boolean[] marked;  // marked[v] = is there an s->v path?
		private int[] edgeTo;      // edgeTo[v] = last edge on shortest s->v path
		private int[] distTo;      // distTo[v] = length of shortest s->v path
		private Queue<Integer> modifiedQ;

		public DeluxeBFS() {
			marked = new boolean[G.V()];
			edgeTo = new int[G.V()];
			distTo = new int[G.V()];
			modifiedQ = new Queue<Integer>();
			for (int v = 0; v < G.V(); v++)
				distTo[v] = INFINITY;
		}

		public void bfs(int s) {
			Queue<Integer> q = new Queue<Integer>();
			marked[s] = true;
			distTo[s] = 0;
			modifiedQ.enqueue(s);
			q.enqueue(s);

			while (!q.isEmpty()) {

				int v = q.dequeue();

				for (int w : G.adj(v)) {
					if (!marked[w]) {
						edgeTo[w] = v;
						marked[w] = true;
						modifiedQ.enqueue(w);
						distTo[w] = distTo[v] + 1;
						q.enqueue(w);
					}
				}
			}
		}


		public void bfs(Iterable<Integer> s) {
			Queue<Integer> q = new Queue<Integer>();
			for (int vertex : s) {
				marked[vertex] = true;
				distTo[vertex] = 0;
				modifiedQ.enqueue(vertex);
				q.enqueue(vertex);
			}
			
			while (!q.isEmpty()) {

				int v = q.dequeue();

				for (int w : G.adj(v)) {
					if (!marked[w]) {
						edgeTo[w] = v;
						marked[w] = true;
						modifiedQ.enqueue(w);
						distTo[w] = distTo[v] + 1;
						q.enqueue(w);
					}
				}
			}
		}

		public void reinitialize() {
			for (int i : modifiedQ) {
				marked[i] = false;
				distTo[i] = INFINITY;
			}
			modifiedQ = null;
			modifiedQ = new Queue<Integer>();
		}

	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		
		// Software cache for this method
		Set<Integer> ints = new HashSet<Integer>();
		ints.add(v); ints.add(w);
		if (ancestorLengthCache.containsKey(ints)) {
			System.out.println("AncestorLengthCache");
			return ancestorLengthCache.get(ints);
		}
		
		// Compute length and ancestor and put them in caches
		ancestor(v, w);
		
		return ancestorLengthCache.get(ints);
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		validateVertex(v);
		validateVertex(w);

		// Software cache for this method
		Set<Integer> ints = new HashSet<Integer>();
		ints.add(v); ints.add(w);
		if (ancestorCache.containsKey(ints)) {
			System.out.println("AncestorCache");
			return ancestorCache.get(ints);
		}
		
		// re-initialize the modifiedQ and marked array here + check cache
		bfs1.reinitialize();
		bfs2.reinitialize();

		bfs1.bfs(v);
		bfs2.bfs(w);

		int minDist = -1;
		int minAncestor = -1;
		for (int vertex : bfs1.modifiedQ) {
			if (bfs1.marked[vertex] == bfs2.marked[vertex]) {
				int distance = bfs1.distTo[vertex] + bfs2.distTo[vertex];
				if (minDist == -1 || distance < minDist) {
					minDist = distance;
					minAncestor = vertex;
				}
			}
		}
		
		ancestorCache.put(ints, minAncestor);
		ancestorLengthCache.put(ints, minDist);

		return minAncestor; 
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		validateVertices(v);
		validateVertices(w);
		
		// Software cache for this method
		Set<Iterable<Integer>> ints = new HashSet<Iterable<Integer>>();
		ints.add(v); ints.add(w);
		if (ancestorLengthIterCache.containsKey(ints)) {
			return ancestorLengthIterCache.get(ints);
		}

		// Compute length and ancestor and put them in caches
		ancestor(v, w);

		return ancestorLengthIterCache.get(ints);
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		validateVertices(v);
		validateVertices(w);

		// Software cache for this method
		Set<Iterable<Integer>> ints = new HashSet<Iterable<Integer>>();
		ints.add(v); ints.add(w);
		if (ancestorIterCache.containsKey(ints)) {
			return ancestorIterCache.get(ints);
		}
		
		// re-initialize the modifiedQ and marked array here + check cache
		bfs1.reinitialize();
		bfs2.reinitialize();

		bfs1.bfs(v);
		bfs2.bfs(w);

		int minDist = -1;
		int minAncestor = -1;
		for (int vertex : bfs1.modifiedQ) {
			if (bfs1.marked[vertex] == bfs2.marked[vertex]) {
				int distance = bfs1.distTo[vertex] + bfs2.distTo[vertex];
				if (minDist == -1 || distance < minDist) {
					minDist = distance;
					minAncestor = vertex;
				}
			}
		}
		return minAncestor;
	}

	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertex(int v) {
		if (v < 0 || v >= G.V())
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (G.V()-1));
	}
	
	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertices(Iterable<Integer> vertices) {
		for (int v : vertices) {
			if (v < 0 || v >= G.V())
				throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (G.V()-1));	
		}

	}

	// do unit testing of this class
	public static void main(String[] args) {
		In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
	}
}
