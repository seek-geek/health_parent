package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetMealService setmealService;

    @GetMapping("/getMemberReport")
    public Result getMemberReport() {
        List<String> months = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, -1);
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            months.add(sdf.format(calendar.getTime()));
        }
        List<Integer> memberCount = memberService.getMemberReport(months);
        Map<String, Object> data = new HashMap<>();
        data.put("months", months);
        data.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, data);
    }

    @GetMapping("/getSetmealReport")
    public Result getSetmealReport() {
        List<Map<String, Object>> list = setmealService.getSetmealReport();
        List<String> setmealNames = list.stream().map(m -> ((String) m.get("name"))).collect(Collectors.toList());
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("setmealNames", setmealNames);
        dataMap.put("setmealCount", list);
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, dataMap);
    }
}
