

import btree.BPlusTree;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class BPTable {
    private String name;//表名
    private File folder;//tree持久化的文件
    private static String userName;//用户姓名，切换或修改用户时修改
    private static String dbName;//数据库dataBase名，切换时修改

    private BPlusTree tree;

    /**
     * 只能静态创建，所以构造函数私有
     */
    private BPTable(String name) {
        this.name = name;
        this.folder = new File("dir" + "/" + userName + "/" + dbName + "/" + name);
        this.tree = new BPlusTree();
    }

    /**
     * 初始化表信息，包括用户和数据库
     *
     * @param userName 用户名
     * @param dbName   数据库名
     */
    public static void init(String userName, String dbName) {
        BPTable.userName = userName;
        BPTable.dbName = dbName;
    }

    /**
     * 创建一个新的表文件
     *
     * @param name 表名
     * @return 如果表存在返回失败的信息，否则返回success
     */
    public static String createTable(String name, Map<String, Field> fields) {
        if (existTable(name)) {
            return "创建表失败，因为已经存在表:" + name;
        }
        BPTable table = new BPTable(name);
        table.addDict(fields);
        return "success";
    }

    public static String dropTable(String name) {
        if (!existTable(name)) {
            return "错误：不存在表:" + name;
        }
        File folder = new File("dir" + "/" + userName + "/" + dbName + "/" + name);
        deleteFolder(folder);
        return "success";

    }

    /**
     * 根据表名获取表
     *
     * @param name 表名
     * @return 如果不存在此表返回null, 否则返回对应Table对象
     */
    public static BPTable getTable(String name) {
        if (!existTable(name)) {
            return null;
        }
        BPTable table = new BPTable(name);
        try (
                FileReader fr = new FileReader(table.dictFile);
                BufferedReader br = new BufferedReader(fr)
        ) {

            String line = null;
            //读到末尾是NULL
            while (null != (line = br.readLine())) {
                String[] fieldValues = line.split(" ");//用空格产拆分字段
                Field field = new Field();
                field.setName(fieldValues[0]);
                field.setType(fieldValues[1]);
                //如果长度为3说明此字段是主键
                if ("*".equals(fieldValues[2])) {
                    field.setPrimaryKey(true);
                } else {
                    field.setPrimaryKey(false);
                }
                //将字段的名字作为key
                table.fieldMap.put(fieldValues[0], field);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File[] dataFiles = new File(table.folder, "data").listFiles();
        if (null != dataFiles && 0 != dataFiles.length) {
            for (int i = 1; i <= dataFiles.length; i++) {
                File dataFile = new File(table.folder + "/data", i + ".data");
                table.dataFileSet.add(dataFile);
            }
        }

        if (table.indexFile.exists()) {
            table.readIndex();
        } /*else {
            table.buildIndex();
            //table.writeIndex();
        }*/

        return table;
    }


    private static void deleteFolder(File file) {
        if (file.isFile()) {//判断是否是文件
            file.delete();//删除文件
        } else if (file.isDirectory()) {//否则如果它是一个目录
            File[] files = file.listFiles();//声明目录下所有的文件 files[];
            for (int i = 0; i < files.length; i++) {//遍历目录下所有的文件
                deleteFolder(files[i]);//把每个文件用这个方法进行迭代
            }
            file.delete();//删除文件夹
        }
    }

    /**
     * 判断表是否存在
     *
     * @param name 表名
     * @return 存在与否
     */
    private static boolean existTable(String name) {
        File folder = new File("dir" + "/" + userName + "/" + dbName + "/" + name);
        return folder.exists();
    }
}
