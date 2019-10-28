package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public Set<Role> findRoleByUserId(Integer id) {
        return roleDao.findRoleByUserId(id);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Role> page = roleDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(Role role, Integer[] roleIds) {
        roleDao.add(role);
        addRoleAndPermission(role.getId(), roleIds);
    }

    @Override
    public List<Integer> findPermissionIdByRoleId(Integer id) {
        return roleDao.findPermissionIdByRoleId(id);
    }

    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    @Override
    public void edit(Role role, Integer[] roleIds) {
        roleDao.edit(role);

        roleDao.deleteRoleAndPermissionByRoleId(role.getId());

        addRoleAndPermission(role.getId(), roleIds);
    }

    @Override
    public void delete(Integer id) {
        Long count = roleDao.findRoleAndPermissionByRoleId(id);
        if (count > 0) {
            throw new RuntimeException("该角色有权限,不能删除!");
        } else {
            Long count1 = roleDao.findRoleAndMenuByRoleId(id);
            if (count1 > 0) {
                throw new RuntimeException("有用户有该角色,不能删除!");
            } else {
                roleDao.delete(id);
            }
        }
    }

    private void addRoleAndPermission(Integer id, Integer[] roleIds) {
        for (Integer roleId : roleIds) {
            roleDao.addRoleAndPermission(id, roleId);
        }
    }
}
