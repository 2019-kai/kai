package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {

    Set<Role> findRoleByUserId(Integer id);

    PageResult findPage(QueryPageBean queryPageBean);

    void add(Role role, Integer[] roleIds);

    List<Integer> findPermissionIdByRoleId(Integer id);

    Role findById(Integer id);

    void edit(Role role, Integer[] roleIds);

    void delete(Integer id);
}
