package com.itheima.health.service;

import com.itheima.health.entity.Result;

import java.util.Map;

public interface OrderService {

    Result submit(Map<String, Object> orderInfo) throws Exception;

    Map<String, Object> findById(Integer id);
}
