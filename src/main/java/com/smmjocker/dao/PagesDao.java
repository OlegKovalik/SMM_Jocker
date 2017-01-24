package com.smmjocker.dao;

import com.smmjocker.db.Pages;
import com.smmjocker.db.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PagesDao extends JpaRepository<Pages, Long> {
    @Query("SELECT p FROM Pages p where p.user=:user")
    List<Pages> getPagersForUser(@Param("user") Users user);
}