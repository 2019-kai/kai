package com.itheima;

import com.itheima.health.dao.RoleDao;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ContextConfiguration(locations = "classpath:applicationContext-dao.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisTest {

    /*@Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test01(){
        Set keys = redisTemplate.keys("*");
        redisTemplate.opsForValue().set("aaa", new Result(false,""));
    }*/

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;



    @Test
    public void findUserByUsername() {
        User admin = userDao.findUserByUsername("admin");
        Set<Role> roles = admin.getRoles();
        for (Role role : roles) {
            System.out.println(role.getKeyword());
        }
        //System.out.println(admin.getUsername());

        /*List<Role> roles = roleDao.findRoleByUserId(1);
        for (Role role : roles) {
            System.out.println(role.getPermissions());
        }*/

    }

    @Test
    public void test(){
        Set set1 = new HashSet();
        Set set2 = new HashSet();

        Collections.addAll(set1, 1,2,3,4);
        Collections.addAll(set2, 2,6,7,3);
        System.out.println(set1);
        System.out.println(set2);
        set1.addAll(set2);
        System.out.println(set1);
        System.out.println(set2);
    }
}
