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
        int myfind = find(v);
        int size = 1;
        for (int i = 0; i < data.length; i++) {
            if (find(i) == myfind) {
                size += 1;
            }
        }
        return size;
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        if (data[v] == -1) {
            return -sizeOf(v);
        }
        return data[v];
    }

    /* Returns true if nodes/vertices V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
//        if (v1 == v2) {
//            return true;
//        } else if (parent(v1) < 0 && parent(v2) < 0) {
//            return false;
//        } else {
//            int nextv1 = v1;
//            int nextv2 = v2;
//            if (parent(v1) >= 0) {
//                nextv1 = parent(v1);
//            }
//            if (parent(v2) >= 0) {
//                nextv2 = parent(v2);
//            }
//            if (parent(v1) >= 0) {
//                data[v1] = find(v1);
//            }
//            if (parent(v2) >= 0) {
//                data[v2] = find(v2);
//            }
//            return connected(nextv1,nextv2);
//        }
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v < 0 || v > data.length) {
            throw new IllegalArgumentException();
        }
        if (data[v] < 0) {
            return v;
        }
        data[v] = rootfind(v);
        return find(data[v]);
    }
    public int rootfind(int v) {
        if (data[v] < 0) {
            return v;
        }
        return rootfind(data[v]);
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing a item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        int rootv1 = find(v1);
        int rootv2 = find(v2);
        int larger = rootv2;
        int smaller = rootv1;
        if (sizeOf(v1) > sizeOf(v2)) {
            larger = rootv1;
            smaller = rootv2;
        }
        if (larger != smaller) {
            data[smaller] = larger;
        }

    }

    /**
     * DO NOT DELETE OR MODIFY THIS, OTHERWISE THE TESTS WILL NOT PASS.
     */
    public int[] returnData() {
        return data;
    }
}
