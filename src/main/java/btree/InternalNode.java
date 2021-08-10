package btree;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InternalNode<K extends Comparable<? super K>, V> extends Node<K, V> implements Serializable {
    private static final long serialVersionUID = -3612776625009174721L;

    protected List<Node<K, V>> children;

    public InternalNode() {
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> child = getChild(key);
        if (child != null) {
            return child.getValue(key);
        }
        return null;
    }

    @Override
    public Node deleteValue(K key) {
        int childIndex = getChildIndex(key);
        if (childIndex >= keys.size())
            return this;
        if (keys.get(childIndex).compareTo(key) > 0) {
            childIndex--;
        }
        if (childIndex < 0) {
            return this;
        }
        Node child = children.get(childIndex);
        Node newChild = child.deleteValue(key);
        if (newChild == null) {
            List<Node<K, V>> newChildren = new ArrayList<>();
            if (children.size() == 1) {
                this.children.clear();
                return null;
            }
            for (int i = 0; i < children.size(); i++) {
                if (i == childIndex)
                    continue;
                newChildren.add(children.get(i));
            }
            this.children.clear();
            this.children = newChildren;
            this.keys.clear();
            for (int i = 0; i < children.size(); i++) {
                this.keys.add(children.get(i).getFirstLeafKey());
            }
            return this;
        }
        this.keys.clear();
        for (int i = 0; i < children.size(); i++) {
            this.keys.add(children.get(i).getFirstLeafKey());
        }
        return this;
    }

    @Override
    public Node insertValue(K key, V value) {
        int childIndex = getChildIndex(key);
        if (childIndex >= children.size()) {
            childIndex = children.size() - 1;
        }
        Node child = children.get(childIndex);
        Node newChild = child.insertValue(key, value);
        if (newChild != child) {
            children.set(childIndex, newChild);
        }
        if (child.isOverflow()) {
            Node sibling = child.split();
            this.keys.add(childIndex + 1, (K)sibling.getFirstLeafKey());
            this.children.add(childIndex + 1, sibling);
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
        InternalNode node = (InternalNode) sibling;
        keys.add((K)node.getFirstLeafKey());
        keys.addAll(node.keys);
        children.addAll(node.children);
    }

    @Override
    public Node split() {
        int from = keys.size() / 2 + 1, to = keys.size();
        InternalNode sibling = new InternalNode();
        sibling.keys.addAll(keys.subList(from, to));
        sibling.children.addAll(children.subList(from, to + 1));
        keys.subList(from - 1, to).clear();
        children.subList(from, to + 1).clear();
        return sibling;
    }

    @Override
    protected boolean isOverflow() {
        return false;
    }

    @Override
    protected boolean isUnderflow() {
        return false;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    private Node getChild(K key) {
        int loc = Collections.binarySearch(keys, key);
        int childIndex = loc >= 0 ? loc : -loc - 1;
        if (childIndex < children.size()) {
            return children.get(childIndex);
        }
        return null;
    }

    private int getChildIndex(K key) {
        int loc = Collections.binarySearch(keys, key);
        int childIndex = loc >= 0 ? loc : -loc - 1;
        return childIndex;
    }

    public Node getFirstChild() {
        return children.get(0);
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeInt(keys.size());
        for (int i = 0; i < keys.size(); i++) {
            out.writeObject(keys.get(i));
            out.writeObject(children.get(i));
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        int size = in.readInt();
        if (size > 0) {
            this.keys = new ArrayList<>(size);
            this.children = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                K k = (K)in.readObject();
                Node<K, V> v = (Node<K, V>)in.readObject();
                keys.add(k);
                children.add(v);
            }
        }
    }
}
