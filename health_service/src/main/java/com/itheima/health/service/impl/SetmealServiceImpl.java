package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constants.RedisConstant;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private RedisTemplate<String, Setmeal> redisTemplate;


    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Setmeal> page = setmealDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        setmealDao.add(setmeal);

        Set<String> keys = redisTemplate.keys("SETMEAL*");
        redisTemplate.delete(keys);

        if (checkGroupIds != null && checkGroupIds.length > 0) {
            addSetmealAndCheckGroup(setmeal.getId(), checkGroupIds);
        }
        if (setmeal.getImg() != null) {
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        }
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Integer> findCheckGroupIdBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdBySetmealId(id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //通过id查询修改之前的套餐
        Setmeal oldSetmeal = setmealDao.findById(setmeal.getId());

        setmealDao.edit(setmeal);

        Set<String> keys = redisTemplate.keys("SETMEAL*");
        redisTemplate.delete(keys);

        if (setmeal.getImg() != null) {
            if (oldSetmeal.getImg() == null) {
                //将修改之后的套餐图片保存到redis的setmealPicDbResources中
                jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
            } else if (!oldSetmeal.getImg().equals(setmeal.getImg())){

                    //删除redis的setmealPicDbResources中修改套餐修改之前的图片
                    jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, oldSetmeal.getImg());

                    //将修改之后的套餐图片保存到redis的setmealPicDbResources中
                    jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
            }
        }

        //先删除中间表中有关setmealId的数据
        setmealDao.deleteSetmealAndCheckGroupBySetmealId(setmeal.getId());
        //再将修改数据保存到中间表中
        addSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
    }

    @Override
    public void delete(Setmeal setmeal) {
        Long count = setmealDao.findSetmealAndCheckGroupBySetmealId(setmeal.getId());
        if (count > 0) {
            throw new RuntimeException("该套餐中有检测项,不能删除!");
        } else {
            setmealDao.delete(setmeal.getId());

            Set<String> keys = redisTemplate.keys("SETMEAL*");
            redisTemplate.delete(keys);

            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        }
    }

    @Override
    public List<Setmeal> findAll() {
        List<Setmeal> list = new ArrayList<>();
        Set<String> keys = redisTemplate.keys("SETMEAL*");
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                Setmeal setmeal = redisTemplate.opsForValue().get(key);
                list.add(setmeal);
            }
        } else {
            list = setmealDao.findAll();
            for (Setmeal setmeal : list) {
                redisTemplate.opsForValue().set("SETMEAL" + setmeal.getId(), setmeal);
            }
        }
        return list;
    }

    @Override
    public Setmeal findSetmealById(Integer id) {
        Setmeal setmeal = redisTemplate.opsForValue().get("SETMEAL" + id);
        if (setmeal != null) {
            return setmeal;
        }
        Set<String> keys = redisTemplate.keys("SETMEAL*");
        redisTemplate.delete(keys);

        findAll();
        return redisTemplate.opsForValue().get("SETMEAL" + id);
    }

    @Override
    public List<Map<String, Object>> getSetmealReport() {
        return setmealDao.getSetmealReport();
    }

    @Override
    public List<Map<String, Object>> findHotSetmeal() {
        return setmealDao.findHotSetmeal();
    }

    private void addSetmealAndCheckGroup(Integer setmealId, Integer[] checkGroupIds) {
        for (Integer checkGroupId : checkGroupIds) {
            setmealDao.addSetmealCheckGroup(setmealId, checkGroupId);
        }
    }
}
