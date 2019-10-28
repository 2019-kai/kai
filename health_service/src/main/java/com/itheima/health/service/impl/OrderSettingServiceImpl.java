package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if (orderSettingList != null && orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                editNumberByOrderDate(orderSetting);
            }
        }
    }

    @Override
    public List<Map<String, Object>> findOrderSettingByOrderDate(String date) {
        String begin = date + "-01";
        String after = date + "-31";
        List<OrderSetting> list = orderSettingDao.findOrderSettingByOrderDate(begin, after);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", orderSetting.getOrderDate().getDate());
            map.put("number", orderSetting.getNumber());
            map.put("reservations", orderSetting.getReservations());
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public void editNumberByOrderDate(OrderSetting orderSetting) {
        Long count = orderSettingDao.findCountOrderSettingByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            orderSettingDao.editOrderSettingByOrderDate(orderSetting);
        } else {
            orderSettingDao.add(orderSetting);
        }
    }

    @Override
    public void clearOrderSetting(Date date) {
        orderSettingDao.clearOrderSetting(date);
    }
}
