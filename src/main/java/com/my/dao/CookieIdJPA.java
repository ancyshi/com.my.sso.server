package com.my.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.model.CookieId;
import org.springframework.data.jpa.repository.Query;

public interface CookieIdJPA extends JpaRepository<CookieId, String> {

    @Query(value = "select * from cookies where cookie_id = ?1", nativeQuery = true)
    CookieId jpaFindOne(String cookieId);

}
