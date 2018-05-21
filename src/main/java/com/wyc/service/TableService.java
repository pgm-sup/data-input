package com.wyc.service;

import java.util.List;

/**
 * @author haima
 */
public interface TableService {

    /**
     * 创建Excel
     * @param headers
     * @return
     */
    String createTable(String tableName, List<String> headers);
}
