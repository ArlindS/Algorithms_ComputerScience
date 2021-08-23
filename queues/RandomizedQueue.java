import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int SIZE = 10;
    private Item[] arr;             // store items
    private int n;                  // keep track of size

    // construct an empty randomized queue
    public RandomizedQueue() {
        arr = (Item[]) new Object[SIZE];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    private void resize(int newSize) {
        assert newSize > SIZE;

        Item[] copy = (Item[]) new Object[newSize];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        arr = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("item == null");

        if (n == arr.length)
            resize(2 * n);
        arr[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Cannot remove from empty deque");

        int r = StdRandom.uniform(n);
        Item ran = arr[r];
        arr[r] = arr[--n];
        arr[n] = null;

        return ran;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Cannot remove from empty deque");

        int r = StdRandom.uniform(n);
        return arr[r];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i;

        public RandomizedQueueIterator() {
            StdRandom.shuffle(arr, 0, n);
            i = 0;
        }

        public boolean hasNext() {
            return i < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return arr[i++];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();
        StdOut.println(q.isEmpty());
        StdOut.println(q.size());
        q.enqueue(4);
        q.enqueue(6);
        q.enqueue(3);
        q.enqueue(-3);
        q.enqueue(12);
        StdOut.println(q.isEmpty());
        StdOut.println(q.size());
        StdOut.println(q.dequeue());
        StdOut.println(q.sample());
        StdOut.println(q.size());

        Iterator<Integer> iter = q.iterator();
        while (iter.hasNext()) {
            StdOut.print(iter.next() + " ");
        }
        StdOut.println();
    }
}
