package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

public interface RoleDao {

    Set<Role> findRoleByUserId(Integer userId);

    Page<Role> findPage(String queryString);

    void add(Role role);

    @Select("select permission_id from t_role_permission where role_id=#{roleId}")
    List<Integer> findPermissionIdByRoleId(Integer id);

    @Select("select * from t_role where id=#{id}")
    Role findById(Integer id);

    void edit(Role role);

    @Delete("delete from t_role_permission where role_id=#{roleId}")
    void deleteRoleAndPermissionByRoleId(Integer id);

    @Select("select count(*) from t_role_permission where role_id=#{roleId}")
    Long findRoleAndPermissionByRoleId(Integer id);

    @Select("select count(*) from t_role_menu where role_id=#{roleId}")
    Long findRoleAndMenuByRoleId(Integer id);

    @Delete("delete from t_role where id=#{id}")
    void delete(Integer id);

    @Insert("insert into t_role_permission (role_id, permission_id) values (#{id}, #{roleId})")
    void addRoleAndPermission(Integer id, Integer roleId);
}
