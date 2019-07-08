package com.hm.doman;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "student")
@Data
public class Student{

	@Id
	// 学号
	@Column(name = "id")
	private String id;

	// 姓名
	@Column(name = "name")
	private String name;

	@Column(name = "password")
	private String password;

	// 性别
	@Column(name = "gender")
	private String gender;

	// 电话
	@Column(name = "phone")
	private String phone;

	// 邮箱
	@Column(name = "email")
	private String email;

	// 院系
	@Column(name = "department")
	private String department;

	// 是否登录
	@Column(name = "islogin")
	private Integer islogin;
}