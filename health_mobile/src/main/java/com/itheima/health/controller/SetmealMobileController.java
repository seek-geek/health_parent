package com.itheima.health.controller;

import com.itheima.health.Utils.QiNiuUtils;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetMealService setMealService;

    @GetMapping("/getSetmeal")
    public Result getSetmeal() {
        List<Setmeal> setmeals = setMealService.findSetmealList();
        setmeals.forEach(s -> s.setImg(QiNiuUtils.DOMAIN + s.getImg()));
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeals);
    }

    @PostMapping("/findDetailsByid")
    public Result findDetailsByid(int id) {
        Setmeal setmeal = setMealService.findDetailsByid(id);
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }

    @GetMapping("/findById")
    public Result findById(int id) {
        Setmeal setmeal = setMealService.findById(id);
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }
}
