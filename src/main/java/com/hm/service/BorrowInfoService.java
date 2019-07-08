package com.hm.service;

import com.hm.doman.BorrowInfo;
import com.hm.mapper.BorrowInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BorrowInfoService {

    @Autowired
    private BorrowInfoMapper borrowinfoMapper;

    /**
     * 添加
     * @param borrowinfo
     */
    public void save(BorrowInfo borrowinfo){
        borrowinfoMapper.insert(borrowinfo);
    }

    /**
     * 主键删除
     * @param id
     */
    public void delete(Integer id){
        borrowinfoMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改
     * @param borrowinfo
     */
    public void update(BorrowInfo borrowinfo){
        borrowinfoMapper.updateByPrimaryKey(borrowinfo);
    }

    /**
     * 查询所有
     * @return
     */
    public List<BorrowInfo> findAll(){
        return borrowinfoMapper.selectAll();
    }
    /**
     * 查询
     * @return
     */
    public BorrowInfo findById(Integer id){
        return borrowinfoMapper.selectByPrimaryKey(id);
    }
}