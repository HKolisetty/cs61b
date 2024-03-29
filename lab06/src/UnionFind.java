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

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        data = new int[N];
        for (int i = 0; i < N; i++) {
            data[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        return -data[find(v)];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        return data[v];
    }

    /* Returns true if nodes/vertices V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v < 0 || v > data.length) {
            throw new IllegalArgumentException();
        }
        int root = v;
        while (data[root] >= 0) {
            root = data[root];
        }
// This does bottom-up compression:
        while (v != root) {
            int nextv = data[v];
            data[v] = root;
            v = nextv;
        }
        return root;
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing a item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        if (v1 != v2 && !connected(v1, v2)) {
//            helper(find(v1), find(v2));
            int larger = find(v2);
            int smaller = find(v1);
            if (sizeOf(v1) > sizeOf(v2)) {
                larger = find(v1);
                smaller = find(v2);
            }
            if (larger != smaller) {
                if (find(smaller) != larger) {
                    data[larger] += parent(smaller);
                }
                data[smaller] = larger;
            }
        }
    }
    // This does top-down compression:
    public void helper(int v1, int v2) {
        int larger = v2;
        int smaller = v1;
        if (sizeOf(v1) > sizeOf(v2)) {
            larger = v1;
            smaller = v2;
        }
        if (larger != smaller) {
            if (find(smaller) != larger) {
                data[larger] += parent(smaller);
            }
            data[smaller] = larger;
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] == smaller) {
                helper(i, larger);
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
