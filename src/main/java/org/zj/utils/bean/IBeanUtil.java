package org.zj.utils.bean;

/**
 * @BelongsProject: utils
 * @BelongsPackage: org.zj.utils.bean
 * @Author: ZhangJun
 * @CreateTime: 2018/12/8
 * @Description: ${Description}
 */
public interface IBeanUtil {
    /**
     * 为指定类生成json格式的toString方法
     * @param className
     */
    void appendJsonToString(Class className);
}
