import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V extends Comparable<V>> implements Map61B<K, V> {
    private BSTNode root;
    private int size = 0;

    public BSTMap() {

    }
    private class BSTNode {
        K key;
        V value;
        BSTNode left;
        BSTNode right;
        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == 0) {
            root = new BSTNode(key, value);
            size +=1;
        } else {
            put(key, value, root);
        }
    }
    private BSTNode put(K key, V value, BSTNode node) {
        if (node == null) {
            size += 1;
            return new BSTNode(key, value);
        }
        if (node.key.compareTo(key) == 0) {
            node.value = value;
            return node;
        } else if (node.key.compareTo(key) > 0) {
            node.left = put(key, value, node.left);
        } else {
            node.right = put(key, value, node.right);
        }
        return node;
    }

    @Override
    public V get(K key) {
        return get(key, root);
    }
    private V get(K key, BSTNode node) {
        if (node == null) {
            return null;
        }
        if (node.key.compareTo(key) == 0) {
            return node.value;
        }
        if (node.key.compareTo(key) > 0) {
            return get(key, node.left);
        }
        return get(key, node.right);
    }

    @Override
    public boolean containsKey(K key) {
        return containsKey(key, root);
    }
    private boolean containsKey(K key, BSTNode node) {
        if (node == null) {
            return false;
        }
        if (node.key.compareTo(key) == 0) {
            return true;
        }
        if (node.key.compareTo(key) > 0) {
            return containsKey(key, node.left);
        }
        return containsKey(key, node.right);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {
        System.out.print(printInOrder(root));
    }
    private String printInOrder(BSTNode node) {
        return "";
    }
}
