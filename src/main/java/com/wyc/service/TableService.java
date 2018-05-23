package com.wyc.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author haima
 */
public interface TableService {

    /**
     * 创建Excel
     * @param headers 表头
     * @return
     */
    int createTable(String tableName, List<String> headers);

    /**
     * 保存数据
     * @param list 数据
     * @param tableName 表名
     * @return
     */
    int saveData(List<Map<String, Object>> list, String tableName);
}
