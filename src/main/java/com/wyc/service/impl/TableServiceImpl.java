package com.wyc.service.impl;

import com.wyc.mapper.TableMapper;
import com.wyc.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author haima
 */
@Service
public class TableServiceImpl implements TableService {

    @Autowired
    TableMapper tableMapper;

    @Override
    public int createTable(String tableName, List<String> headers) {
        return tableMapper.createTmpTable(tableName, headers);
    }

    @Override
    public int saveData(List<Map<String, Object>> list, String tableName) {
        int i = 0;
        for (Map<String, Object> pd : list) {
            i += tableMapper.insertData(pd, tableName);
        }
        return i;
    }
}
