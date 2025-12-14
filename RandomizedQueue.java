import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Object[] items;
    private int size;
    public RandomizedQueue() {
        items = new Object[2];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("null item");
        if (size == items.length) resize(items.length * 2);
        items[size++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("empty");
        int r = StdRandom.uniform(size);
        Item item = (Item) items[r];
        items[r] = items[size - 1];
        items[size - 1] = null;
        size--;
        if (size > 0 && size == items.length / 4) resize(items.length / 2);
        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("empty");
        return (Item) items[StdRandom.uniform(size)];
    }

    private void resize(int capacity) {
        Object[] copy = new Object[capacity];
        for (int i = 0; i < size; i++) copy[i] = items[i];
        items = copy;
    }

    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    }

    private class RandomizedIterator implements Iterator<Item> {
        private final Object[] iterItems;
        private int current;
        public RandomizedIterator() {
            iterItems = new Object[size];
            for (int i = 0; i < size; i++) iterItems[i] = items[i];
            // shuffle via Fisher-Yates
            for (int i = 0; i < iterItems.length; i++) {
                int r = StdRandom.uniform(i, iterItems.length);
                Object tmp = iterItems[i];
                iterItems[i] = iterItems[r];
                iterItems[r] = tmp;
            }
            current = 0;
        }

        public boolean hasNext() {
            return current < iterItems.length;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return (Item) iterItems[current++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        System.out.println("empty? " + rq.isEmpty());
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        System.out.println("size (3): " + rq.size());
        System.out.println("sample: " + rq.sample());
        System.out.println("dequeue: " + rq.dequeue());
        System.out.println("size (2): " + rq.size());
        for (int x : rq) System.out.print(x + " ");
        System.out.println();
    }
}
