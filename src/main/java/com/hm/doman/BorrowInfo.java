package com.hm.doman;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "borrow_info")
@Data
public class BorrowInfo{

	@Id
	// 借阅信息id
	@Column(name = "id")
	private Integer id;

	// 学号
	@Column(name = "student_id")
	private String studentId;

	// 书名
	@Column(name = "book_id")
	private Integer bookId;

	// 管理员id
	@Column(name = "manager_id")
	private String managerId;
}