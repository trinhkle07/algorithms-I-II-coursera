import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
	
	private int N;
	private Map<String, Integer> teams;
	private String[] teamNames;
	private int[] w;
	private int[] l;
	private int[] r;
	private int[][] g;
	FordFulkerson ff;
	
	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		In in = new In(filename);
		N = in.readInt();
		teams = new HashMap<String, Integer>();
		teamNames = new String[N];
		w = new int[N];
		l = new int[N];
		r = new int[N];
		g = new int[N][N];
		
		for (int i = 0; i < N; i++) {
			String teamName = in.readString();
			
			teams.put(teamName, i);
			teamNames[i] = teamName;
			
			w[i] = in.readInt();
			l[i] = in.readInt();
			r[i] = in.readInt();
			
			for (int j = 0; j < N; j++) {
				g[i][j] = in.readInt();
			}
			
			
		}
		
	}

	//number of teams
	public int numberOfTeams() {
		return N;

	}

	//all teams
	public Iterable<String> teams() {
		List<String> names = new LinkedList<>();
        names.addAll(teams.keySet());
        return names;
	}

	//number of wins for given team
	public int wins(String team) {
		if (!teams.containsKey(team)) throw new IllegalArgumentException();
		
		return w[teams.get(team)];

	}

	//number of losses for given team
	public int losses(String team) {
		if (!teams.containsKey(team)) throw new IllegalArgumentException();

		return l[teams.get(team)];

	}

	//number of remaining games for given team
	public int remaining(String team) {
		if (!teams.containsKey(team)) throw new IllegalArgumentException();

		return r[teams.get(team)];

	}

	//number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		if (!teams.containsKey(team1)) throw new IllegalArgumentException();
		if (!teams.containsKey(team2)) throw new IllegalArgumentException();
		
		return g[teams.get(team1)][teams.get(team2)];

	}

	//is given team eliminated?
	public boolean isEliminated(String team) {
		
		if (!teams.containsKey(team)) throw new IllegalArgumentException();
		
		int teamId = teams.get(team);
		for (int i = 0; i < N; i++) {
			if (i == teamId) continue;
			if (w[teamId] + r[teamId] < w[i]) {
				return true;
			}
		}
		
		int V = (N - 1) * (N - 2) / 2 + N - 1 + 2;
		FlowNetwork network = new FlowNetwork(V);
		
		int count = 1;
		for (int i = 0; i < N; i++) {
			if (i == teamId) continue;
			for (int j = i + 1; j < N; j++) {
				if (j == teamId) continue;
				network.addEdge(new FlowEdge(0, count, g[i][j]));
				network.addEdge(new FlowEdge(count, V - N + i - (i > teamId ? 1 : 0), Double.POSITIVE_INFINITY));
				network.addEdge(new FlowEdge(count, V - N + j - (j > teamId ? 1 : 0), Double.POSITIVE_INFINITY));
				count++;
			}
		}
		
		for (int i = 0; i < N; i++) {
			if (i == teamId) continue;
			network.addEdge(new FlowEdge(count, V - 1, w[teamId] + r[teamId] - w[i]));
			count++;
		}
		
		ff = new FordFulkerson(network, 0, V - 1);
		
		for (FlowEdge e : network.adj(0)) {
			if (e.flow() != e.capacity()) return true;
		}
		
		return false;

	}

	//subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		if (!teams.containsKey(team)) throw new IllegalArgumentException();
		
		List<String> R = new LinkedList<String>();
		int teamId = teams.get(team);
		
		for (int i = 0; i < N; i++) {
			if (i == teamId) continue;
			if (w[teamId] + r[teamId] < w[i]) {
				R.add(teamNames[i]);
				return R;
			}
		}
		
		if (!isEliminated(team)) {
			return R;
		}
		
		int V = (N - 1) * (N - 2) / 2 + N - 1 + 2;
		for (int i = 0; i < N; i++) {
			if (i == teamId) continue;
			int v = V - N + i - (i > teamId ? 1 : 0);
			
			if(ff.inCut(v)) {
				R.add(teamNames[i]);
			}
		}
		
		return R;
	}
	
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}

}
