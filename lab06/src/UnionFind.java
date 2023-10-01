import edu.princeton.cs.algs4.In;

import javax.lang.model.type.ArrayType;
import java.util.ArrayList;

public class UnionFind {
    /**
     * DO NOT DELETE OR MODIFY THIS, OTHERWISE THE TESTS WILL NOT PASS.
     * You can assume that we are only working with non-negative integers as the items
     * in our disjoint sets.
     */
    private int[] data;
    private int[] size;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        data = new int[N];
        size = new int[N];
        for (int i = 0; i < N; i++) {
            data[i] = -1;
            size[i] = 1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        return size[find(v)];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        if (data[v] == -1) {
            return -size[v];
        }
        return data[v];
    }

    /* Returns true if nodes/vertices V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        if (v1 == v2) {
            return true;
        } else if (parent(v1) < 0 && parent(v2) < 0) {
            return false;
        } else {
            int nextv1 = v1;
            int nextv2 = v2;
            if (parent(v1) >= 0) {
                nextv1 = parent(v1);
                data[v1] = find(v1);
            }
            if (parent(v2) >= 0) {
                nextv2 = parent(v2);
                data[v2] = find(v2);
            }
            return connected(nextv1, nextv2);
        }
    }
    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v < 0 || v > data.length) {
            throw new IllegalArgumentException();
        }
        if (parent(v) < 0) {
            return v;
        }
        return find(data[v]);
    }
    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing a item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        if (find(v1) != find(v2)) {
            helper(find(v1), find(v2));
        } else {
            helper(v1, find(v1));
            helper(v2, find(v2));
        }

    }
    public void helper(int v1, int v2) {
        int larger = v2;
        int smaller = v1;
        if (sizeOf(v1) > sizeOf(v2)) {
            larger = v1;
            smaller = v2;
        }
        if (smaller != larger) {
            data[smaller] = larger;
            size[larger] = size[larger] + size[smaller];
        }
        if (size[smaller] != 1) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] == smaller) {
                    helper(i, larger);
                }
            }
        }

    }

    /**
     * DO NOT DELETE OR MODIFY THIS, OTHERWISE THE TESTS WILL NOT PASS.
     */
    public int[] returnData() {
        return data;
    }
}
