package btree;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Test {

    public static void main(String[] args) {

        BPlusTree<Integer, Integer> tree = new BPlusTree<>();
        tree.insert(1, 1);
        tree.insert(2, 2);
        tree.insert(3, 3);
        tree.insert(4, 4);
        tree.insert(5, 5);

        try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("tree.ini"));
                ObjectInputStream i = new ObjectInputStream(new FileInputStream("tree.ini"));) {
            o.writeObject(tree);
            BPlusTree<Integer, Integer> p = (BPlusTree<Integer, Integer>)i.readObject();
            System.out.println(p);
            p.printList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
