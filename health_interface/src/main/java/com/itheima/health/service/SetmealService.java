package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {

    PageResult findPage(QueryPageBean queryPageBean);

    void add(Setmeal setmeal, Integer[] checkGroupIds);

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupIdBySetmealId(Integer id);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    void delete(Setmeal setmeal);

    List<Setmeal> findAll();

    Setmeal findSetmealById(Integer id);

    List<Map<String, Object>> getSetmealReport();

    List<Map<String, Object>> findHotSetmeal();
}
