package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {

    }

    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        return null;
    }

    @Override
    public CheckGroup findById(int id) {
        return null;
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int id) {
        return null;
    }

    @Override
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {

    }

    @Override
    public void deleteById(int id) throws MyException {

    }
}
