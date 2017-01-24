package com.smmjocker.dao;

import com.smmjocker.db.Assistants;
import com.smmjocker.db.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssistantsDao extends JpaRepository<Assistants, Long> {

//    @Query("SELECT a FROM Assistant a where a.user=null and a.page=null")
//    List<Assistants> getFreeAssistant();

    Assistants findFirstByUserIsNullAndPageIsNull();

    List<Assistants> findByUserAndPageIsNull(Users user);

    List<Assistants> findByUserAndPageIsNotNullAndTaskIsNull(Users user);

    List<Assistants> findByUserAndPageIsNotNullAndTaskIsNotNull(Users user);

//    List<Assistants> findByUserAndPage(Users user, Pages page);


}