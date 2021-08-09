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
//        Test_Btree();
        Socket s = new Socket("127.0.0.1", 8083);
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        br.write("GET / HTTP1.1\r\n");
        br.write("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.70\n"
                + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\r\n");
        br.write("Connection: keep-alive\r\n");
        br.flush();
        br.close();
    }

    public static void Test_Btree() {
        BTree<Integer, Integer> bTree = new BTree<>(3);
        bTree.insert(1,1);
        bTree.insert(2,2);
        bTree.insert(3,3);
        bTree.insert(4,4);
        bTree.insert(5,5);
        bTree.insert(6,6);
        bTree.insert(7,7);
        bTree.insert(8,8);
        System.out.println(bTree);
        bTree.delete(3);
        System.out.println(bTree);
        bTree.delete(2);
        System.out.println(bTree);
        bTree.delete(6);
        System.out.println(bTree);
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