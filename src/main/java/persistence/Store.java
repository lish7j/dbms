package persistence;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Store {

    public static void main(String[] args) throws Exception {
        BinTree t = new BinTree();
        t.insert(3);
        t.insert(1);
        t.insert(4);
        t.insert(6);
        t.insert(10);
        t.insert(20);
        t.insert(100);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tree.txt"));
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("tree.txt"))) {
            out.writeObject(t);
            BinTree nt = (BinTree) in.readObject();
            System.out.println(nt);
        }
        System.out.println(t);
    }

}
