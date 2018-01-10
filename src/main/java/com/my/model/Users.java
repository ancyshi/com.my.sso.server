package com.my.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "users")
public class Users implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2705100612345452869L;
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	@Column(name = "user_name")
	private String userName;
	@Column(name = "pass_word")
	private String passWord;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
}
