package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {

    @Select("select count(*) from t_ordersetting where orderDate=#{orderDate}")
    Long findCountOrderSettingByOrderDate(Date orderDate);

    void editOrderSettingByOrderDate(OrderSetting orderSetting);

    @Insert("insert into t_ordersetting (orderDate, number, reservations) values (#{orderDate}, #{number}, #{reservations})")
    void add(OrderSetting orderSetting);

    @Select("select * from t_ordersetting where orderDate between #{begin} and #{after}")
    List<OrderSetting> findOrderSettingByOrderDate(@Param("begin") String begin, @Param("after") String after);

    @Select("select * from t_ordersetting where orderDate=#{orderDate}")
    OrderSetting findOrderSettingByDate(Date orderDate);

    @Delete("DELETE FROM t_ordersetting WHERE orderDate <= #{date}")
    void clearOrderSetting(Date date);
}
