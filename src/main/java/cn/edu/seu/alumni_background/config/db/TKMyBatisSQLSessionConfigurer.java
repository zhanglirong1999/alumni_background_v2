package cn.edu.seu.alumni_background.config.db;

import com.github.pagehelper.PageInterceptor;
import java.util.Properties;
import javax.sql.DataSource;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

@Configuration
public class TKMyBatisSQLSessionConfigurer {

	@Bean
	public SqlSessionFactory sqlSessionFactory(
		@Qualifier("dataSource")
			DataSource dataSource
	) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();

		// 配置数据源
		bean.setDataSource(dataSource);

		// 配置实体类对象
		bean.setTypeAliasesPackage("cn.edu.seu.alumni_background.model.dao.entity");

		// xml目录配置
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		bean.setMapperLocations(
			resourcePatternResolver.getResources("classpath:mapper/*.xml")
		);

		//配置实体类属性与表字段的映射
		org.apache.ibatis.session.Configuration configuration =
			new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);  // 驼峰
//		configuration.setLogImpl(StdOutImpl.class);  // 显示 mysql 语句
		bean.setConfiguration(configuration);

		return bean.getObject();
	}

	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer =
			new MapperScannerConfigurer();
		mapperScannerConfigurer.setBasePackage(
			"cn.edu.seu.alumni_background.model.dao.mapper"
		);
		mapperScannerConfigurer.setSqlSessionFactoryBeanName(
			"sqlSessionFactory"
		);
		Properties properties = new Properties();
		properties.setProperty("mappers", "tk.mybatis.mapper.common.Mapper");
		mapperScannerConfigurer.setProperties(properties);
		return mapperScannerConfigurer;
	}
}
