package com.my.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


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
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="users_id",referencedColumnName="id")//在cookieId表增加一个外键列来实现一对多的单向关联
	private Set<CookieId> cookieIds = new HashSet<CookieId>();
	
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

	public Set<CookieId> getCookieIds() {
		return cookieIds;
	}

	public void setCookieIds(Set<CookieId> cookieIds) {
		this.cookieIds = cookieIds;
	}
}
