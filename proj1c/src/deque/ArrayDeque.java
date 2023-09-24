package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayDeque<T> implements Deque<T> {
    private T[] array;
    private int size, startIndex, endIndex;
    private static final int CONSTANT_1 = 16;
    private static final double CONSTANT_2 = 0.25;

    /**
     * Construct new empty list
     */
    public ArrayDeque() {
        array = (T[]) new Object[8];
        size = 0;
        startIndex = 0;
        endIndex = 0;
    }

    /**
     * Private helper function to geometrically resize array up by a factor of {@code n}
     * @param n factor to resize array up by
     */
    private void resizeUp(int n) {
        T[] newArray = (T[]) new Object[array.length * n];
        boolean flag = false;
        int storeEndIndex = endIndex;
        if (startIndex <= endIndex) {
            storeEndIndex = endIndex + array.length * (n - 1);
        }
        for (int i = 0; i < array.length; i++) {
            if (i <= endIndex && !flag) {
                newArray[i] = array[i];
            } else {
                newArray[i + array.length * (n - 1)] = array[i];
            }
            if (i == startIndex) {
                startIndex = i + array.length * (n - 1);
                flag = true;
            }
        }
        array = newArray;
        endIndex = storeEndIndex;
    }

    /**
     * Add {@code x} of type T to the front of the deque. Assumes {@code x} is never null.
     *
     * @param x item of datatype T to add
     */
    @Override
    public void addFirst(T x) {
        if (size >= array.length - 2) {
            resizeUp(2);
        }
        array[startIndex] = x;
        size += 1;
        startIndex -= 1;
        if (startIndex < 0) {
            startIndex = array.length - 1;
        }
        if (size == 1) {
            endIndex += 1;
        }
    }

    /**
     * Add {@code x} of type T to the back of the deque. Assumes {@code x} is never null.
     *
     * @param x item of datatype T to add
     */
    @Override
    public void addLast(T x) {
        if (size >= array.length - 2) {
            resizeUp(2);
        }
        array[endIndex] = x;
        size += 1;
        endIndex += 1;
        if (endIndex == array.length) {
            endIndex = 0;
        }
        if (size == 1) {
            startIndex = array.length - 1;
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
        int startValue = startIndex + 1;
        if (startValue == array.length) {
            startValue = 0;
        }
        int i = startValue;
        while (i < endIndex || i > startIndex) {
            if (startIndex <= endIndex && i >= endIndex) {
                break;
            }
            returnList.add(array[i]);
            if (i == array.length - 1) {
                i = -1;
            }
            i += 1;
        }
        return returnList;
    }

    /**
     * @return boolean for whether deque is empty.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return int for number of items in deque.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Private helper function to geometrically resize array down by a factor of {@code n}
     * @param n factor to resize array down by
     */
    private void resizeDown(int n) {
        T[] newArray = (T[]) new Object[(int) (double) (array.length / n)];
        int change = array.length - newArray.length;
        int storeStartIndex = startIndex - change;
        int storeEndIndex = endIndex;
        for (int i = 0; i < newArray.length; i++) {
            if (i <= endIndex) {
                if (startIndex <= endIndex) {
                    newArray[i] = array[i + startIndex];
                } else {
                    newArray[i] = array[i];
                }
            } else {
                newArray[i] = array[i + change];
            }
            if (i + startIndex == startIndex && startIndex <= endIndex) {
                storeStartIndex = i;
            }
            if (i + startIndex == endIndex) {
                storeEndIndex = i;
                if (startIndex <= endIndex) {
                    break;
                }
            }
        }
        array = newArray;
        startIndex = storeStartIndex;
        endIndex = storeEndIndex;
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
        if (array.length > CONSTANT_1 && ((double) size / array.length) < CONSTANT_2) {
            resizeDown(2);
        }
        int startValue = startIndex + 1;
        if (startValue == array.length) {
            startValue = 0;
        }
        T returnItem = array[startValue];
        array[startValue] = null;
        startIndex = startValue;
        size -= 1;
        if (size == 0) {
            endIndex = 0;
            startIndex = 0;
        }
        return returnItem;
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
        if (size > CONSTANT_1 && (double) size / array.length < CONSTANT_2) {
            resizeDown(2);
        }
        int endValue = endIndex - 1;
        if (endValue < 0) {
            endValue = array.length - 1;
        }
        T returnItem = array[endValue];
        array[endValue] = null;
        endIndex = endValue;
        size -= 1;
        if (size == 0) {
            endIndex = 0;
            startIndex = 0;
        }
        return returnItem;
    }

    /**
     * Return the item at the specified index
     *
     * @param index of item
     * @return item specified by index or null if index is out of range
     */
    @Override
    public T get(int index) {
        if (index >= array.length || index < 0) {
            return null;
        }
        if (index <= array.length - startIndex - 2) {
            return array[startIndex + 1 + index];
        }
        return array[index - (array.length - startIndex - 1)];
    }

    /**
     * Error thrown when this unnecessary method is called
     */
    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    private class ArrayDequeIterator implements Iterator<T> {
        private int position;
        public ArrayDequeIterator() {
            position = 0;
        }
        public boolean hasNext() {
            return position < size();
        }
        public T next() {
            T returnValue = get(position);
            position += 1;
            return returnValue;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof ArrayDeque otherArrayDeque) {
            if (this.size() != otherArrayDeque.size()) {
                return false;
            }
            for (int i = 0; i < this.size(); i++) {
                if (!this.get(i).equals(otherArrayDeque.get(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder("[");
        for (T i : this) {
            if (i == get(size() - 1)) {
                returnString.append(i);
                returnString.append("]");
            } else {
                returnString.append(i);
                returnString.append(", ");
            }
        }
        return returnString.toString();
    }
}

