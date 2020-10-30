package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;
    @Override
    public Order submit(Map<String, String> orderInfo) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-mm-yy");
        String orderDateStr = orderInfo.get("orderDate");
        Date orderDate = null;
        try {
            orderDate = simpleDateFormat.parse(orderDateStr);
        } catch (Exception e) {
            throw new MyException("预约日期格式不正确");
        }
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        if (orderSetting == null){
            throw new MyException("所选日期不能预约，选择其他日期");
        }
        if (orderSetting.getReservations() >= orderSetting.getNumber()){
            throw new MyException("所选日期预约已满，请选择其他日期");
        }
        String telephone = orderInfo.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        Order order = new Order();
        order.setSetmealId(Integer.valueOf(orderInfo.get("setmealId")));
        order.setOrderDate(orderDate);
        if (member != null){
            order.setMemberId(member.getId());
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0){
                throw new MyException("所选日期已经预约过了,不能重复预约");
            }
        }else {
            member = new Member();
            member.setName(orderInfo.get("name"));
            member.setSex(orderInfo.get("sex"));
            String idCard = orderInfo.get("idCard");
            member.setIdCard(idCard);
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            member.setPassword(idCard.substring(idCard.length()-6));
            member.setRemark("微信公众号注册");
            memberDao.add(member);
            order.setMemberId(member.getId());
        }
        order.setOrderType(orderInfo.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        orderDao.add(order);
        int updateRowCount = orderSettingDao.editReservationsByOrderDate(orderSetting);
        if (updateRowCount == 0){
            throw new MyException("所选日期，预约已满，请选择其他日期");
        }
        return order;
    }

    @Override
    public Map<String, String> findById(int id) {
        return orderDao.findById4Detail(id);
    }
}
