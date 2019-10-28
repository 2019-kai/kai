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
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private OrderSettingDao orderSettingDao;


    @Override
    public Map<String, Object> getBusinessReportData() {
        Map<String, Object> map = new HashMap<>();
        try{
            //当前时间
            String today = DateUtils.parseDate2String(DateUtils.getToday());
            //本周第一天
            String thisWeekFirstDay = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            //本周最后一天
            String thisWeekLastDay = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
            //本月第一天
            String thisMonthFirstDay = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
            //本月最后一天
            String thisMonthLastDay = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());
            //今日新增会员数
            Integer todayNewMember = memberDao.findCountByDate(today);
            //总会员数
            Integer totalMember = memberDao.findCount();
            //本周新增会员数
            Integer thisWeekNewMember = memberDao.findThisNewMember(thisWeekFirstDay);
            //本月新增会员数
            Integer thisMonthNewMember = memberDao.findThisNewMember(thisMonthFirstDay);

            //今日预约数
            Integer todayOrderNumber = orderDao.findCountByDate(today);
            //今日到诊数
            Integer todayVisitsNumber = orderDao.findVisitCountByDate(today);
            //本周预约数
            Map<String, String> dateMap = new HashMap<>();
            dateMap.put("begin", thisWeekFirstDay);
            dateMap.put("end", thisWeekLastDay);
            Integer thisWeekOrderNumber = orderDao.findThisDateOrderNumber(dateMap);
            //本周到诊数
            Integer thisWeekVisitsNumber = orderDao.findThisDateVisitsNumber(dateMap);
            //本月预约数
            dateMap.put("begin", thisMonthFirstDay);
            dateMap.put("end", thisMonthLastDay);
            Integer thisMonthOrderNumber = orderDao.findThisDateOrderNumber(dateMap);
            //本月到诊数
            Integer thisMonthVisitsNumber = orderDao.findThisDateVisitsNumber(dateMap);

            //热门套餐
            List<Map<String, Object>> hotList = setmealService.findHotSetmeal();

            map.put("reportDate",today);
            map.put("todayNewMember",todayNewMember);
            map.put("totalMember",totalMember);
            map.put("thisWeekNewMember",thisWeekNewMember);
            map.put("thisMonthNewMember",thisMonthNewMember);
            map.put("todayOrderNumber",todayOrderNumber);
            map.put("todayVisitsNumber",todayVisitsNumber);
            map.put("thisWeekOrderNumber",thisWeekOrderNumber);
            map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
            map.put("thisMonthOrderNumber",thisMonthOrderNumber);
            map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
            map.put("hotSetmeal",hotList);

        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("出现异常");
        }
        return map;
    }
}
