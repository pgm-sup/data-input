package com.wyc.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author haima
 */
public interface TableMapper {


    /**
     * 创建表
     * @param tableName 表名
     * @param params 表字段集合
     * @return 是否创建成功
     */
    int createTmpTable(@Param("tableName") String tableName, @Param("params") List<String> params);

    /**
     * 插入数据
     * @param params 数据
     * @param tableName 表名
     * @return
     */
    int insertData(@Param("params")Map<String, Object> params, @Param("tableName")String tableName);
}
