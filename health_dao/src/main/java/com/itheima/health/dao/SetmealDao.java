package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    Page<Setmeal> findPage(String queryString);


    @Insert("insert into t_setmeal (name,code,helpCode,sex,age,price,remark,attention,img) values (#{name},#{code},#{helpCode},#{sex},#{age},#{price},#{remark},#{attention},#{img})")
    @SelectKey(
            statement = "select last_insert_id()",
            keyProperty = "id",
            before = false,
            resultType = Integer.class)
    void add(Setmeal setmeal);

    @Insert("insert into t_setmeal_checkgroup (setmeal_id, checkgroup_id) values (#{setmealId}, #{checkGroupId})")
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkGroupId") Integer checkGroupId);

    @Select("select * from t_setmeal where id=#{id}")
    Setmeal findById(Integer id);

    @Select("select checkgroup_id from t_setmeal_checkgroup where setmeal_id=#{setmealId}")
    List<Integer> findCheckGroupIdBySetmealId(Integer id);

    void edit(Setmeal setmeal);

    @Delete("delete from t_setmeal_checkgroup where setmeal_id=#{setmealId}")
    void deleteSetmealAndCheckGroupBySetmealId(Integer id);

    @Select("select count(*) from t_setmeal_checkgroup where setmeal_id=#{setmealId}")
    Long findSetmealAndCheckGroupBySetmealId(Integer id);

    @Delete("delete from t_setmeal where id=#{id}")
    void delete(Integer id);

    //@Select("select * from t_setmeal")
    List<Setmeal> findAll();

    Setmeal findSetmealById(Integer id);

    @Select("SELECT COUNT(*) value, s.name FROM t_order o, t_setmeal s WHERE o.setmeal_id = s.id GROUP BY s.name")
    List<Map<String, Object>> getSetmealReport();

    @Select("SELECT COUNT(*) setmeal_count, s.name, COUNT(*)/(SELECT COUNT(*) FROM t_order) proportion FROM t_order o, t_setmeal s WHERE o.setmeal_id = s.id GROUP BY s.name LIMIT 0,4")
    List<Map<String, Object>> findHotSetmeal();
}
