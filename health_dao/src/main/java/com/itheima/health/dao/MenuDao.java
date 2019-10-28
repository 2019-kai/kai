package com.itheima.health.dao;

import com.itheima.health.pojo.Menu;

import java.util.LinkedHashSet;
import java.util.List;

public interface MenuDao {

    LinkedHashSet<Menu> findMenuByRoleId(Integer RoleId);

    List<Menu> findChildrenByPriority(Integer id);

}
