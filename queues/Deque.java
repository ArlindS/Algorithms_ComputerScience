import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private static final int SIZE = 10;
    private Item[] arr;     // store items
    private int n;                  // keep track of size
    private int first;
    private int last;

    // construct an empty deque
    public Deque() {
        arr = (Item[]) new Object[SIZE];
        n = 0;
        first = arr.length / 2;
        last = arr.length / 2;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    private void resize(int newSize) {
        assert newSize > SIZE;                           // newSize should be greater than original size
        int f = first;
        first = (newSize / 2) - ((arr.length / 2) - first);  // location of new first
        last = first + n;                                    // location of new last

        Item[] copy = (Item[]) new Object[newSize];
        for (int i = first; i < last; i++) {
            copy[i] = arr[f++];
        }
        arr = copy;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("item == null");

        if (first == 0)
            resize(arr.length * 2);
        n++;
        arr[--first] = item;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("item == null");

        if (last == arr.length)
            resize(arr.length * 2);
        n++;
        arr[last++] = item;
    }

    private void downSize() {
        assert n <= arr.length / 4;
        int newSize = arr.length / 2;
        int f = first;
        if ((newSize / 2) % 2 == 0) {
            first = ((newSize / 2)) - (n / 2);                      // location of new first
            last = (newSize / 2) + (n / 2);
        } else {
            first = ((newSize / 2)) - (n / 2);                      // location of new first
            last = (newSize / 2) + (n / 2) + 1;
        }
        Item[] copy = (Item[]) new Object[newSize];
        for (int i = first; i < last; i++) {
            copy[i] = arr[f++];
        }
        arr = copy;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Cannot remove from empty deque");
        if (n == arr.length / 4)
            downSize();
        n--;

        return arr[first++];
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Cannot remove from empty deque");
        if (n == (arr.length / 4))
            downSize();
        n--;
        return arr[--last];
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class DequeIterator implements Iterator<Item> {
        private int i;

        public DequeIterator() {
            i = first;
        }

        public boolean hasNext() {
            return i < last;
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
        Deque<Integer> dq = new Deque<Integer>();
        StdOut.println(dq.size());
        StdOut.println(dq.isEmpty());
        dq.addLast(1);
        dq.addFirst(3);
        dq.addLast(2);
        dq.addLast(5);
        dq.addFirst(4);
        dq.addFirst(7);
        dq.addLast(12);
        dq.addLast(15);
        dq.addLast(51);
        dq.addFirst(17);
        dq.addFirst(13);
        dq.addFirst(23);
        dq.addFirst(33);
        dq.addFirst(43);
        dq.addFirst(53);
        dq.addFirst(63);
        dq.addFirst(73);
        dq.addFirst(83);
        dq.addFirst(93);
        dq.addFirst(-3);
        dq.addFirst(-2);

        StdOut.println(dq.isEmpty());
        StdOut.println(dq.size());
        Iterator<Integer> it = dq.iterator();
        while (it.hasNext()) {
            StdOut.print(it.next() + " ");
        }
        StdOut.println();
        StdOut.println(dq.removeFirst());
        /* StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size());
        StdOut.println(dq.removeFirst() + " " + dq.size()); */

        dq.removeLast();
        dq.removeLast();
        dq.removeLast();
        dq.removeLast();
        dq.removeLast();
        dq.removeLast();

        StdOut.println(dq.size());

        Iterator<Integer> iter = dq.iterator();
        while (iter.hasNext()) {
            StdOut.print(iter.next() + " ");
        }
        StdOut.println();
    }
}
