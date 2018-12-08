package org.zj.utils.db;

import java.util.Date;

/**
 * @BelongsProject: utils
 * @BelongsPackage: org.zj.utils
 * @Author: ZhangJun
 * @CreateTime: 2018/12/8
 * @Description: ${Description}
 */
public class Student {
    private String username;
    private String password;
    private Date birthday;
    private Double money;
    private Float rate;

    @Override
    public String toString() {
        return "Student{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                ", money=" + money +
                ", rate=" + rate +
                '}';
    }
}
