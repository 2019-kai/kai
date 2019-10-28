package com.itheima.health.service;

import com.itheima.health.pojo.Menu;

import java.util.LinkedHashSet;
import java.util.List;

public interface MenuService {

    LinkedHashSet<Menu> findMenuByRoleId(Integer id);

    List<Menu> findChildrenByPriority(Integer id);
}
