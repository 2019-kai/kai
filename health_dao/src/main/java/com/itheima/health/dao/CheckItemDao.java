package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CheckItemDao {

    @Insert("insert into t_checkitem (code,name,sex,age,price,type,remark,attention) values (#{code},#{name},#{sex},#{age},#{price},#{type},#{remark},#{attention})")
    void add(CheckItem checkItem);

    Page<CheckItem> findPage(String queryString);

    @Select("select * from t_checkitem where id=#{id}")
    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    @Select("select count(*) from t_checkgroup_checkitem where checkitem_id=#{id}")
    Long findCheckGroupCheckItemByCheckItemId(Integer id);

    @Delete("delete from t_checkitem where id=#{id}")
    void deleteById(Integer id);

    @Select("select * from t_checkitem")
    List<CheckItem> findAll();

    @Select("SELECT * FROM t_checkitem ci, t_checkgroup_checkitem cgci WHERE ci.id = cgci.checkitem_id AND cgci.checkgroup_id=#{checkGroupId}")
    List<CheckItem> findCheckItemByCheckGroupId(Integer id);
}
