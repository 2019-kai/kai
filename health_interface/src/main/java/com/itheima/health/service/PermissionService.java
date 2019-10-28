package com.itheima.health.service;

import com.itheima.health.pojo.Permission;

import java.util.Set;

public interface PermissionService {

    Set<Permission> findPermissionByRoleId(Integer id);
}
