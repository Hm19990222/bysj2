package com.hm.service;

import com.hm.doman.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Dvdcontroller {

    @Autowired
    private BookService bookService;

    @GetMapping("/findAll")
    public String findAll(){
        List<Book> all = bookService.findAll();
        System.out.println(all);
        return "哈哈哈哈";
    }





}
