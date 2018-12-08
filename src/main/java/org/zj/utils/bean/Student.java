package org.zj.utils.bean;

import java.util.List;

/**
 * @BelongsProject: utils * @BelongsPackage: org.zj.utils.bean * @Author: ZhangJun * @CreateTime: 2018/12/8 * @Description: ${Description}
 */
public class Student {
    private List<Teacher> teacherList;

    public String toString() {
        StringBuilder teacherListsb = new StringBuilder();
        for (org.zj.utils.bean.Teacher b : teacherList) {
            teacherListsb.append(b.toString());
        }
        return "{teacherList:[" + teacherListsb.toString() + "],}";
    }
}
