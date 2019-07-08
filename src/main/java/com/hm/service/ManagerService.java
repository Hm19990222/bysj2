package com.hm.service;

import com.hm.doman.Manager;
import com.hm.mapper.ManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ManagerService {

    @Autowired
    private ManagerMapper managerMapper;

    /**
     * 添加
     * @param manager
     */
    public void save(Manager manager){
        managerMapper.insert(manager);
    }

    /**
     * 主键删除
     * @param id
     */
    public void delete(Integer id){
        managerMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改
     * @param manager
     */
    public void update(Manager manager){
        managerMapper.updateByPrimaryKey(manager);
    }

    /**
     * 查询所有
     * @return
     */
    public List<Manager> findAll(){
        return managerMapper.selectAll();
    }
    /**
     * 查询
     * @return
     */
    public Manager findById(Integer id){
        return managerMapper.selectByPrimaryKey(id);
    }
}