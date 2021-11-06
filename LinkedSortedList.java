package assignment;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author mikep
 *cps 231
 *11/6/2021
 *Implementation of an iterator woth a sorted list
 * @param <T>
 */

public class LinkedSortedList<T extends Comparable<? super T>> implements IterableSortedListInterface<T>{

	/**
	 * Private inner class for Node objects
	 * @author adamdivelbiss
	 *
	 * @param <S>
	 */
	private class Node<S> {
		private S data;
		private Node<S> next;
		private Node(S data, Node<S> next) {
			this.data = data;
			this.next = next;
		}
		private Node(S data) {
			this(data,null);
		}
	}

	private Node<T> first;
	private int numberOfEntries;
		
	/**
	 * no-arg constructor
	 */
	public LinkedSortedList() {
		this.first = null;
		this.numberOfEntries = 0;
	}

//	/**
//	 * returns the node BEFORE entry or null if first
//	 * @param entry
//	 * @return
//	 */
//	private Node<T> getNodeBefore(T entry) {
//		Node<T> curr = this.first;
//		Node<T> prev = null;
//		while (curr!=null && entry.compareTo(curr.data)>0) {
//			prev = curr;
//			curr = curr.next;
//		}
//		return prev;
//	}
	
	/**
	 * An iterative implementation of add
	 */
	/*
	@Override
	public void add(T newEntry) {
		Node<T> node = new Node<>(newEntry);
		Node<T> prev = this.getNodeBefore(newEntry);
		if (this.isEmpty() || prev==null) {
			node.next = this.first;
			this.first = node;
		} else {
			node.next = prev.next;
			prev.next = node;
		}
		this.numberOfEntries++;
	}
	*/

	// private helper method to do recursive add
	private Node<T> add(T newEntry, Node<T> curr) {
		if (curr == null || newEntry.compareTo(curr.data)<=0) {
			curr = new Node<>(newEntry, curr);
		} else {
			curr.next = add(newEntry, curr.next);
		}
		return curr;
	}
	
	@Override
	public void add(T newEntry) {
		this.first = add(newEntry, this.first);
		this.numberOfEntries++;
	}
	

	@Override
	public boolean remove(T anEntry) {
		
		boolean result = false;
		int position = getPosition(anEntry);
		
		if (position > 0) {
			remove(position);
			result = true;
		} // end if 
		
		return result;
	}

	@Override
	public T remove(int givenPosition) {
		// validate the index
		checkBounds(givenPosition);

		// Declare a value to return;
		T value = null;

		// handle first node case
		if (givenPosition==0) {
			// get the desired data
			value = this.first.data;
			// remove the current first node
			this.first = this.first.next;
		}
		// handle non-first node case
		else {
			// get the previous node
			Node<T> prev = this.nodeAtIndex(givenPosition-1);
			// get the desired data
			value = prev.next.data;
			// remove the desired node
			prev.next = prev.next.next;	
			// check if last was removed.
		}
		this.numberOfEntries--;
		return value;
	}
	
	
	private Node<T> nodeAtIndex(int index) {
		// validate the index
		checkBounds(index);
		
		// traverse the chain until we get to the correct node.
		Node<T> node = this.first;
		for (int i=0; (i<index) && (node!=null); i++) {
			node = node.next;
		}
		return node;
	}
	
	
	private void checkBounds(int index) {
		// validate the index
		if (index < 0 || this.numberOfEntries <= index) {
			throw new IndexOutOfBoundsException();
		}				
	}

//	while (current.data != givenPosition) {
//		if (current.next == null) {
//			return null;
//		}else 
//			current = current.next;
//	}
//		if (current == first) {
//			first = first.next;
//		}else
//			previous.next = current.next;
//		return current;
//	}

	@Override
	public void clear() {
		this.first = null;
		this.numberOfEntries = 0;
	}

	@Override
	public int getPosition(T anEntry) {
		// traverse the chain until we get to the correct node.
		Node<T> node = this.first;
		int index = 0;
		while ((node!=null) && anEntry.compareTo(node.data)>0) {
			node = node.next;
			index++;
		}
		if (node==null || anEntry.compareTo(node.data)!=0) {
			index = -(index+1);
		}
		return index;

	}

	@Override
	public T getEntry(int givenPosition) {
		Node<T> node = this.nodeAtIndex(givenPosition);
		return node.data;
	}

	@Override
	public boolean contains(T anEntry) {
		boolean result = false;
		
		Node<T> current = this.first;
		while (current!=null && !result) {
			result = anEntry.equals(current.data);
			current = current.next;
		}

		return result;
	}

	@Override
	public int getLength() {
		return this.numberOfEntries;
		
	}

	@Override
	public boolean isEmpty() {
		return this.first==null;
	}

	@Override
	public T[] toArray() {
		// Step 1. Create the new container array
		// OK because array is null
		@SuppressWarnings("unchecked")
		T[] data = (T[])(new Object[this.numberOfEntries]);

		// Step 2. Copy data into the array
		Node<T> current = this.first;
		for (int i=0; i<this.numberOfEntries; i++) {
			data[i] = current.data;
			current = current.next;
		}
		return data;
	}

	@Override
	public Iterator<T> iterator() {
		return new InnerIterator();
	}
	
	
	private class InnerIterator implements Iterator<T>  {
		
		private Node<T> first;
		// needed for the remove method to see if the next element was found 
		private boolean wasNextCalled;
		// variable to hold the position of the next element in the list 
		private int nextPosition;
		/**
		 * constructor for the innerIterator
		 */
		private InnerIterator() {
			nextPosition = 0;
			wasNextCalled = false;
		}// end default constructor.
		
		
		@Override
		public boolean hasNext() {
			if (nextPosition < LinkedSortedList.this.getLength()) {
				return true;
			}else return false;

			
			//			Node<T> current = this.first;
//			if (current.next == null) {
//				return false;
//			}else return true;
		}

		@Override
		public T next() {
			if (hasNext()) {
				wasNextCalled = true;
				nextPosition++;
				return LinkedSortedList.this.getEntry(nextPosition-1);
			}else throw new NoSuchElementException("illegal call to next() itterator is after end of list");

		}
		
		/*
		 * removes the entry that the iterator is currently on 
		 */
		public void remove() {
			if (wasNextCalled) {
				LinkedSortedList.this.remove(nextPosition-1);
				nextPosition--;
				wasNextCalled = false;
			}else throw new IllegalStateException("Illegal call to remove next() was not called");
		}// end of remove 

		
	}

}
