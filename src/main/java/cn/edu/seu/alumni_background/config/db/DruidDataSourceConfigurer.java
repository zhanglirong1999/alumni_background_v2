package cn.edu.seu.alumni_background.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:db/db-config.properties")
public class DruidDataSourceConfigurer {

	@Value("${dev.url}")
	String url;
	@Value("${dev.username}")
	String username;
	@Value("${dev.password}")
	String password;

	@Bean
	public DataSource dataSource() {
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setUrl(url);
		druidDataSource.setUsername(username);
		druidDataSource.setPassword(password);

		druidDataSource.setValidationQuery("select 1");
		druidDataSource.setTestWhileIdle(true);
		druidDataSource.setTestOnBorrow(true);

		druidDataSource.setInitialSize(1);
		druidDataSource.setMinIdle(10);
		druidDataSource.setMaxActive(30);

		return druidDataSource;
	}

}
