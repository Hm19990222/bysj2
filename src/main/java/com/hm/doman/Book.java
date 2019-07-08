package com.hm.doman;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "book")
@Data
public class Book{

	@Id
	// id
	@Column(name = "id")
	private Integer id;

	// 书名
	@Column(name = "bookname")
	private String bookname;

	// 价格
	@Column(name = "price")
	private Double price;
}