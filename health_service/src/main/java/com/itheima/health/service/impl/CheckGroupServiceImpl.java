package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;


@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        Integer checkGroupId = checkGroup.getId();
        if (checkitemIds != null) {
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroupId, checkitemId);
            }
        }
    }

    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+ queryPageBean.getQueryString()+"%");
        }
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryPageBean.getQueryString());
        PageResult<CheckGroup> pageResult = new PageResult<>(page.getTotal(),page.getResult());
        return pageResult;
    }

    @Override
    public CheckGroup findById(int id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.update(checkGroup);
        checkGroupDao.deleteCheckGroupCheckItem(checkGroup.getId());
        if(null != checkitemIds){
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroup.getId(), checkitemId);
            }
        }
    }

    @Override
    public void deleteById(int id) throws MyException {
        int count = checkGroupDao.findCountByCheckGroupId(id);
        if (count > 0){
            throw new MyException("该检查组已经被套餐使用了，不能删除");
        }
        checkGroupDao.deleteCheckGroupCheckItem(id);
        checkGroupDao.deleteById(id);
    }
}
