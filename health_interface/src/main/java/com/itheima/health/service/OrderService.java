package com.itheima.health.service;

import com.itheima.health.pojo.Order;

import java.util.Map;

public interface OrderService {
    Order submit(Map<String, String> orderInfo);

    Map<String, String> findById(int id);
}
