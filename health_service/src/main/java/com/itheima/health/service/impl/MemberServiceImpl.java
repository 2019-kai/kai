package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findMemberByPhoneNumber(String telephone) {
        return memberDao.findMemberByPhoneNumber(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public List<Integer> findCountByRegTime(List<String> months) {
        List<Integer> list = new ArrayList<>();
        if (months != null && months.size() > 0) {
            for (String month : months) {
                String regTime = month + "-31";
                Integer count = memberDao.findCountByRegTime(regTime);
                list.add(count);
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getSexReport() {
        return memberDao.getSexReport();
    }

    @Override
    public List<Map<String, Object>> getAgeReport() {
        return memberDao.getAgeReport();
    }
}
