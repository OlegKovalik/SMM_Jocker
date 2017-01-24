package com.smmjocker.dao;

import com.smmjocker.db.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Users, Long> {
    @Query("SELECT u FROM Users u where u.login = :login")
    Users findByLogin(@Param("login") String login);
}
