import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Percolation {
    int length;
    HashSet<Integer> open;
    WeightedQuickUnionUF data;
    int first;
    int top;
    int bottom;


    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        length = N;
        open = new HashSet<Integer>(N);
        data = new WeightedQuickUnionUF((length * length) + 2);
        top = length * length;
        bottom = (length * length) + 1;

    }

    public void open(int row, int col) {
        if (row < 0 || row >= length || col < 0 || col >= length) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int value = xyTo1D(row, col);
        if (open.contains(value)) {
            return;
        }
        open.add(value);
        if (row > 0) {
            if (isOpen(row - 1, col)) {
                data.union(value, xyTo1D(row - 1, col));
            }
        }
        if (row < length - 1) {
            if (isOpen(row + 1, col)) {
                data.union(value, xyTo1D(row + 1, col));
            }
        }
        if (col > 0) {
            if (isOpen(row, col - 1)) {
                data.union(value, xyTo1D(row, col - 1));
            }
        }
        if (col < length - 1) {
            if (isOpen(row, col + 1)) {
                data.union(value, xyTo1D(row, col + 1));
            }
        }
        if (row == 0) {
            data.union(value, top);
        } else if (row == length - 1) {
            data.union(value, bottom);
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= length || col < 0 || col >= length) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return open.contains(xyTo1D(row, col));
    }

    public boolean isFull(int row, int col) {
        if (row < 0 || row >= length || col < 0 || col >= length) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return data.connected(top, xyTo1D(row, col));
    }

    public int numberOfOpenSites() {
        return open.size();
    }

    public boolean percolates() {
        return data.connected(top, bottom);
    }

    private int xyTo1D(int row, int col) {
        if (row < 0 || row >= length || col < 0 || col >= length) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return (length * row) + col;
    }
}
