package com.wang.one.mapper;

import static org.junit.Assert.*;

import com.wang.one.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ZM.Wang
 * @date 2018/9/29 15:41
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserMapperTest {

  @Autowired
  private UserMapper userMapper;

  @Test
  public void insertTest(){
    User user = new User();
    user.setUserErp("11111");
    userMapper.insert(user);
//    System.out.println(user);
  }

  @Test
  public void queryTest(){
    userMapper.selectByPrimaryKey(11L);
  }

}