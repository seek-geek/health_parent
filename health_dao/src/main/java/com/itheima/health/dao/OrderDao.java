package com.itheima.health.dao;

import com.itheima.health.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/29
 */
public interface OrderDao {
    /**
     * 添加订单
     * @param order
     */
    void add(Order order);

    /**
     * 条件查询
     * @param order
     * @return
     */
    List<Order> findByCondition(Order order);

    /**
     * 获取订单详情
     * @param id
     * @return
     */
    Map findById4Detail(Integer id);

    Integer findOrderCountByDate(String date);
    Integer findOrderCountAfterDate(String date);
    Integer findVisitsCountByDate(String date);
    Integer findVisitsCountAfterDate(String date);

    /**
     * 获取热门套餐
     * @return
     */
    List<Map> findHotSetmeal();
}
