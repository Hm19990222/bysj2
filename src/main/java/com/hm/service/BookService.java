package com.hm.service;

import com.hm.doman.Book;
import com.hm.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BookService  {

    @Autowired
    private BookMapper bookMapper;

    /**
     * 添加
     * @param book
     */
    public void save(Book book){
        bookMapper.insert(book);
    }

    /**
     * 主键删除
     * @param id
     */
    public void delete(Integer id){
        bookMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改
     * @param book
     */
    public void update(Book book){
        bookMapper.updateByPrimaryKey(book);
    }

    /**
     * 查询所有
     * @return
     */
    public List<Book> findAll(){
        return bookMapper.selectAll();
    }
    /**
     * 查询
     * @return
     */
    public Book findById(Integer id){
        return bookMapper.selectByPrimaryKey(id);
    }
}