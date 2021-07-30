
import bplustree.BPlusTree;
public class Test {

    public static void main(String[] args) {
    }


    @org.junit.Test
    public void TestTraverseBTree() {
        BPlusTree<Integer, Integer> tree = new BPlusTree<>(3);
        tree.insert(1, 1);
        tree.insert(2, 2);
        tree.insert(3, 3);
        tree.insert(4, 4);
        tree.insert(5, 5);
        System.out.println(tree);
    }


}
