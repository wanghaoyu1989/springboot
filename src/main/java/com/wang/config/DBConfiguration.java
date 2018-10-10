package com.wang.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.LogFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author ZM.Wang
 * @date 2018/8/31 10:24
 */
@Configuration
public class DBConfiguration {

  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Value("${spring.datasource.driver-class-name}")
  private String driverClassName;

  @Value("${spring.datasource.initialSize}")
  private int initialSize;

  @Value("${spring.datasource.minIdle}")
  private int minIdle;

  @Value("${spring.datasource.maxActive}")
  private int maxActive;

  @Value("${spring.datasource.maxWait}")
  private int maxWait;

  @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
  private int timeBetweenEvictionRunsMillis;

  @Value("${spring.datasource.minEvictableIdleTimeMillis}")
  private int minEvictableIdleTimeMillis;

  @Value("${spring.datasource.validationQuery}")
  private String validationQuery;

  @Value("${spring.datasource.testWhileIdle}")
  private boolean testWhileIdle;

  @Value("${spring.datasource.testOnBorrow}")
  private boolean testOnBorrow;

  @Value("${spring.datasource.testOnReturn}")
  private boolean testOnReturn;

  @Value("${spring.datasource.poolPreparedStatements}")
  private boolean poolPreparedStatements;

  @Value("${spring.datasource.filters}")
  private String filters;

  @Value("${druid.username}")
  private String druidUserName;

  @Value("${druid.password}")
  private String druidPassword;

  @Bean("defaultDataSource")
  @Primary
  // 在同样的DataSource中，首先使用被标注的DataSource
  public DataSource defaultDataSource() {
    return getDruidDataSource(driverClassName, username, password, url);
  }

  private DruidDataSource getDruidDataSource(
      String driverClassName, String username, String password, String url) {
    DruidDataSource datasource = new DruidDataSource();
    datasource.setDriverClassName(driverClassName);
    datasource.setUrl(url);
    datasource.setUsername(username);
    datasource.setPassword(password);
    datasource.setInitialSize(initialSize);
    datasource.setMinIdle(minIdle);
    datasource.setMaxActive(maxActive);
    datasource.setMaxWait(maxWait);
    datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
    datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    datasource.setValidationQuery(validationQuery);
    datasource.setTestWhileIdle(testWhileIdle);
    datasource.setTestOnBorrow(testOnBorrow);
    datasource.setTestOnReturn(testOnReturn);
    datasource.setPoolPreparedStatements(poolPreparedStatements);
    try {
      datasource.setFilters(filters);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    Slf4jLogFilter logFilter = new Slf4jLogFilter();
    //是否显示仅仅是占位符的sql语句
    logFilter.setStatementLogEnabled(false);
    // 是否显示SQL语句
    logFilter.setStatementExecutableSqlLogEnable(true);
    //是否显示结果集
    logFilter.setResultSetLogEnabled(true);
    List<Filter> list = new ArrayList<>();
    list.add(logFilter);
    datasource.setProxyFilters(list);

    return datasource;
  }


  @Bean
  public ServletRegistrationBean druidServlet() {
    ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
    servletRegistrationBean.setServlet(new StatViewServlet());
    servletRegistrationBean.addUrlMappings("/druid/*");
    Map<String, String> initParameters = new HashMap<String, String>();
    // 用户名
    initParameters.put("loginUsername", this.druidUserName);
    // 密码
    initParameters.put("loginPassword", this.druidPassword);
    // 禁用HTML页面上的“Reset All”功能
    initParameters.put("resetEnable", "false");
    // IP白名单 (没有配置或者为空，则允许所有访问)
    initParameters.put("allow", "127.0.0.1");
    // IP黑名单
    // initParameters.put("deny", "192.168.20.38");
    // (存在共同时，deny优先于allow)
    servletRegistrationBean.setInitParameters(initParameters);
    return servletRegistrationBean;
  }

  /**
   * @Author: Mr.Wang
   * @Date: 2018/4/26 18:05
   * @Description:
   * @Param: []
   * @return: org.springframework.boot.web.servlet.FilterRegistrationBean
   */
  @Bean
  public FilterRegistrationBean filterRegistrationBean() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    filterRegistrationBean.setFilter(new WebStatFilter());
    filterRegistrationBean.addUrlPatterns("/*");
    filterRegistrationBean
        .addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
    return filterRegistrationBean;
  }

  // 按照BeanId来拦截配置 用来bean的监控
  @Bean(value = "druid-stat-interceptor")
  public DruidStatInterceptor DruidStatInterceptor() {
    DruidStatInterceptor druidStatInterceptor = new DruidStatInterceptor();
    return druidStatInterceptor;
  }

  @Bean
  public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
    BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
    beanNameAutoProxyCreator.setProxyTargetClass(true);
    // 设置要监控的bean的id
    //beanNameAutoProxyCreator.setBeanNames("sysRoleMapper","loginController");
    beanNameAutoProxyCreator.setInterceptorNames("druid-stat-interceptor");
    return beanNameAutoProxyCreator;
  }

}
