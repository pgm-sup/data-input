package com.wyc.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author haima
 */
@Repository
public interface TableMapper {


    /**
     * 创建表
     * @param tableName 表名
     * @param params 表字段集合
     * @return 是否创建成功
     */
    boolean createTmpTable(@Param("tableName") String tableName, @Param("params") List<String> params);
}
