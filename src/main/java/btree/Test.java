package btree;

public class Test {

    public static void main(String[] args) {
        BPlusTree<Integer, Integer> tree = new BPlusTree<>();
        tree.insert(1, 1);
        tree.insert(2, 2);
        tree.insert(3, 3);
        tree.insert(4, 4);
        tree.insert(5, 5);

        tree.delete(1);
        tree.printList();
        System.out.println(tree);
        tree.delete(2);
        tree.printList();
        System.out.println(tree);
        tree.delete(3);
        tree.printList();
        System.out.println(tree);
    }

}
