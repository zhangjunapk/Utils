package org.zj.utils.bean;

import org.zj.utils.clazz.ClassUtil;

import java.io.*;
import java.lang.reflect.Field;

/**
 * @BelongsProject: utils
 * @BelongsPackage: org.zj.utils.bean
 * @Author: ZhangJun
 * @CreateTime: 2018/12/8
 * @Description: ${Description}
 */
public class BeanUtil implements IBeanUtil {

    public void appendJsonToString(Class clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        generate(clazz, stringBuilder);
        writeToJava(clazz, stringBuilder);
    }

    /**
     * 把文字写入到java文件
     *
     * @param clazz
     * @param stringBuilder
     */
    private void writeToJava(Class clazz, StringBuilder stringBuilder) {
        try {
            String classPath = ClassUtil.getJavaPath();
            String rawPath = classPath + clazz.getName().replace(".", "\\") + ".java";
            FileOutputStream fos = new FileOutputStream(rawPath);
            fos.write(stringBuilder.toString().getBytes("UTF-8"));
            fos.flush();
            fos.close();
        }catch (Exception e){

        }
    }

    /**
     * 生成要写入到java文件的字符
     * @param clazz
     * @param sb
     */
    private void generate(Class clazz, StringBuilder sb) {
        String classPath = ClassUtil.getJavaPath();
        String rawPath = classPath + clazz.getName().replace(".", "\\") + ".java";
        System.out.println(rawPath);
        try {
            //BufferedWriter bw=new BufferedWriter(new FileWriter(rawPath));

            BufferedReader br = new BufferedReader(new FileReader(rawPath));

            //我需要获得最后一个右括号，然后删掉他,no 现在不需要了，我要从前面写
            //我还是全部读进来，放到sb中，然后去掉最后一个右括号，再写吧
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            System.out.println(sb);
            sb.deleteCharAt(sb.toString().lastIndexOf("}"));

            sb.append("public String toString(){");

            sb.append(genListSb(clazz));

            sb.append("return \"");
            sb.append(genJsonStr(clazz));
            sb.append("\";");
            sb.append("}");
            System.out.println("---------下面是之后的");
            sb.append("}");
            System.out.println(sb);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成所有list的sb
     *
     * @param c
     * @return
     */
    private String genListSb(Class c) {

        //先生成所有list的遍历方法
        StringBuilder sb = new StringBuilder();
        for (Field f : c.getDeclaredFields()) {
            if (ClassUtil.isList(f)) {
                //那就你懂得
                sb.append("StringBuilder " + f.getName() + "sb=new StringBuilder();");
                sb.append("for(" + ClassUtil.getFieldListClass(f).getName() + " b:" + f.getName() + "){");
                sb.append(f.getName() + "sb.append(b.toString());");
                sb.append("}");
            }
        }
        return sb.toString();
    }

    /**
     * 生成一个java文件的json格式的toString
     *
     * @param c
     * @return
     */
    private String genJsonStr(Class c) {

        System.out.println("开始遍历" + c);
        StringBuilder sb = new StringBuilder("{");
        try {
            for (Field f : c.getDeclaredFields()) {
                f.setAccessible(true);


                //如果字段是数组，那就递归调用来解析
                if (ClassUtil.isList(f) && ClassUtil.getFieldListClass(f) != null) {

                    //获得那个类
                    sb.append(f.getName() + ":[\"+" + f.getName() + "sb.toString()+\"],");
                    continue;
                }
                sb.append(f.getName() + ":\"+\"'\"+" + f.getName() + "+\"',");
            }
            sb.append("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        new BeanUtil().appendJsonToString(Student.class);
    }
}
