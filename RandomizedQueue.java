import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	
	private Item[] q;
	private int N;
	
	public RandomizedQueue()  {  // construct an empty randomized queue
		q = (Item[]) new Object[1];
	}
	
	public boolean isEmpty() { // is the randomized queue empty?
		return N == 0;
	}
	
	public int size() { // return the number of items on the randomized queue
		return N;
	}
	
	public void enqueue(Item item) { // add the item
		if (item == null) throw new IllegalArgumentException();
		
		if (N == q.length) {
			resize(2 * q.length);
		}
		q[N++] = item;
	}
	
	public Item dequeue() { // remove and return a random item
		if (isEmpty()) throw new NoSuchElementException();
		
		int randomIndex = StdRandom.uniform(N);
		Item item = q[randomIndex];
		q[randomIndex] = q[N - 1];
		q[N - 1] = null;
		N--;
		if (N > 0 && N == q.length/4) resize(q.length/2);
		return item;
		
	}
	
	public Item sample() { // return a random item (but do not remove it)
		if (isEmpty()) throw new NoSuchElementException();
		int randomIndex = StdRandom.uniform(N);
		return q[randomIndex];
	}
	
	private void resize(int capacity) {
		Item[] copy = (Item[]) new Object[capacity];
		for (int i = 0; i < N; i++) {
			copy[i] = q[i];
		}
		q = copy;
	}
	
	public Iterator<Item> iterator() { // return an independent iterator over items in random order
		return new RandomizedQueueIterator<Item>();
	}
	
	private class RandomizedQueueIterator<Item> implements Iterator<Item> {
		
		private Item[] copiedArray;
		private int copiedN;
		
		
		public RandomizedQueueIterator() {
			copiedArray = (Item[]) new Object[N];
			for (int i = 0; i < N; i++) {
				copiedArray[i] = (Item) q[i];
			}
			
			copiedN = N;
		}
		
		
		@Override
		public boolean hasNext() {
			return copiedN != 0;
		}

		@Override
		public Item next() {
			if (copiedN == 0) throw new NoSuchElementException();
			int randomIndex = StdRandom.uniform(copiedN);
			Item item = copiedArray[randomIndex];
			copiedArray[randomIndex] = copiedArray[copiedN - 1];
			copiedArray[copiedN - 1] = null;
			copiedN--;
			return item;
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	private String AXCToString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item + " ");
        return s.toString();
    }
	
	public static void main(String[] args) { // unit testing (optional)
		RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        queue.enqueue(1);
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
        queue.enqueue(2);
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
        queue.enqueue(3);
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
        queue.enqueue(4);
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
        queue.enqueue(5);
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
        queue.enqueue(6);
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
        queue.enqueue(7);
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
        queue.enqueue(8);
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
        queue.enqueue(9);
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
        queue.enqueue(10);
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.AXCToString());
        StdOut.println(queue.AXCToString());
	}
}
