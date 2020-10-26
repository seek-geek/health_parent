package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao settingDao;

    @Override
    public void add(List<OrderSetting> orderSettings) {
        for (OrderSetting orderSetting : orderSettings) {
            OrderSetting targetOrderstr = settingDao.findOrderSetting(orderSetting.getOrderDate());

            if (targetOrderstr != null) {
                if (targetOrderstr.getReservations() > orderSetting.getNumber()) {
                    throw new MyException("最大预约数不能小于已已预约数");
                } else {
                    settingDao.updateNumber(orderSetting);
                }
            } else {
                settingDao.add(orderSetting);
            }
        }
    }
}
