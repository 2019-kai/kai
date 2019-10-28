package com.itheima.health.service;

import com.itheima.health.pojo.Role;

import java.util.Set;

public interface RoleService {

    Set<Role> findRoleByUserId(Integer id);
}
