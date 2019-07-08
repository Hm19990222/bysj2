package com.hm;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySqlUtil {

    /**
     * 2018/11/7 18:02
     * ycl
     * 连接数据库,将数据库所有表生成JavaBean
     * 使用即粘,用后就删,方便快捷,简约强大
     *
     * 使用方法
     *      0: 添加Maven依赖(如不使用lombok,则无需添加)
     *      1: 将MySqlUtil粘贴到包中(粘到哪里,JavaBean在哪里生成)
     *      2: 修改MySql的配置
     *      3: 执行main函数
     *

     * Maven依赖

             <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>5.1.32</version>
             </dependency>

           <dependency>
              <groupId>lombok</groupId>
              <artifactId>lombok</artifactId>
              <version>1.0</version>
          </dependency>

     */

    /**
     * 注 : 1.只支持idea
     *      2. 字段仅支持 int double date String float
     *      3.需要配置lombok(不配就要手动添加GET SET)
     *          若尚未配置 参考 https://jingyan.baidu.com/article/0a52e3f4e53ca1bf63ed725c.html
     */

    // MySql的配置
    private final String driver = "com.mysql.jdbc.Driver";
    private final String url = "jdbc:mysql://127.0.0.1:3306/book";
    private final String user = "root";
    private final String password = "";


    //以下是为了更好的体验的辅助功能(如果不需要,就当做没看到)
    //========================================================================

     /**
      * 忽略数据库字段头
      *     忽略的字段头(表名: t_user, 可以设置 hand="t_", 这样javabean就会忽略t_)
      *     如果不全是以"t_"开头, 不是的则会被忽略
      *     如果出现这种 user, t_user, 后者会覆盖前者
      */
    private final String hand = "";

    /**
     *  指定生成表
     *      (列: 生成user表, appoint = {"user"})
     *      注: 名字与数据库名字一致
     */
    private final String[] appoint = {};

    // 时间转化格式(可自定义)
    private final String pattern = "yyyy-MM-dd HH:mm:ss";

    // 是否使用lombok(不使用就手动添加GET SET)
    private final boolean islombok = true;

    /**
     * 生成(dao/mapper)
     *      这里会在当前目录下创建一个dao文件夹, 生成mapper
     *      你也可以自定义这个文件夹的名字,这样复制会方便许多
     *      例(daopath = "com.czxy.bos.dao")
     */
    private final String daomkdir = "dao";
    // 如果你不想生成dao, 将isdao = false;
    private final boolean isdao = true;

    //================================================================================
    public static void main(String[] args) throws Exception {
        new MySqlUtil().start();
    }

    // 非专业人士不要在往下看了,大神请当做没看到这句话
    // ===============================================================================
    public void start() throws Exception {
        // 获取当前文件绝对路径
        // 生成包名
        // 注 : 此格式只适用于idea
        File f = new File(this.getClass().getResource("").getPath());
        String packageName = f.getPath().split("classes\\\\")[1].replace("\\",".");
        String path = f.getPath().replace("target\\classes", "src\\main\\java");

        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, user, password);
        // 获取所有表名
        Statement statement = conn.createStatement();
        ArrayList<String> tables = getTables(conn);

        if (appoint.length > 0) {
            tables.clear();
            for (String s : appoint) {
                tables.add(s);
            }
        }

        for (String tableName : tables) {

            String ClassName = null;
            // 找出主键字段(没有则为null)
            String primarykey = null;
            ResultSet resultSet = statement.executeQuery("select * from " + tableName);
            ResultSet rs = conn.getMetaData().getPrimaryKeys(conn.getCatalog().toUpperCase(),
                    null, tableName.toUpperCase());
            while (rs.next()) {
                primarykey = rs.getString("COLUMN_NAME");
            }

            // 拼接文件内容
            StringBuffer sb = new StringBuffer();
            StringBuffer sbpackage = new StringBuffer();
            sbpackage.append("package " + packageName + ";\r\n\r\n");
            sbpackage.append("import javax.persistence.*;\r\n");
            sbpackage.append("import java.util.Date;\r\n");
            if (islombok) {
                sbpackage.append("import lombok.Data;\r\n");
            }
            sbpackage.append("import org.springframework.format.annotation.DateTimeFormat;\r\n");
            sb.append("\r\n@Entity\r\n");
            sb.append("@Table(name = \"" + tableName + "\")\r\n");
            if (islombok) {
                sb.append("@Data\r\n");
            }
            ClassName = underline2Camel(hand != "" && tableName.startsWith(hand) ? tableName.substring(hand.length() - 1, tableName.length()) : tableName, false);
            sb.append("public class " + ClassName + "{\r\n");
            System.out.println("表 " + tableName + " 生成" + underline2Camel(tableName, false));

            // 获取列名
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                // resultSet数据下标从1开始
                sb.append("\r\n");
                String columnName = metaData.getColumnName(i + 1);
                int type = metaData.getColumnType(i + 1);
                if (columnName.equals(primarykey)) {
                    sb.append("\r\n@Id");
                }
                sb.append("\r\n@Column(name = \"" + columnName + "\")");
                switch (type) {
                    case (Types.INTEGER):
                        System.out.print("Integer类型 " + columnName + "\t");
                        sb.append("\r\nprivate Integer " + underline2Camel(columnName) + ";");
                        break;
                    case (Types.VARCHAR):
                        System.out.print("String类型 " + columnName + "\t");
                        sb.append("\r\nprivate String " + underline2Camel(columnName) + ";");
                        break;
                    case (Types.DATE):
                        System.out.print("Date类型 " + columnName + "\t");
                        sb.append("\r\n@DateTimeFormat(pattern = \"" + pattern + "\")");
                        sb.append("\r\nprivate Date " + underline2Camel(columnName) + ";");
                        break;
                    case (Types.TIMESTAMP):
                        System.out.print("DateTime类型 " + columnName + "\t");
                        sb.append("\r\n@DateTimeFormat(pattern = \"" + pattern + "\")");
                        sb.append("\r\nprivate Date " + underline2Camel(columnName) + ";");
                        break;
                    case (Types.DOUBLE):
                        System.out.print("double类型 " + columnName + "\t");
                        sb.append("\r\nprivate Double " + underline2Camel(columnName) + ";");
                        break;
                    case (Types.FLOAT):
                        System.out.print("float类型 " + columnName + "\t");
                        sb.append("\r\nprivate Float " + underline2Camel(columnName) + ";");
                        break;
                    case (Types.LONGVARCHAR):
                        System.out.print("Long类型 " + columnName + "\t");
                        System.out.print(type);
                        sb.append("\r\nprivate Long " + underline2Camel(columnName) + ";");
                        break;
                    default:
                        System.err.print("未知 类型 " + columnName + "\t");
                        System.out.print(type);
                        sb.append("\r\nprivate Object " + underline2Camel(columnName) + ";");
                        break;
                }
            }

            sb.append("}");
            // 生成文件
            // 如果路径中有空格,idea会转成%,所以需要转回来
            path = path.replace("%", " ");
            File file = new File(path, ClassName + ".java");
            System.out.println("\n生成文件\t" + path + "/" + ClassName + ".java");
            if (file.exists()) {
                file.delete();
            }
            getTitle(sbpackage, tableName);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(sbpackage.toString().getBytes());
            outputStream.write(sb.toString().getBytes());
            outputStream.close();

            // 生成dao
            if (isdao) {
                File file2 = new File(path + "/" + daomkdir, ClassName + "Mapper.java");
                file2.mkdirs();
                System.out.println("\n生成文件\t" + path + "/" + ClassName + "Mapper.java");
                if (file2.exists()) {
                    file2.delete();
                }
                StringBuffer sbdao = new StringBuffer();
                sbdao.append("package " + packageName + "." + daomkdir + ";\r\n\r\n");
                sbdao.append("import tk.mybatis.mapper.common.Mapper;\n");
                sbdao.append("import " + packageName +"." + ClassName + ";\n");
                sbdao.append("@org.apache.ibatis.annotations.Mapper\n");
                sbdao.append("public interface " + ClassName + "Mapper extends Mapper<" + ClassName + "> {}\n");
                FileOutputStream outputStream2 = new FileOutputStream(file2);
                outputStream2.write(sbdao.toString().getBytes());
                outputStream2.close();
            }
        }
    }

    // 获取所有表名
    private static ArrayList<String> getTables(Connection conn) throws SQLException {
        DatabaseMetaData dbMetData = conn.getMetaData();
        ResultSet rs = dbMetData.getTables(null, convertDatabaseCharsetType("root", "mysql"), null, new String[] { "TABLE", "VIEW" });
        ArrayList<String> tableNames = new ArrayList<String>();
        while (rs.next()) {
            if (rs.getString(4) != null
                    && (rs.getString(4).equalsIgnoreCase("TABLE") || rs
                    .getString(4).equalsIgnoreCase("VIEW"))) {
                String tableName = rs.getString(3).toLowerCase();
                tableNames.add(tableName);
            }
        }
        return tableNames;
    }

    public static String convertDatabaseCharsetType(String in, String type) {
        String dbUser;
        if (in != null) {
            if (type.equals("oracle")) {
                dbUser = in.toUpperCase();
            } else if (type.equals("postgresql")) {
                dbUser = "public";
            } else if (type.equals("mysql")) {
                dbUser = null;
            } else if (type.equals("mssqlserver")) {
                dbUser = null;
            } else if (type.equals("db2")) {
                dbUser = in.toUpperCase();
            } else {
                dbUser = in;
            }
        } else {
            dbUser = "public";
        }
        return dbUser;
    }

    public void getTitle(StringBuffer sbpackage, String className) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        sbpackage.append("\r\n/**\r\n");
        sbpackage.append("*\r\n");
        sbpackage.append("* 标题: " + className + "<br/>\r\n");
        sbpackage.append("* 作成信息: DATE: " + format.format(new Date())
                + " NAME: 黄明\r\n");
        sbpackage.append("*\r\n");
        sbpackage.append("*/\r\n");
    }

    /**
     * 下划线转驼峰法(默认小驼峰)
     *
     * @param line
     *            源字符串
     * @param smallCamel
     *            大小驼峰,是否为小驼峰(驼峰，第一个字符是大写还是小写)
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line, boolean ... smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        //匹配正则表达式
        while (matcher.find()) {
            String word = matcher.group();
            //当是true 或则是空的情况
            if((smallCamel.length ==0 || smallCamel[0] ) && matcher.start()==0){
                sb.append(Character.toLowerCase(word.charAt(0)));
            }else{
                sb.append(Character.toUpperCase(word.charAt(0)));
            }

            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

}

