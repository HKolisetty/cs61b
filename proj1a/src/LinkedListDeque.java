import java.util.List;
import java.util.ArrayList; // import the ArrayList class

public class LinkedListDeque<T> implements Deque<T> {
    private final IntNode SENTINEL;
    private int size;
    private IntNode last;
    private class IntNode {
        private final T ITEM;
        private IntNode next;
        private IntNode prev;
        private IntNode (T i, IntNode n) {
            ITEM = i;
            next = n;
        }
    }

    public LinkedListDeque() {
        SENTINEL = new IntNode(null,null);
        SENTINEL.next = SENTINEL;
        SENTINEL.prev = SENTINEL;
        last = SENTINEL.prev;
    }

    /**
     * Add {@code x} of type T to the front of the deque. Assumes {@code x} is never null.
     *
     * @param x item of datatype T to add
     */
    @Override
    public void addFirst(T x) {
        IntNode value = new IntNode(x, SENTINEL.next);
        SENTINEL.next.prev = value;
        value.next = SENTINEL.next;
        SENTINEL.next = value;
        value.prev = SENTINEL;
        if (size==0) {
            last = value;
        }
        size+=1;
    }

    /**
     * Add {@code x} of type T to the back of the deque. Assumes {@code x} is never null.
     *
     * @param x item of datatype T to add
     */
    @Override
    public void addLast(T x) {
        IntNode value = new IntNode(x, SENTINEL.next);
        last.next = value;
        value.next = SENTINEL;
        value.prev = last;
        last = value;
        size+=1;
    }

    /**
     * Helper private method to convert the deque into a list.
     *
     * @param myList list to add deque items to
     * @param p node to start adding from
     * @return List<T> list containing deque items in order
     */
    private List<T> toList(List<T> myList, IntNode p) {
        if (p.ITEM ==null) {
            return myList;
        } else {
            myList.add(p.ITEM);
            return toList(myList, p.next);
        }
    }

    /**
     * Converts the deque into a list.
     *
     * @return List<T> list containing deque items in order
     */
    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        toList(returnList, SENTINEL.next);
        return returnList;
    }

    /**
     * @return boolean for whether deque is empty.
     */
    @Override
    public boolean isEmpty() {
        return size==0;
    }

    /**
     * @return int for number of items in deque.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Remove the first item from the deque
     *
     * @return item that was removed or null if deque is empty
     */
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (size==1) {
            return removeLast();
        }
        IntNode value = SENTINEL.next;
        SENTINEL.next = SENTINEL.next.next;
        SENTINEL.next.prev = SENTINEL;
        size-=1;
        return value.ITEM;
    }

    /**
     * Remove the last item from the deque
     *
     * @return item that was removed or null if deque is empty
     */
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        IntNode value = last;
        last = last.prev;
        last.next = SENTINEL;
        size-=1;
        return value.ITEM;
    }

    /**
     * Iteratively return the item at the specified index
     *
     * @param index of item
     * @return item specified by index or null if index is out of range
     */
    @Override
    public T get(int index) {
        if (index<0 || index>=size) {
            return null;
        }
        IntNode p = SENTINEL.next;
        while (p.next != null && index!=0) {
            p = p.next;
            index-=1;
        }
        return p.ITEM;
    }

    /**
     * Private helper function to recursively return item
     *
     * @param index of item
     * @param p of node to start indexing from
     * @return item specified by index or null if index is out of range
     */
    private T getRecursive(int index, IntNode p) {
        if (index==0 || p.ITEM ==null) {
            return p.ITEM;
        } else {
            return getRecursive(index-1,p.next);
        }
    }

    /**
     * Recursively return the item at the specified index
     *
     * @param index of item
     * @return item specified by index or null if index is out of range
     */
    @Override
    public T getRecursive(int index) {
        if (index<0 || index>=size) {
            return null;
        }
        return getRecursive(index, SENTINEL.next);
    }
}
