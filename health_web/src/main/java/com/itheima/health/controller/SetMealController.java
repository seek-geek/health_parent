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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    private static final Logger log = LoggerFactory.getLogger(SetMealController.class);
    @Reference
    private SetMealService setMealService;
    @Autowired
    private JedisPool jedisPool;

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
        Integer setmealId =setMealService.add(setmeal,checkGroupIds);
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        jedis.sadd(key,setmealId+"|1|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @PostMapping("/findPage")
    public Result findByPage(@RequestBody QueryPageBean pageBean){
        PageResult<Setmeal> pageDta = setMealService.findPage(pageBean);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,pageDta);
    }


    @GetMapping("/findById")
    public Result findById(int id){
        Setmeal setmeal = setMealService.findById(id);
        /**
         * {
         *     flag:
         *     message:
         *     data:{
         *          setmeal: setmeal,
         *          domain: QiNiuUtils.DOMAIN
         *     }
         * }
         */
        Map<String,Object> resultMap = new HashMap<String,Object>(2);
        resultMap.put("setmeal",setmeal);
        resultMap.put("domain",QiNiuUtils.DOMAIN);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,resultMap);
    }

    /**
     * 查询属于这个套餐的选中的检查组id
     */
    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id){
        // 后端list => 前端 [], javaBean 或 map => json{}
        // 查询属于这个套餐的选中的检查组id
        List<Integer> list = setMealService.findCheckgroupIdsBySetmealId(id);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,list);
    }
    /**
     * 编辑套餐的提交
     */
    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        // 调用服务更新
        setMealService.update(setmeal,checkgroupIds);
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        jedis.sadd(key,setmeal.getId()+"|1|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true, "更新套餐成功");
    }

    /**
     * 删除套餐
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        // 调用服务删除
        setMealService.deleteById(id);
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        jedis.sadd(key,id+"|0|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true, "删除套餐成功！");
    }
}
