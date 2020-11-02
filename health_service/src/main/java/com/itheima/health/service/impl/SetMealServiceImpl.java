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
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;

    @Override
    public int add(Setmeal setmeal, Integer[] checkgroupIds) {
       int result = setMealDao.add(setmeal);
        Integer isetmealId= setmeal.getId();
        if (checkgroupIds != null){
            for (Integer checkgroupId : checkgroupIds){
                setMealDao.addSetmealCheckGroup(isetmealId,checkgroupId);
            }
        }
        return result;
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

    /**
     * 通过id查询套餐信息
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(int id) {
        return setMealDao.findById(id);
    }

    /**
     * 查询属于这个套餐的选中的检查组id
     */
    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        return setMealDao.findCheckgroupIdsBySetmealId(id);
    }

    @Override
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        // 更新套餐
        setMealDao.update(setmeal);
        // 删除旧关系
        setMealDao.deleteSetmealCheckGroup(setmeal.getId());
        // 添加新关系
        if(null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                setMealDao.addSetmealCheckGroup(setmeal.getId(), checkgroupId);
            }
        }
    }

    @Override
    public void deleteById(int id) throws MyException {
        // 判断是否被订单使用
        int count = setMealDao.findOrderCountBySetmealId(id);
        // 使用则报错
        if(count > 0){
            throw new MyException("该套餐已经被订单使用了，不能删除");
        }
        // 没使用
        // 先删除套餐与检查组的关系
        setMealDao.deleteSetmealCheckGroup(id);
        // 再删除套餐
        setMealDao.deleteById(id);
    }

    @Override
    public List<Setmeal> findSetmealList() {
        return setMealDao.findSetmealList();
    }

    @Override
    public Setmeal findDetailsByid(int id) {
        return setMealDao.findDetailsByid(id);
    }

    @Override
    public List<Map<String, Object>> getSetmealReport() {
        return setMealDao.getSetmealReport();
    }
}
