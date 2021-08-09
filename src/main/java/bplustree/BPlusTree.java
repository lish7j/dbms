package bplustree;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BPlusTree<K extends Comparable<? super K>, V> {
    /**
     * The branching factor used when none specified in constructor.
     */
    private static final int DEFAULT_BRANCHING_FACTOR = 128;

    /**
     * The branching factor for the B+ tree, that measures the capacity of nodes
     * (i.e., the number of children nodes) for internal nodes in the tree.
     */
    private int branchingFactor;

    /**
     * The root node of the B+ tree.
     */
    private Node<K, V> root;
    private LeafNode<K, V> leafNode;

    public BPlusTree() {
        this(DEFAULT_BRANCHING_FACTOR);
    }

    public BPlusTree(int branchingFactor) {
        if (branchingFactor <= 2)
            throw new IllegalArgumentException("Illegal branching factor: "
                    + branchingFactor);
        this.branchingFactor = branchingFactor;
        root = new LeafNode<>();
    }

    /**
     * Returns the value to which the specified key is associated, or
     * {@code null} if this tree contains no association for the key.
     *
     * <p>
     * A return value of {@code null} does not <i>necessarily</i> indicate that
     * the tree contains no association for the key; it's also possible that the
     * tree explicitly associates the key to {@code null}.
     *
     * @param key
     *            the key whose associated value is to be returned
     *
     * @return the value to which the specified key is associated, or
     *         {@code null} if this tree contains no association for the key
     */
    public V search(K key) {
        return root.getValue(key);
    }

    /**
     * Returns the values associated with the keys specified by the range:
     * {@code key1} and {@code key2}.
     *
     * @param key1
     *            the start key of the range
     * @param policy1
     *            the range policy, {@link RangePolicy#EXCLUSIVE} or
     *            {@link RangePolicy#INCLUSIVE}
     * @param key2
     *            the end end of the range
     * @param policy2
     *            the range policy, {@link RangePolicy#EXCLUSIVE} or
     *            {@link RangePolicy#INCLUSIVE}
     * @return the values associated with the keys specified by the range:
     *         {@code key1} and {@code key2}
     */
    public List<V> searchRange(K key1, RangePolicy policy1, K key2,
            RangePolicy policy2) {
        return root.getRange(key1, policy1, key2, policy2);
    }

    /**
     * Associates the specified value with the specified key in this tree. If
     * the tree previously contained a association for the key, the old value is
     * replaced.
     *
     * @param key
     *            the key with which the specified value is to be associated
     * @param value
     *            the value to be associated with the specified key
     */
    public void insert(K key, V value) {
        root = root.insertValue(key, value);
    }

    /**
     * Removes the association for the specified key from this tree if present.
     *
     * @param key
     *            the key whose association is to be removed from the tree
     */
    public void delete(K key) {
        root = root.deleteValue(key);
    }

    public void printFromEndToStart() {
        if (root instanceof LeafNode) {
            LeafNode<K, V> r = (LeafNode<K, V>) root;
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            for (int i = 0; i < r.keys.size(); i++) {
                sb.append("(").append(r.keys.get(i)).append(", ").append(r.values.get(i)).append(")");
            }
            sb.append("}");
            System.out.println(sb);
        } else {
            InternalNode<K, V> r = (InternalNode<K, V>) root;
            Node child = r;
            do {
                r = (InternalNode<K, V>)child;
                child = r.getFirstChild();
            } while (child instanceof InternalNode);
            LeafNode<K, V> first = (LeafNode<K, V>) child;
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            while (first.next != null) {
                first = first.next;
            }
            while (first != null) {
                sb.append("[");
                for (int i = 0; i < first.keys.size(); i++) {
                    sb.append("(").append(first.keys.get(i)).append(", ").append(first.values.get(i)).append(")");
                }
                sb.append("]");
                first = first.before;
            }
            sb.append("}");
            System.out.println(sb);
        }
    }
    public void printList() {
        if (root instanceof LeafNode) {
            LeafNode<K, V> r = (LeafNode<K, V>) root;
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            for (int i = 0; i < r.keys.size(); i++) {
                sb.append("(").append(r.keys.get(i)).append(", ").append(r.values.get(i)).append(")");
            }
            sb.append("}");
            System.out.println(sb);
        } else {
            InternalNode<K, V> r = (InternalNode<K, V>) root;
            Node child = r;
            do {
                r = (InternalNode<K, V>)child;
                child = r.getFirstChild();
            } while (child instanceof InternalNode);
            LeafNode<K, V> first = (LeafNode<K, V>) child;
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            while (first != null) {
                sb.append("[");
                for (int i = 0; i < first.keys.size(); i++) {
                    sb.append("(").append(first.keys.get(i)).append(", ").append(first.values.get(i)).append(")");
                }
                sb.append("]");
                first = first.next;
            }
            sb.append("}");
            System.out.println(sb);
        }
    }

    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList((Node) root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else
                    sb.append('\n');
            }
            queue = nextQueue;
        }

        return sb.toString();
    }
}
