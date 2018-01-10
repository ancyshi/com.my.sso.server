package com.my.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.model.Users;

import org.springframework.data.jpa.repository.Query;

public interface UsersJPA extends JpaRepository<Users,Long>{
	
	@Query(value = "select * from users where user_name = ?1 and pass_word=?2", nativeQuery = true)
	Users findByUserNameAndPassWord(String userName,String passWord);
	
}

