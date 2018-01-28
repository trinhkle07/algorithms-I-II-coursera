import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

	private Node<Item> first, last;
	private int N; // number of elements in Deque
	
	private class Node<Item> {
		Item item;
		Node<Item> next;
		Node<Item> prev;
	}

	public Deque() { // construct an empty deque
		first = null;
        last = null;
        N = 0;
	}
	
	public boolean isEmpty() { // is the deque empty?
		return N == 0;
		
	}
	
	public int size()  { // return the number of items on the deque
		return N;
	}
	
	public void addFirst(Item item) { // add the item to the front
		if (item == null) throw new IllegalArgumentException();
		Node<Item> oldfirst = first;
		first = new Node<Item>();
		first.item = item;
		first.next = oldfirst;
		first.prev = null;
		N++;
	}
	
	public void addLast(Item item) {  // add the item to the end
		if (item == null) throw new IllegalArgumentException();
		
		Node<Item> oldlast = last;
		last = new Node<Item>();
		last.item = item;
		last.next = null;
		last.prev = oldlast;
		
		if (isEmpty()) { // because if queue was empty, first and oldlast was null
			first = last;
		} else {
			oldlast.next = last;
		}
		N++;
	}
	
	public Item removeFirst()  {  // remove and return the item from the front
		if (isEmpty()) throw new NoSuchElementException();
		Item item = first.item;
		first = first.next;
		N--;
		if (isEmpty()) {
			last = null;
		} else {
			first.prev = null;
		}
		
		return item;
	}
	
	public Item removeLast()  {  // remove and return the item from the end
		if (isEmpty()) throw new NoSuchElementException();
		Item item = last.item;
		last = last.prev;
		N--;
		if (last == null) {
			first = null;
		} else {
			last.next = null;
		}
		
		return item;
	}
	
	public Iterator<Item> iterator() {  // return an iterator over items in order from front to end
		return new DequeueIterator();
	}
	
	private class DequeueIterator implements Iterator<Item> {
		
		private Node<Item> current = first;
		
		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public Item next() {
			if (!hasNext()) throw new NoSuchElementException();
			Item item = current.item;
			current = current.next;
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
		
		Deque<String> deque = new Deque<String>();
        deque.addFirst("1");
        StdOut.println("addfirst to string: " + deque.AXCToString());
        deque.addFirst("2");
        StdOut.println("addfirst to string: " + deque.AXCToString());
        deque.addFirst("3");
        StdOut.println("addfirst to string: " + deque.AXCToString());
        deque.addFirst("4");
        StdOut.println("addfirst to string: " + deque.AXCToString());
        deque.addFirst("5");
        StdOut.println("addfirst to string: " + deque.AXCToString());
        deque.removeFirst();
        StdOut.println("removefirst to string: "+deque.AXCToString());
        deque.removeFirst();
        StdOut.println("removefirst to string: "+deque.AXCToString());
        deque.removeFirst();
        StdOut.println("removefirst to string: "+deque.AXCToString());
        deque.removeFirst();
        StdOut.println("removefirst to string: "+deque.AXCToString());
        deque.removeFirst();
        StdOut.println("removefirst to string: "+deque.AXCToString());
        deque.addLast("1");
        StdOut.println("addlast to string: "+deque.AXCToString());
        deque.addLast("2");
        StdOut.println("addlast to string: "+deque.AXCToString());
        deque.addLast("3");
        StdOut.println("addlast to string: "+deque.AXCToString());
        deque.addLast("4");
        StdOut.println("addlast to string: "+deque.AXCToString());
        deque.addLast("5");
        StdOut.println("addlast to string: "+deque.AXCToString());
        deque.removeLast();
        StdOut.println("removelast to string: "+ deque.AXCToString());
        deque.removeLast();
        StdOut.println("removelast to string: "+ deque.AXCToString());
        deque.removeLast();
        StdOut.println("removelast to string: "+ deque.AXCToString());
        deque.removeLast();
        StdOut.println("removelast to string: "+ deque.AXCToString());
        deque.removeLast();
        StdOut.println("removelast to string: "+ deque.AXCToString());
        //deque.addLast(null);
        //deque.addFirst(null);
        ///deque.removeLast();
        //deque.removeFirst();
	}
}