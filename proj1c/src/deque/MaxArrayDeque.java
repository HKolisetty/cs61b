package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{
    private Comparator<T> prevComparator;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        prevComparator = c;
    }

    public T max() {
        return max(prevComparator);
    }

    public T max(Comparator<T> c) {
        prevComparator = c;
        if (isEmpty()) {
            return null;
        }
        T max = get(0);
        for (T i : this) {
            if (prevComparator.compare(i, max) > 0) {
                max = i;
            }
        }
        return max;
    }
}
