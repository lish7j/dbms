package bplustree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LeafNode<K extends Comparable<? super K>, V> extends Node<K, V> {
    List<V> values;
    LeafNode<K, V> next;
    LeafNode<K, V> before;

    LeafNode() {
        this.keys = new ArrayList<K>();
        this.values = new ArrayList<V>();
    }

    @Override
    V getValue(K key) {
        int loc = Collections.binarySearch(keys, key);
        return loc >= 0 ? values.get(loc) : null;
    }

    @Override
    Node deleteValue(K key) {
        int loc = Collections.binarySearch(keys, key);
        if (loc >= 0) {
            keys.remove(loc);
            values.remove(loc);
        }
        if (keys.size() == 0) {
            this.before.next = this.next;
            if (this.next != null)
                this.next.before = this.before;
            this.next = null;
            this.before = null;
            return null;
        } else {
            return this;
        }
    }

    @Override
    Node insertValue(K key, V value) {
        int loc = Collections.binarySearch(keys, key);
        int valueIndex = loc >= 0 ? loc : -loc - 1;
        if (loc >= 0) {
            values.set(valueIndex, value);
        } else {
            keys.add(valueIndex, key);
            values.add(valueIndex, value);
        }
        if (isOverflow()) {
            Node sibling = split();
            InternalNode newRoot = new InternalNode();
            newRoot.keys.add(this.getFirstLeafKey());
            newRoot.children.add(this);
            newRoot.children.add(sibling);
            return newRoot;
        }
        return this;
    }

    @Override
    K getFirstLeafKey() {
        return keys.get(0);
    }

    @Override
    List<V> getRange(K key1, RangePolicy policy1, K key2,
            RangePolicy policy2) {
        List<V> result = new LinkedList<>();
        LeafNode<K, V> node = this;
        while (node != null) {
            Iterator<K> kIt = node.keys.iterator();
            Iterator<V> vIt = node.values.iterator();
            while (kIt.hasNext()) {
                K key = kIt.next();
                V value = vIt.next();
                int cmp1 = key.compareTo(key1);
                int cmp2 = key.compareTo(key2);
                if (((policy1 == RangePolicy.EXCLUSIVE && cmp1 > 0) || (policy1 == RangePolicy.INCLUSIVE && cmp1 >= 0))
                        && ((policy2 == RangePolicy.EXCLUSIVE && cmp2 < 0) || (policy2 == RangePolicy.INCLUSIVE && cmp2 <= 0)))
                    result.add(value);
                else if ((policy2 == RangePolicy.EXCLUSIVE && cmp2 >= 0)
                        || (policy2 == RangePolicy.INCLUSIVE && cmp2 > 0)) {
                    return result;
                }
            }
            node = node.next;
        }
        return result;
    }

    @Override
    void merge(Node sibling) {
        if (sibling == null)
            return;
        if (sibling instanceof InternalNode) {
            LeafNode<K, V> nextLeafNode = this.next;
            if (nextLeafNode == null)
                return;
            K firstKey = nextLeafNode.getFirstLeafKey();
            //((InternalNode<K, V>) sibling).deleteChild(firstKey);
            ((InternalNode<K, V>) sibling).deleteValue(firstKey);
            this.merge(nextLeafNode);
            return;
        }
        if (this == sibling)
            return;
        @SuppressWarnings("unchecked")
        LeafNode node = (LeafNode) sibling;
        keys.addAll(node.keys);
        values.addAll(node.values);
        next = node.next;
        if (next != null)
            node.next.before = this;
    }

    /**
      均分keys, 把叶子节点右半部分的值分割出来成为一个独立的叶子节点
     */
    @Override
    Node split() {
        LeafNode<K, V> sibling = new LeafNode();
        int from = (keyNumber() + 1) / 2, to = keyNumber();
        sibling.keys.addAll(keys.subList(from, to));
        sibling.values.addAll(values.subList(from, to));

        keys.subList(from, to).clear();
        values.subList(from, to).clear();

        sibling.next = next;
        sibling.before = this;
        next = sibling;
        return sibling;
    }

    @Override
    boolean isOverflow() {
        return values.size() > branchingFactor - 1;
    }

    @Override
    boolean isUnderflow() {
        return values.size() < branchingFactor / 2;
    }
}