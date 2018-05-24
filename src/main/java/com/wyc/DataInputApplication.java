package com.wyc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author haima
 */
@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.wyc.mapper")
public class DataInputApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataInputApplication.class, args);
	}
}
