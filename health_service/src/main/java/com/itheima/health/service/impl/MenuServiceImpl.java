package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public LinkedHashSet<Menu> findMenuByRoleId(Integer RoleId) {
        return menuDao.findMenuByRoleId(RoleId);
    }

    @Override
    public List<Menu> findChildrenByPriority(Integer id) {
        return menuDao.findChildrenByPriority(id);
    }
}
