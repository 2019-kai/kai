package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<CheckGroup> page = checkGroupDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        addCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
    }

    @Override
    public List<Integer> findCheckItemIdByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdByCheckGroupId(id);
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.edit(checkGroup);

        checkGroupDao.deleteCheckGroupAndCheckItemByCheckGroupId(checkGroup.getId());

        addCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
    }

    @Override
    public void delete(Integer id) {
        Long count = checkGroupDao.findCheckGroupAndCheckItemByCheckGroupId(id);
        if (count > 0) {
            throw new RuntimeException("该检查组中有检查项,不能删除!");
        } else {
            Long count1 = checkGroupDao.findSetmealCheckGroupByCheckGroupId(id);
            if (count1 > 0) {
                throw new RuntimeException("有套餐中有该检查组,不能删除!");
            } else {
                checkGroupDao.delete(id);
            }
        }
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    private void addCheckGroupAndCheckItem(Integer id, Integer[] checkItemIds) {
        for (Integer checkItemId : checkItemIds) {
            checkGroupDao.addCheckGroupCheckItem(id, checkItemId);
        }
    }
}
