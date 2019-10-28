package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Autowired
    private RedisTemplate<String, Setmeal> redisTemplate;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<CheckItem> page = checkItemDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        Long count = checkItemDao.findCheckGroupCheckItemByCheckItemId(checkItem.getId());
        if (count > 0) {
            Set<String> keys = redisTemplate.keys("SETMEAL*");
            redisTemplate.delete(keys);
        }
        checkItemDao.edit(checkItem);
    }

    @Override
    public void delete(Integer id) {
        Long count = checkItemDao.findCheckGroupCheckItemByCheckItemId(id);
        if (count > 0) {
            throw new RuntimeException("有检查组里有这个检查项,不能删除!");
        } else {
            checkItemDao.deleteById(id);
        }
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
