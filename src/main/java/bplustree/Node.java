package bplustree;

import java.util.List;

abstract class Node<K, V> {

    public static final int branchingFactor = 4;
    List<K> keys;

    int keyNumber() {
        return keys.size();
    }

    abstract V getValue(K key);

    abstract Node deleteValue(K key);

    abstract Node insertValue(K key, V value);

    abstract K getFirstLeafKey();

    abstract List<V> getRange(K key1, RangePolicy policy1, K key2,
            RangePolicy policy2);

    abstract void merge(Node sibling);

    abstract Node split();

    abstract boolean isOverflow();

    abstract boolean isUnderflow();

    /*public void setBranchingFactor(int branchingFactor) {
        this.branchingFactor = branchingFactor;
    }*/

    public String toString() {
        return keys.toString();
    }
}