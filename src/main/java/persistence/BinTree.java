package persistence;

import java.io.IOException;
import java.io.Serializable;

public class BinTree implements Serializable {
    Node root;

    static class Node implements Serializable {

        int val;
        Node left, right;

        public Node(int val) {
            this.val = val;
        }

        public void insert(int v) {
            if (val < v && right == null) {
                right = new Node(v);
            } else if (val > v && left == null)
                left = new Node(v);
            else if (val < v)
                right.insert(v);
            else
                left.insert(v);
        }

        public boolean get(int v) {
            if (v == val)
                return true;
            else if (v > val && right != null)
                return right.get(v);
            else if (v < val && left != null) {
                return left.get(v);
            }
            return false;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "val=" + val +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }

        private void writeObject(java.io.ObjectOutputStream out) throws IOException {
            out.writeInt(val);
            out.writeObject(left);
            out.writeObject(right);
        }

        private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
            val = in.readInt();
            left = (Node)in.readObject();
            right = (Node)in.readObject();
        }
    }
    public void insert(int val) {
        if (root == null)
            root = new Node(val);
        else
            root.insert(val);
    }

    public boolean get(int val) {
        if (root == null)
            return false;
        return root.get(val);
    }

    @Override
    public String toString() {
        if (root == null) {
            return "Tree{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Tree{");
        sb.append(root);
        sb.append("}");
        return sb.toString();
    }
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(root);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        root = (Node)in.readObject();
    }
}