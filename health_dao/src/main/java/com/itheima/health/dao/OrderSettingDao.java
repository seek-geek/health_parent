package com.itheima.health.dao;

import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    OrderSetting findOrderSetting(Date date);

    void updateNumber(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<Map<String, Integer>> getOrderSettingByMonth(String month);

    OrderSetting findByOrderDate(Date orderDate);

    int editReservationsByOrderDate(OrderSetting orderSetting);
}
