package bplustree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InternalNode<K extends Comparable<? super K>, V> extends Node<K, V> {
    List<Node<K, V>> children;

    InternalNode() {
        this.keys = new ArrayList<K>();
        this.children = new ArrayList<Node<K, V>>();
    }

    @Override
    V getValue(K key) {
        Node<K, V> child = getChild(key);
        if (child == null)
            return null;
        return child.getValue(key);
    }

    @Override
    Node deleteValue(K key) {
        int childIndex = getChildIndex(key);
        if (childIndex >= children.size())
            return this;
        Node child = children.get(childIndex);
        Node newChild = child.deleteValue(key);
        if (newChild == null) {
            List<Node<K, V>> newChildren = new ArrayList<>();
            if (children.size() == 1) {
                this.children.clear();
                return null;
            }
            // 是否可以直接删除呢 children.remove(child);
            //
            for (int i = 0; i < children.size(); i++) {
                if (i == childIndex)
                    continue;;
                newChildren.add(children.get(i));
            }
            this.children.clear();
            this.children = newChildren;
            this.keys.clear();
            for (int i = 0; i < children.size(); i++) {
                this.keys.add(children.get(i).getFirstLeafKey());
            }
            return this;
        } else if (child.isUnderflow()) {
            Node childLeftSibling = getChildLeftSibling(key);
            Node childRightSibling = getChildRightSibling(key);
            Node left = childLeftSibling != null ? childLeftSibling : child;
            Node right = childLeftSibling != null ? child
                    : childRightSibling;
            left.merge(right);
            Node node = deleteValue((K)right.getFirstLeafKey());

            if (left.isOverflow()) {
                Node sibling = left.split();
                insertChild((K)sibling.getFirstLeafKey(), sibling);
            }
            if (keyNumber() == 0 || children.size() == 0) {
                return null;
            }
        }
        return this;
    }

    @Override
    Node insertValue(K key, V value) {
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
            insertChild((K)sibling.getFirstLeafKey(), sibling);
        }
        if (isOverflow()) {
            Node sibling = split();
            InternalNode newRoot = new InternalNode();
            newRoot.keys.add(sibling.getFirstLeafKey());
            newRoot.children.add(this);
            newRoot.children.add(sibling);
            return newRoot;
        }
        return this;
    }

    @Override
    K getFirstLeafKey() {
        return (K)children.get(0).getFirstLeafKey();
    }

    @Override
    List<V> getRange(K key1, RangePolicy policy1, K key2,
            RangePolicy policy2) {
        Node child = getChild(key1);
        return child.getRange(key1, policy1, key2, policy2);
    }

    @Override
    void merge(Node sibling) {
        @SuppressWarnings("unchecked")
        InternalNode node = (InternalNode) sibling;
        keys.add((K)node.getFirstLeafKey());
        keys.addAll(node.keys);
        children.addAll(node.children);
    }

    @Override
    Node split() {
        int from = keyNumber() / 2 + 1, to = keyNumber();
        InternalNode sibling = new InternalNode();
        sibling.keys.addAll(keys.subList(from, to));
        sibling.children.addAll(children.subList(from, to + 1));

        keys.subList(from - 1, to).clear();
        children.subList(from, to + 1).clear();

        return sibling;
    }

    @Override
    boolean isOverflow() {
        return children.size() > branchingFactor;
    }

    @Override
    boolean isUnderflow() {
        return children.size() < (branchingFactor + 1) / 2;
    }

    int getChildIndex(K key) {
        int loc = Collections.binarySearch(keys, key);
        int childIndex = loc >= 0 ? loc : -loc - 1;
        return childIndex;
    }

    Node getChildForDelete(K key) {
        int loc = Collections.binarySearch(keys, key);
        int childIndex = loc >= 0 ? loc : -loc - 1;
        if (loc < children.size()) {
            return children.get(loc);
        }
        return null;
    }

    Node getChild(K key) {
        int loc = Collections.binarySearch(keys, key);
        int childIndex = loc >= 0 ? loc : -loc - 1;
        if (loc < children.size()) {
            return children.get(loc);
        }
        return null;
    }

    Node getFirstChild() {
        return children.get(0);
    }

//    Node deleteChild(K key) {
//        int loc = Collections.binarySearch(keys, key);
//        if (loc >= 0 && loc < children.size()) {
//            keys.remove(loc);
//            Node child = children.get(loc);
//            if (child instanceof InternalNode) {
//                ((InternalNode<K, V>) child).deleteChild(key);
//            } else {
//                children.remove(child);
//            }
//        }
//        if (children.size() == 0) {
//            return null;
//        }
//        return this;
//    }

    void insertChild(K key, Node child) {
        int loc = Collections.binarySearch(keys, key);
        int childIndex = loc >= 0 ? loc + 1 : -loc - 1;
        if (loc >= 0) {
            children.set(childIndex, child);
        } else {
            keys.add(childIndex, key);
            children.add(childIndex + 1, child);
        }
    }

    Node getChildLeftSibling(K key) {
        int loc = Collections.binarySearch(keys, key);
        int childIndex = loc >= 0 ? loc : -loc - 1;
        if (childIndex > 0) {
            return children.get(childIndex - 1);
        }
        return null;
    }

    Node getChildRightSibling(K key) {
        int loc = Collections.binarySearch(keys, key);
        int childIndex = loc >= 0 ? loc : -loc - 1;
        if (childIndex < keyNumber()) {
            return children.get(childIndex + 1);
        }
        return null;
    }
}