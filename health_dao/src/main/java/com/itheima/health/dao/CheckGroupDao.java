package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CheckGroupDao {

    //通过查询条件查询分页的数据
    Page<CheckGroup> findPage(String queryString);

    //保存检查组数据
    void add(CheckGroup checkGroup);

    //保存检查组和检查项中间表数据
    @Insert("insert into t_checkgroup_checkitem (checkgroup_id, checkitem_id) values (#{id}, #{checkItemId})")
    void addCheckGroupCheckItem(@Param("id") Integer id, @Param("checkItemId") Integer checkItemId);

    //通过检查组id查检查组中检查项的id
    @Select("select checkitem_id from t_checkgroup_checkitem where checkgroup_id=#{checkGroupId}")
    List<Integer> findCheckItemIdByCheckGroupId(Integer id);

    //通过检查组id查检查组
    @Select("select * from t_checkgroup where id=#{id}")
    CheckGroup findById(Integer id);

    //更新检查组中的数据
    void edit(CheckGroup checkGroup);

    //通过检查组id删除中间表数据
    @Delete("delete from t_checkgroup_checkitem where checkgroup_id=#{checkGroupId}")
    void deleteCheckGroupAndCheckItemByCheckGroupId(Integer id);

    //通过检查组id查中间表中的数据数量
    @Select("select count(*) from t_checkgroup_checkitem where checkgroup_id=#{checkGroupId}")
    Long findCheckGroupAndCheckItemByCheckGroupId(Integer id);

    //通过id删除检查组
    @Delete("delete from t_checkgroup where id=#{id}")
    void delete(Integer id);

    //通过检查组id查询中间表中的数据数量
    @Select("select count(*) from t_setmeal_checkgroup where checkgroup_id=#{checkGroupId}")
    Long findSetmealCheckGroupByCheckGroupId(Integer id);

    @Select("select * from t_checkgroup")
    List<CheckGroup> findAll();

    List<CheckGroup> findCheckGroupBySetmealId(Integer id);
}
