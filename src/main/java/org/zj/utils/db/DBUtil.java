package org.zj.utils.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import javax.persistence.Column;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @BelongsProject: utils
 * @BelongsPackage: org.zj.utils
 * @Author: 张君
 * @CreateTime: 2018/12/8
 * @Description: 操作数据库的工具
 */
public class DBUtil implements IDbUtil {

    private DruidDataSource druidDataSource;


    //加载配置文件，获得链接
    public DBUtil(String propertiesPath, String usernameName, String passwordName, String urlName, String driverClassName) {
        Properties properties = new Properties();
        try {
            System.out.println("加载properties");
            properties.load(new FileInputStream(propertiesPath));
            System.out.println("properties加载完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("初始化连接池");
        druidDataSource=new DruidDataSource();
        druidDataSource.setUsername(properties.getProperty(usernameName));
        druidDataSource.setPassword(properties.getProperty(passwordName));
        druidDataSource.setUrl(properties.getProperty(urlName));
        druidDataSource.setDriverClassName(properties.getProperty(driverClassName));
        System.out.println("连接池初始化成功");
    }


    public <T> List<T> getResult(String sql, Class c) {
        List<T> result = new ArrayList<T>();
        ResultSet resultSet = getResultSet(sql);
        inflateToResult(resultSet, result, c);
        return result;
    }

    /**
     * 直接执行sql
     *
     * @param sql
     * @return
     */
    public boolean runSql(String sql) {
        try {
            return getStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解析ResultSet，然后把结果通过反射封装到list
     *
     * @param resultSet
     * @param result
     * @param c
     * @param <T>
     */
    private <T> void inflateToResult(ResultSet resultSet, List<T> result, Class c) {
        try {
            while (resultSet.next()) {
                result.add((T) inflateBean(resultSet, c));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析resultset并且通过反射弄出来对象
     *
     * @param resultSet
     * @param c
     * @param <T>
     * @return
     */
    private <T> T inflateBean(ResultSet resultSet, Class c) {
        Object o = null;
        //遍历这个类的所有字段
        try {
            o = c.newInstance();
            for (Field f : c.getDeclaredFields()) {
                f.setAccessible(true);
                f.set(o, parseValueFromResultSetAndField(resultSet, f));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) o;
    }

    /**
     * 通过ResultSet和Field来获得值,如果字段上面加了Coul注解，那就通过那个注解的值来获得
     *
     * @param resultSet
     * @param f
     * @return
     */
    private Object parseValueFromResultSetAndField(ResultSet resultSet, Field f) {
        String colName = f.getName();

        if (f.isAnnotationPresent(Column.class)) {
            colName = f.getAnnotation(Column.class).name();
        }

        try {
            if (f.getType() == int.class || f.getType() == Integer.class) {
                return resultSet.getInt(colName);
            }
            if (f.getType() == String.class) {
                return resultSet.getString(colName);
            }
            if (f.getType() == Double.class || f.getType() == double.class) {
                return resultSet.getDouble(colName);
            }
            if (f.getType() == Date.class) {
                return resultSet.getDate(colName);
            }
            if (f.getType() == Float.class || f.getType() == float.class) {
                return resultSet.getFloat(colName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("类型为判断");
        return null;
    }

    //获得结果
    private ResultSet getResultSet(String sql) {
        try {
            return getStatement().executeQuery(sql);
        } catch (Exception e) {
            return null;
        }
    }

    //获得statement
    private Statement getStatement() {
        try {
            DruidPooledConnection connection = druidDataSource.getConnection();
            return connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
