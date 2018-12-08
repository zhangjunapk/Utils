package org.zj.utils.db;

import java.util.List;

/**
 * @BelongsProject: utils
 * @BelongsPackage: org.zj.utils
 * @Author: ZhangJun
 * @CreateTime: 2018/12/8
 * @Description: ${Description}
 */
public class Content {
    public static void main(String[] args) {
        DBUtil dbUtil=new DBUtil("D:\\java\\IdeaProjects\\base\\utils\\src\\main\\resources\\application.properties","username","password","url","driver-class-name");

        List<Student> result = dbUtil.getResult("select * from tt", Student.class);
        for(Student s:result){
            System.out.println(s);
        }

    }
}
