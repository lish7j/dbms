import bplustree.BPlusTree;
import bplustree.BTree;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) throws IOException {
//        Operating operating = new Operating();
//        operating.dbms();
//        File f = new File("did/dx", "dd.ii");
//        FileOutputStream fos = new FileOutputStream(f);
//        fos.write(100);
//        fos.flush();
//        fos.close();
        //Test_regex();
//        Test_linkedMap();
        //Test_delete();

//        Test_deleteValue();
        Test_Btree();
    }

    public static void Test_Btree() {
        BPlusTree<Integer, Integer> bTree = new BPlusTree<>(3);
        bTree.insert(1,1);
        bTree.insert(2,2);
        bTree.insert(3,3);
        bTree.insert(4,4);
        bTree.insert(5,5);
        bTree.insert(6,6);
        bTree.insert(7,7);
        bTree.insert(8,8);
//        System.out.println(bTree);
//        bTree.printList();
//        bTree.delete(3);
//        bTree.printList();
//        System.out.println(bTree);
//        bTree.delete(2);
//        bTree.printList();
//        System.out.println(bTree);
//        bTree.delete(6);
//        bTree.printList();
//        System.out.println(bTree);
//        bTree.delete(1);
//        bTree.printList();
//        System.out.println(bTree);
//        bTree.delete(4);
//        bTree.printList();
//        System.out.println(bTree);
//        bTree.delete(5);
//        bTree.printList();
//        System.out.println(bTree);
//        System.out.println(bTree.search(2));
        System.out.println(bTree);
        bTree.delete(2);
        System.out.println(bTree);
        bTree.printList();
    }

    public static void Test_deleteValue() {
        BPlusTree<Integer, Integer> tree = new BPlusTree<>();
        for (int i = 1; i <= 8; i++) {
            tree.insert(i, i);
        }
        System.out.println(tree);
        tree.delete(3);
//        for (int i = 3; i<= 4; i++) {
//            tree.delete(i);
//        }
        System.out.println(tree);
        tree.printList();
        tree.printFromEndToStart();
    }

    public static void Test_delete() {
        String cmd = "delete from table_name where name = 3 and tr=2 and tt>4;";
        Pattern PATTERN_DELETE = Pattern.compile("delete\\sfrom\\s(\\w+)(?:\\swhere\\s(\\w+\\s?[<=>]\\s?[^\\s\\;]+(?:\\sand\\s(?:\\w+)\\s?(?:[<=>])\\s?(?:[^\\s\\;]+))*))?\\s?;");
        Matcher m = PATTERN_DELETE.matcher(cmd);
        while (m.find()) {
            System.out.println(m.group(1) + " " + m.group(2));
        }


    }
    public static void Test_linkedMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("212", "233");
        map.put("32", "233");
        map.put("23", "662");
        map.put("22", "66");
        map.put("88", "99");
        for (Map.Entry<String, String> e : map.entrySet()) {
            System.out.println(e.getKey() + " " + e.getValue());
        }
        Set<String> sets = map.keySet();
        for (String s : sets) {
            System.out.println(s);
        }
    }
    public static void Test_regex() {
        String regex = "(\\w+)\\sin\\s\\(((?:\\w+\\s\\w+)+)\\)";
        Pattern pa = Pattern.compile(regex);
        String cmd = "table in (wq err wqsw eeq wqssq waqw)";
        Matcher matcher = pa.matcher(cmd);
        while (matcher.find()) {
            String t = matcher.group(1);
            String g = matcher.group(2);
            //String s = matcher.group(3);
            System.out.println(t + " " + g + " "  + "\n");
        }
    }
}