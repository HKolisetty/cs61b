package ngrams;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        if (ts.isEmpty()) {
            return;
        }
        Integer low = ts.ceilingKey(startYear);
        Integer high = ts.floorKey(endYear);
        Integer key = low;
        this.put(key, ts.get(key));
        while (!key.equals(high)) {
            key = ts.higherKey(key);
            this.put(key, ts.get(key));
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        ArrayList<Integer> list = new ArrayList<>();
        if (this.isEmpty()) {
            return list;
        }
        Integer key = this.firstKey();
        list.add(key);
        while (!key.equals(this.lastKey())) {
            key = this.higherKey(key);
            list.add(key);
        }
        return list;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        ArrayList<Double> list = new ArrayList<>();
        if (this.isEmpty()) {
            return list;
        }
        Integer key = this.firstKey();
        list.add(this.get(key));
        while (!key.equals(this.lastKey())) {
            key = this.higherKey(key);
            list.add(this.get(key));
        }
        return list;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries copy = new TimeSeries();
        if (this.isEmpty() && ts.isEmpty()) {
            return copy;
        }
        Integer key1 = this.firstKey();
        Double value1 = this.get(key1);
        Integer key2 = ts.firstKey();
        Double value2 = ts.get(key2);
        while (!(key1.equals(this.lastKey()) && key2.equals(ts.lastKey()))) {
            if (key1.equals(key2)) {
                copy.put(key1, value1 + value2);
                key1 = getNext(key1, this);
                key2 = getNext(key2, ts);
                value1 = this.get(key1);
                value2 = ts.get(key2);
            } else {
                if (!ts.containsKey(key1)) {
                    copy.put(key1, value1);
                    key1 = getNext(key1, this);
                    value1 = this.get(key1);
                }
                if (!this.containsKey(key2)) {
                    copy.put(key2, value2);
                    key2 = getNext(key2, ts);
                    value2 = ts.get(key2);
                }
            }
        }
        if (key1.equals(key2)) {
            copy.put(key1, value1 + value2);
        } else {
            if (!ts.containsKey(key1)) {
                copy.put(key1, value1);
            }
            if (!this.containsKey(key2)) {
                copy.put(key2, value2);
            }
        }
        return copy;
    }
    private static Integer getNext(Integer key, TimeSeries ts) {
        if (key.equals(ts.lastKey())) {
            return key;
        }
        return ts.higherKey(key);
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries copy = new TimeSeries();
        if (this.isEmpty() && ts.isEmpty()) {
            return copy;
        }
        Integer key1 = this.firstKey();
        Double value1 = this.get(key1);
        Integer key2 = ts.firstKey();
        Double value2 = ts.get(key2);
        while (!(key1.equals(this.lastKey()) && key2.equals(ts.lastKey()))) {
            if (key1.equals(key2)) {
                copy.put(key1, value1 / value2);
                key1 = getNext(key1, this);
                key2 = getNext(key2, ts);
                value1 = this.get(key1);
                value2 = ts.get(key2);
            } else {
                if (!ts.containsKey(key1)) {
                    throw new IllegalArgumentException();
                }
                if (!this.containsKey(key2)) {
                    key2 = getNext(key2, ts);
                    value2 = ts.get(key2);
                }
            }
        }
        if (key1.equals(key2)) {
            copy.put(key1, value1 / value2);
        } else if (!ts.containsKey(key1)) {
            throw new IllegalArgumentException();
        }
        return copy;
    }

}
