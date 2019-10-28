package com.itheima.health.dao;

import com.itheima.health.pojo.Order;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    List<Order> findListByOrder(Order order);

    void add(Order order);

    @Select("SELECT m.name member,s.name setmeal, o.orderDate, o.orderType FROM t_order o, t_member m, t_setmeal s WHERE o.member_id = m.id AND o.setmeal_id = s.id AND o.id = #{id}")
    Map<String, Object> findById(Integer id);

    @Select("select count(*) from t_order where orderDate = #{today}")
    Integer findCountByDate(String today);

    @Select("select count(*) from t_order where orderDate = #{today} and orderStatus = '已到诊'")
    Integer findVisitCountByDate(String today);

    @Select("select count(*) from t_order where orderDate between #{begin} and #{end}")
    Integer findThisDateOrderNumber(Map<String, String> map);

    @Select("select count(*) from t_order where orderDate between #{begin} and #{end} and orderStatus = '已到诊'")
    Integer findThisDateVisitsNumber(Map<String, String> map);
}
