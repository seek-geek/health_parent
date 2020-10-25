package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
//        return checkItemDao.findCountByCheckItemId(queryPageBean);
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        if (!StringUtils.isNullOrEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        Page<CheckItem> pages = checkItemDao.findPage(queryPageBean.getQueryString());
        PageResult<CheckItem> pageResult = new PageResult<>(pages.getTotal(),pages.getResult());
        return pageResult;
    }

    @Override
    public void deleteById(int id) {
        int count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0) {
            throw new MyException("该检查项已经被使用了，不能删除!");
        }
        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }
}
