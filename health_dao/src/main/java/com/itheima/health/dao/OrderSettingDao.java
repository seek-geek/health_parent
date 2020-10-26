package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;

public interface OrderSettingDao {

    OrderSetting findOrderSetting(Date date);

    void updateNumber(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);
}