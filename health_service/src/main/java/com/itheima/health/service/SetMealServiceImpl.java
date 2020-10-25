package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.SetMealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = SetMealService.class)
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {

    }

    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        return null;
    }

    @Override
    public Setmeal findById(int id) {
        return null;
    }

    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        return null;
    }

    @Override
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {

    }

    @Override
    public void deleteById(int id) throws MyException {

    }
}
