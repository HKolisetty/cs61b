import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class ArrayDeque<T> implements Deque<T> {
    private T[] array;
    private int size, startIndex, endIndex;

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
        for (int i = 0; i < array.length; i++) {
            if (i <= endIndex && !flag) {
                newArray[i] = array[i];
            }
            else {
                newArray[i + array.length] = array[i];
            }
            if (i == startIndex) {
                startIndex = i + array.length;
                flag = true;
            }
        }
        array = newArray;
    }

    /**
     * Add {@code x} of type T to the front of the deque. Assumes {@code x} is never null.
     *
     * @param x item of datatype T to add
     */
    @Override
    public void addFirst(T x) {
        if (size == array.length) {
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
        if (size == array.length) {
            resizeUp(2);
        }
        array[endIndex] = x;
        size += 1;
        endIndex += 1;
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
        for (int i = startValue; i < endIndex || i > startIndex; i++) {
            if (startIndex <= endIndex && i >= endIndex) {
                break;
            }
            returnList.add(array[i]);
            if (i == array.length - 1) {
                i = -1;
            }
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
        for (int i = 0; i < newArray.length; i++) {
            if (i <= endIndex) {
                newArray[i] = array[i];
            }
            else {
                newArray[i] = array[i + change];
            }
            if (i + change == startIndex) {
                startIndex = i;
            }
        }
        array = newArray;
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
        int startValue = startIndex + 1;
        if (startValue == array.length) {
            startValue = 0;
        }
        T returnItem = array[startValue];
        array[startValue] = null;
        startIndex = startValue;
        if (size > 16 && (double) size /array.length < 0.25) {
            resizeDown(2);
        }
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

        int endValue = endIndex - 1;
        if (endValue < 0) {
            endValue = array.length - 1;
        }
        T returnItem = array[endValue];
        array[endValue] = null;
        endIndex = endValue;
        if (size > 16 && (double) size /array.length < 0.25) {
            resizeDown(2);
        }
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
        if (index > array.length || index < 0) {
            return null;
        }
        return array[index];
    }

    /**
     * Error thrown when this unnecessary method is called
     */
    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }
}
