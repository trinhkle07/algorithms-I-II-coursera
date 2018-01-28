import java.util.HashMap;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
	private SAP sap;
	private HashMap<Integer, String> id2synset;
    private HashMap<String, Bag<Integer>> noun2ids;
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		// vertices are from nouns field of synsets
		// edges are from hypernyms which specifies at least 2 synset ids (edges) pointing to a synset id
		id2synset = new HashMap<>();
		noun2ids = new HashMap<>();
		readSynsets(synsets);
		Digraph G = readHypernyms(hypernyms);
		sap = new SAP(G);
	}
	
	private void readSynsets(String synsetsFile) {
		In input = new In(synsetsFile);
		Bag<Integer> bag;
		while (input.hasNextLine()) {
			String line = input.readLine();
			String[] fields = line.split(",");
			int synsetId = Integer.parseInt(fields[0]);
			id2synset.put(synsetId, fields[1]);
			String[] nouns = fields[1].split("\\\\s+");
			for (String noun : nouns) {

				bag = noun2ids.get(noun);
				if (bag == null) {
					bag = new Bag<Integer>();
				}
				bag.add(synsetId);
				noun2ids.put(noun, bag);
			}
		}
	}
	
	private Digraph readHypernyms(String hypernymsFile) {
		Digraph G = new Digraph(id2synset.size());
		
		In input = new In(hypernymsFile);
		while (input.hasNextLine()) {
			String line = input.readLine();
			String[] ids = line.split(",");
			int synSetId = Integer.parseInt(ids[0]);
			for (int i = 0; i < ids.length - 1; i++) {
				G.addEdge(Integer.parseInt(ids[i]), synSetId);
			}
		}
		
		return G;
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return noun2ids.keySet();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return noun2ids.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		verifyNoun(nounA);
        verifyNoun(nounB);

        return sap.length(noun2ids.get(nounA), noun2ids.get(nounB));
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		verifyNoun(nounA);
		verifyNoun(nounB);
		
		return id2synset.get(sap.ancestor(noun2ids.get(nounA), noun2ids.get(nounB)));
	}
	
	private void verifyNoun(String noun) {
        if (!isNoun(noun)) {
            throw new IllegalArgumentException();
        }
    }
	

	// do unit testing of this class
	public static void main(String[] args) {
		WordNet wordNet = new WordNet(args[0], args[1]);

		while (!StdIn.isEmpty()) {
			String nounA = StdIn.readString();
			String nounB = StdIn.readString();

			if (!wordNet.isNoun(nounA)) {
				StdOut.printf("%s is not a noun!\n", nounA);
				continue;
			}

			if (!wordNet.isNoun(nounB)) {
				StdOut.printf("%s is not a noun!\n", nounB);
				continue;
			}

			int distance = wordNet.distance(nounA, nounB);
			String ancestor = wordNet.sap(nounA, nounB);

			StdOut.printf("distance = %d, ancestor = %s\n", distance, ancestor);
		}

	}
}
