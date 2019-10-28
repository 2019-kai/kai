package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Permission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface PermissionDao {

    Set<Permission> findPermissionByRoleId(Integer RoleId);

    @Insert("insert into t_permission (name, keyword, description) values (#{name},#{keyword},#{description}")
    void add(Permission permission);

    Page<Permission> findPage(String queryString);

    @Select("select * from t_permission where id=#{id}")
    Permission findById(Integer id);

    void edit(Permission permission);

    @Delete("delete from t_permission where id=#{id}")
    void deleteById(Integer id);

    @Select("select count(*) from t_role_permission where permission_id=#{permissionId}")
    Long findRoleAndPermissionByPermissionId(Integer id);
}
