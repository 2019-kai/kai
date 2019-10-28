package com.itheima.health.service;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    void add(List<OrderSetting> orderSettingList);

    List<Map<String, Object>> findOrderSettingByOrderDate(String date);

    void editNumberByOrderDate(OrderSetting orderSetting);

    void clearOrderSetting(Date date);
}
