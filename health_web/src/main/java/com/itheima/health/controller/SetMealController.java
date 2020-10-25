package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.entity.Result;
import com.itheima.health.service.SetMealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/checkitem")
public class SetMealController {
    private static final Logger log = LoggerFactory.getLogger(SetMealController.class);

    @Reference
    private SetMealService setMealService;

    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        String original = file.getOriginalFilename();
        String suffixName = original.substring(original.lastIndexOf("."));
        return null;
    }

}
