package com.itheima.health.controller;

import com.itheima.health.Utils.SMSUtils;
import com.itheima.health.Utils.ValidateCodeUtils;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    private static final Logger log = LoggerFactory.getLogger(ValidateCodeController.class);

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/send4Order")
    public Result send4ofOrder(String phoneNumber) {
        String key = RedisMessageConstant.SENDTYPE_GETPWD + "_" + phoneNumber;
        Jedis jedis = jedisPool.getResource();
        String phoneCode = jedis.get(key);
        if (StringUtils.isEmpty(phoneCode)) {
            phoneCode = String.valueOf(ValidateCodeUtils.generateValidateCode(6));
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, phoneNumber, phoneCode);
            jedis.setex(key, 60 * 5, phoneCode);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } else {
            return new Result(false, "验证码已经发送过了，请注意查收");
        }
    }

    @PostMapping("/send4Login")
    public Result send4Login(String phoneNumber) {
        String key = RedisMessageConstant.SENDTYPE_GETPWD + "_" + phoneNumber;
        Jedis jedis = jedisPool.getResource();
        String phoneCode = jedis.get(key);
        if (StringUtils.isEmpty(phoneCode)) {
            phoneCode = String.valueOf(ValidateCodeUtils.generateValidateCode(6));
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, phoneNumber, phoneCode);
            jedis.setex(key, 60 * 10, phoneCode);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } else {
            return new Result(false, "验证码已经发送过了，请注意查收");
        }
    }
}
