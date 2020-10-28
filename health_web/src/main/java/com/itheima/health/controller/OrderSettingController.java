package com.itheima.health.controller;

import com.itheima.health.Utils.POIUtils;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.mysql.jdbc.log.LogFactory;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    private static Logger logger = LoggerFactory.getLogger(OrderSettingController.class);

    @Reference
    private OrderSettingService orderSettingService;


    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile) throws Exception {
        try {
            List<String[]> strings = POIUtils.readExcel(excelFile);
            List<OrderSetting> orderSettings = new ArrayList<>();
            OrderSetting orderSetting = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            for (String[] string : strings) {
                Date date = simpleDateFormat.parse(string[0]);
                orderSetting = new OrderSetting(date, Integer.valueOf(string[1]));
                orderSettings.add(orderSetting);
            }
            orderSettingService.add(orderSettings);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            logger.error("导入预约设置失败",e);
            throw e;
        }

    }

    @GetMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month){
        List<Map<String,Integer>> data = orderSettingService.getOrderSettingByMonth(month);
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,data);
    }
}
