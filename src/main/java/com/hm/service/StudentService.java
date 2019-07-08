package com.hm.service;

import com.hm.doman.Student;
import com.hm.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 添加
     * @param student
     */
    public void save(Student student){
        studentMapper.insert(student);
    }

    /**
     * 主键删除
     * @param id
     */
    public void delete(String id){
        studentMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改
     * @param student
     */
    public void update(Student student){
        studentMapper.updateByPrimaryKey(student);
    }

    /**
     * 查询所有
     * @return
     */
    public List<Student> findAll(){
        return studentMapper.selectAll();
    }
    /**
     * 查询
     * @return
     */
    public Student findById(String id){
        return studentMapper.selectByPrimaryKey(id);
    }
}