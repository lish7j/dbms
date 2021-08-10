package btree;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import persistence.BinTree;

public class LeafNode<K extends Comparable<? super K>, V> extends Node<K, V> implements Serializable {
    private static final long serialVersionUID = 7778367429773455679L;

    protected List<V> values;
    protected LeafNode<K, V> next;
    protected LeafNode<K, V> before;

    public LeafNode() {
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    @Override
    public V getValue(K key) {
        int loc = Collections.binarySearch(keys, key);
        return loc >= 0 ? values.get(loc) : null;
    }

    @Override
    public Node deleteValue(K key) {
        int loc = Collections.binarySearch(keys, key);
        if (loc >= 0) {
            this.keys.remove(loc);
            this.values.remove(loc);
        }
        if (keys.size() == 0) {
            if (this.before != null)
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
    public Node insertValue(K key, V value) {
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
            newRoot.keys.add(sibling.getFirstLeafKey());
            newRoot.children.add(this);
            newRoot.children.add(sibling);
            return newRoot;
        }
        return this;
    }

    @Override
    public K getFirstLeafKey() {
        return keys.get(0);
    }

    @Override
    public void merge(Node sibling) {
        if (sibling == null)
            return;
        if (sibling instanceof InternalNode) {
            LeafNode<K, V> nextLeafNode = this.next;
            if (nextLeafNode == null)
                return;
            K firstKey = nextLeafNode.getFirstLeafKey();
            ((InternalNode<K, V>) sibling).deleteValue(firstKey);
            this.merge(nextLeafNode);
            return;
        }
        if (this == sibling)
            return;

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
    public Node split() {
        LeafNode<K, V> sibling = new LeafNode();
        int from = (keys.size() + 1) / 2, to = keys.size();
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
    public boolean isOverflow() {
        return values.size() > branchingFactor - 1;
    }

    @Override
    public boolean isUnderflow() {
        return values.size() < branchingFactor / 2;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(before);
        out.writeObject(next);
        out.writeInt(keys.size());
        for (int i = 0; i < keys.size(); i++) {
            out.writeObject(keys.get(i));
            out.writeObject(values.get(i));
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        before = (LeafNode<K, V>)in.readObject();
        next = (LeafNode<K, V>)in.readObject();
        int size = in.readInt();
        System.out.println("size is " + size);
        if (size > 0) {
            this.keys = new ArrayList<>(size);
            this.values = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                K k = (K)in.readObject();
                V v = (V)in.readObject();
                keys.add(k);
                values.add(v);
            }
        }
    }
}
