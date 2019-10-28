package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public Result submit(Map<String, Object> orderInfo) throws Exception {
        //检查输入的日期能不能体检
        String orderDate = (String) orderInfo.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findOrderSettingByDate(date);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //检查输入日期是否已预约满
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (reservations >= number) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //检查当前用户是否是会员,使用手机号检查
        String telephone = (String) orderInfo.get("telephone");
        Member member = memberDao.findMemberByPhoneNumber(telephone);
        //如果是会员，防止重复预约（一个会员、一个时间、一个套餐不能重复，否则是重复预约
        if (member != null) {
            Integer memberId = member.getId();
            Integer setmealId = Integer.parseInt((String) orderInfo.get("setmealId"));
            Order order = new Order(memberId, date, null, null, setmealId);
            List<Order> orderList = orderDao.findListByOrder(order);
            if (orderList != null && orderList.size() > 0) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            //如果当前用户不是会员,将当前用户加入会员
            member = new Member();
            member.setName((String) orderInfo.get("name"));
            member.setPhoneNumber((String) orderInfo.get("telephone"));
            member.setIdCard((String) orderInfo.get("idCard"));
            member.setSex((String) orderInfo.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        //可以预约,预约人数加一
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editOrderSettingByOrderDate(orderSetting);

        //将预约信息保存到信息表
        Order order = new Order(member.getId(),date,
                (String) orderInfo.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) orderInfo.get("setmealId")));
        orderDao.add(order);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }

    @Override
    public Map<String, Object> findById(Integer id) {
        Map<String, Object> map = orderDao.findById(id);
        if (map != null && map.size() > 0) {
            Date date = (Date) map.get("orderDate");
            try {
                map.put("orderDate", DateUtils.parseDate2String(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
