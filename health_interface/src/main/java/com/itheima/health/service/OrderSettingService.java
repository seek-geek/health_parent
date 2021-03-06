package com.itheima.health.service;

import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    void add(List<OrderSetting> orderSetting);

    List<Map<String, Integer>> getOrderSettingByMonth(String month);
}
