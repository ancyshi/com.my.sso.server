package com.my.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.my.model.Users;


public interface UsersJPA extends JpaRepository<Long,Users>{
	@Query(value = "select * from users where user_name = ?1 and pass_word=?2", nativeQuery = true)
	Users findByUserNameAndPassWord(String userName,String passWord);

}
