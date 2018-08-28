package com.coconason.dtf.demo;

import com.coconason.dtf.client.core.dbconnection.DTFDataSourceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import javax.sql.DataSource;
import org.springframework.core.env.Environment;
import com.alibaba.druid.pool.DruidDataSource;

@EnableAutoConfiguration
@SpringBootApplication
public class SpringDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDemoApplication.class, args);
	}

//	@Autowired
//	private Environment env;
//
//	@Bean
//	public DataSource dataSource() {
//		DruidDataSource dataSource = new DruidDataSource();
//		dataSource.setUrl(env.getProperty("spring.datasource.url"));
//		dataSource.setUsername(env.getProperty("spring.datasource.username"));//用户名
//		dataSource.setPassword(env.getProperty("spring.datasource.password"));//密码
//		dataSource.setInitialSize(2);
//		dataSource.setMaxActive(20);
//		dataSource.setMinIdle(0);
//		dataSource.setMaxWait(60000);
//		dataSource.setValidationQuery("SELECT 1");
//		dataSource.setTestOnBorrow(false);
//		dataSource.setTestWhileIdle(true);
//		dataSource.setPoolPreparedStatements(false);
//		DTFDataSourceProxy dataSourceProxy = new DTFDataSourceProxy();
//		dataSourceProxy.setDataSource(dataSource);
//		return dataSourceProxy;
//	}
}
