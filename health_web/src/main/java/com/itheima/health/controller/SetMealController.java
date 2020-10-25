package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.Utils.QiNiuUtils;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/checkitem")
public class SetMealController {
    private static final Logger log = LoggerFactory.getLogger(SetMealController.class);

    @Reference
    private SetMealService setMealService;

    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        String original = imgFile.getOriginalFilename();
        String suffixName = original.substring(original.lastIndexOf("."));
        String uuidName = UUID.randomUUID().toString() + suffixName;
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),uuidName);
            Map<String,String> result = new HashMap<>();
            result.put("domain",QiNiuUtils.DOMAIN);
            result.put("imgName",uuidName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,result);
        } catch (IOException e) {
            log.error("上传图片失败",e);
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @PostMapping("/add")
    public Result add(Setmeal setmeal,Integer[] checkGroupIds){
        setMealService.add(setmeal,checkGroupIds);
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @PostMapping("/findPage")
    public Result findByPage(@RequestBody QueryPageBean pageBean){
        PageResult<Setmeal> pageDta = setMealService.findPage(pageBean);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,pageDta);
    }

}
