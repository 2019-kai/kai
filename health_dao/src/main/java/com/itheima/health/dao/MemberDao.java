package com.itheima.health.dao;

import com.itheima.health.pojo.Member;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;
import java.util.Map;

public interface MemberDao {

    @Select("select * from t_member where phoneNumber=#{phoneNumber}")
    Member findMemberByPhoneNumber(String telephone);

    void add(Member member);

    @Select("select count(*) from t_member where regTime <= #{regTime}")
    Integer findCountByRegTime(String regTime);

    @Select("select count(*) from t_member where regTime = #{todayTime}")
    Integer findCountByDate(String todayTime);

    @Select("select count(*) from t_member")
    Integer findCount();

    @Select("select count(*) from t_member where regTime >= #{firstDay}")
    Integer findThisNewMember(String firstDay);

    @Select("SELECT IF(sex='1','男','女') name, COUNT(*) value FROM t_member GROUP BY sex")
    List<Map<String, Object>> getSexReport();

    @Select("SELECT (" +
            "CASE " +
            "WHEN a.age < 18 THEN '0-18岁' " +
            "WHEN a.age < 30 THEN '18-30岁' " +
            "WHEN a.age < 45 THEN '30-45岁' " +
            "ELSE '45岁以上' END) AS name, COUNT(*) value " +
            "FROM (SELECT TIMESTAMPDIFF(YEAR, birthday, CURDATE()) AS age " +
            "FROM t_member ) a GROUP BY NAME")
    List<Map<String, Object>> getAgeReport();
}
