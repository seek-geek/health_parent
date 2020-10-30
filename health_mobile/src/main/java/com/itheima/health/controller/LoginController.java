package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import javafx.scene.media.MediaBuilder;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @PostMapping("/check")
    public Result login(@RequestBody Map<String, String> paramMap, HttpServletResponse res) {
        String telephone = paramMap.get("telephone");
        String key = RedisMessageConstant.SENDTYPE_GETPWD + "_" + telephone;
        Jedis jedis = jedisPool.getResource();
        String phoneCode = jedis.get(key);
        if (StringUtil.isUnicodeString(phoneCode)) {
            return new Result(false, "请发送验证码！");
        }
        String validateCode = paramMap.get("validateCode");
        if (!phoneCode.equals(validateCode)) {
            return new Result(false, "验证码错误");
        }
        jedis.del(key);
        Member member = memberService.findByTelephone(telephone);
        if (member == null) {
            member = new Member();
            member.setRemark("快速登录");
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            memberService.add(member);
        }
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setPath("/");
        res.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
