package com.itheima.health.controller;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 用户申请预约
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Autowired
    private JedisPool jedisPool;

    private OrderService orderService;

    /**
     * 提交预约申请
     * @param orderInfo
     * @return
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String> orderInfo){
        String telephone = orderInfo.get("telephone");
        String key = RedisMessageConstant.SENDTYPE_GETPWD + "_" + telephone;
        Jedis jedis = jedisPool.getResource();
        String phoneCode = jedis.get(key);
        if (StringUtils.isEmpty(phoneCode)){
            return new Result(false, "请重新获取验证码");
        }else {
            String validateCode = orderInfo.get("validateCode");
            if (!phoneCode.equals(validateCode)){
                return new Result(false, "验证码错误");
            }
            jedis.del(key);
            orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN);
            Order order = orderService.submit(orderInfo);
            return new Result(true, MessageConstant.ORDER_SUCCESS,order);
        }
    }
    @GetMapping("/findById")
    public Result findById(int id){
        Map<String,String> orderInfo = orderService.findById(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,orderInfo);
    }
}
