package org.zj.utils.db;

import java.util.List;

/**
 * @BelongsProject: utils
 * @BelongsPackage: org.zj.utils
 * @Author: ZhangJun
 * @CreateTime: 2018/12/8
 * @Description: ${Description}
 */
public interface IDbUtil {
    <T> List<T> getResult(String sql, Class c);
    boolean runSql(String sql);
}
