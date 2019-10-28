package com.itheima.health.service;

import com.itheima.health.pojo.Member;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface MemberService {

    Member findMemberByPhoneNumber(String telephone);

    void add(Member member);

    List<Integer> findCountByRegTime(List<String> months);

    List<Map<String, Object>> getSexReport();

    List<Map<String, Object>> getAgeReport();
}
