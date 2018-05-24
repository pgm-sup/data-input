package com.wyc;

import com.wyc.mapper.TableMapper;
import com.wyc.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataInputApplicationTests {

	@Autowired
	private TableMapper tableMapper;

	@Autowired
	private UserMapper userMapper;

	@Test
	public void contextLoads() {
		String username = "admin";
		System.out.println(userMapper.findByUserName(username));
	}

}
