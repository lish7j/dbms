import java.io.IOException;
import java.util.LinkedHashMap;
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
        System.out.println("===");
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