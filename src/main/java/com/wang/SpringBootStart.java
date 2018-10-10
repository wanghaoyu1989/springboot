package com.wang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author ZM.Wang
 * @date 2018/9/28 12:46
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringBootStart extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootStart.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(SpringBootStart.class);
  }
}
