package btree;


import java.util.List;

public abstract class Node<K, V> {
    protected static final int branchingFactor = 4;
    protected List<K> keys;

    private int keyNumber() {
        return keys.size();
    }

    protected abstract V getValue(K key);

    protected abstract Node deleteValue(K key);

    protected abstract Node insertValue(K key, V value);

    protected abstract K getFirstLeafKey();

    protected abstract void merge(Node sibling);

    protected abstract Node split();

    protected abstract boolean isOverflow();

    protected abstract boolean isUnderflow();

    public String toString() {
        return keys.toString();
    }
}