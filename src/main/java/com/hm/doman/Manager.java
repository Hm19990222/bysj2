package com.hm.doman;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "manager")
@Data
public class Manager{

	@Id
	@Column(name = "id")
	private Integer id;

	// 帐号
	@Column(name = "username")
	private String username;

	// 密码
	@Column(name = "password")
	private String password;
}