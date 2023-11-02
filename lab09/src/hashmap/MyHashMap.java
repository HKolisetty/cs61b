package hashmap;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 *  A hash table-backed Map implementation.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    private int size = 0;
    private double factor;
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;
        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        this.factor = loadFactor;
        buckets = new Collection[initialCapacity];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    // Your code won't compile until you do so!


    @Override
    public void put(K key, V value) {
        int bucket = Math.floorMod(key.hashCode(), buckets.length);
        if (containsKey(key)) {
            remove(key);
            size -= 1;
        }
        buckets[bucket].add(new Node(key, value));
        size += 1;
        if ((double) size / buckets.length > factor) {
            resize(2);
        }
    }

    private void resize(int increaseFactor) {
        Collection<Node>[] newBuckets = new Collection[buckets.length * increaseFactor];
        for (int i = 0; i < newBuckets.length; i++) {
            newBuckets[i] = createBucket();
        }
        for (Collection<Node> nodes : buckets) {
            for (Node j : nodes) {
                int bucket = Math.floorMod(j.key.hashCode(), newBuckets.length);
                newBuckets[bucket].add(j);
            }
        }
        int storesize = size;
        clear();
        size = storesize;
        buckets = newBuckets;
    }

    @Override
    public V get(K key) {
        int bucket = Math.floorMod(key.hashCode(), buckets.length);
        for (Node i : buckets[bucket]) {
            if (i.key.equals(key)) {
                return i.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int bucket = Math.floorMod(key.hashCode(), buckets.length);
        for (Node i : buckets[bucket]) {
            if (i.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = createBucket();
        }
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        int bucket = Math.floorMod(key.hashCode(), buckets.length);
        for (Node i : buckets[bucket]) {
            if (i.key.equals(key)) {
                V value = get(key);
                buckets[bucket].remove(i);
                return value;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

}
