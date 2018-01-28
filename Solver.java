import java.util.Stack;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
	
	private MinPQ<Board> minPQ;
	private int minMoves;
	private boolean isSolvable;
	private Stack<Board> solution;
	
    public Solver(Board initial)  { // find a solution to the initial board (using the A* algorithm)
    	
    	minPQ = new MinPQ<Board>(Board.BY_MANHATTAN);
    	minPQ.insert(initial);
    	
    	MinPQ<Board> twinMinPQ = new MinPQ<Board>(Board.BY_MANHATTAN);
    	twinMinPQ.insert(initial.twin());
    	
    	while (!minPQ.min().isGoal() && !twinMinPQ.min().isGoal()) { // guaranteed to terminate since one of two converges.
    		Board searchNode = minPQ.delMin();
    		searchNode.setMoves(0);
    		Board twinSearchNode = twinMinPQ.delMin();
    		twinSearchNode.setMoves(0);
    		
    		for (Board neighbor : searchNode.neighbors()) {
    			
    			Board predecessor = searchNode.getParentBoard();
    			if (predecessor != null && neighbor == predecessor) {
    				continue;
    			}
    			
    			neighbor.setMoves(searchNode.getMoves() + 1);
    			neighbor.setParentBoard(searchNode);
    			
    			minPQ.insert(neighbor);
    		}
    		
    		for (Board neighbor : twinSearchNode.neighbors()) {
    			
    			Board twinPredecessor = twinSearchNode.getParentBoard();
    			if (twinPredecessor != null && neighbor == twinPredecessor) {
    				continue;
    			}
    			
    			neighbor.setMoves(twinSearchNode.getMoves() + 1);
    			neighbor.setParentBoard(twinSearchNode);
    			
    			twinMinPQ.insert(neighbor);
    		}
    	}
    	
    	isSolvable = minPQ.min().isGoal();
    	
    	minMoves = 0;
    	if (isSolvable == true) { // calculates # of moves and solution
    		Stack<Board> stack = new Stack<Board>();
    		Board b = minPQ.min();
        	while (b != null) {
        		minMoves++;
        		stack.push(b);
        		b = b.getParentBoard();
        	}
    	}
    	
    }
    
    public boolean isSolvable() { // is the initial board solvable?
    	return isSolvable;
    }
    
    public int moves() { // min number of moves to solve initial board; -1 if unsolvable

    	if (!isSolvable()) {
    		return -1;
    	}
    	
    	return minMoves;
    }
    
    public Iterable<Board> solution() { // sequence of boards in a shortest solution; null if unsolvable
    	
    	if (!isSolvable()) {
    		return null;
    	}
    	
    	return solution;
    }
    
    public static void main(String[] args) { // solve a slider puzzle (given below)
    	// create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

