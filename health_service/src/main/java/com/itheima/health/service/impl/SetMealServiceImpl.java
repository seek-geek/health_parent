package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.SetMealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

@Service(interfaceClass = SetMealService.class)
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setMealDao.add(setmeal);
        Integer isetmealId= setmeal.getId();
        if (checkgroupIds != null){
            for (Integer checkgroupId : checkgroupIds){
                setMealDao.addSetmealCheckGroup(isetmealId,checkgroupId);
            }
        }
    }

    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+ queryPageBean.getQueryString()+"%");
        }
        Page<Setmeal> page = setMealDao.findByCondition(queryPageBean.getQueryString());
        PageResult<Setmeal> pageResult = new PageResult<Setmeal>(page.getTotal(),page.getResult());
        return pageResult;
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

    @Override
    public List<Setmeal> findSetmealList() {
        return setMealDao.findSetmealList();
    }

    @Override
    public Setmeal findDetailsByid(int id) {
        return setMealDao.findDetailsByid(id);
    }
}
