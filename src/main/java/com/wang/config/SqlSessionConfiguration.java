package com.wang.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @author ZM.Wang
 * @date 2018/8/31 12:10
 */
@Configuration
@MapperScan(basePackages = {"com.wang.**.mapper"})
public class SqlSessionConfiguration {

  @Bean(name = "defaultSqlSessionFactory")
  @Primary
  public SqlSessionFactory defaultSqlSessionFactory(
      @Qualifier("defaultDataSource") DataSource dataSource) throws Exception {
    SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
    bean.setDataSource(dataSource);
    bean.setMapperLocations(
        new PathMatchingResourcePatternResolver().getResources("classpath*:mappers/**/*.xml"));
    return bean.getObject();
  }
}
