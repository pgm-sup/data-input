package com.wyc.mapper;

import com.wyc.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author haima
 */
@Mapper
public interface UserMapper {

    public SysUser findByUserName(String username);
}
