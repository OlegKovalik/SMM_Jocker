package com.smmjocker.dao;

import com.smmjocker.db.Pages;
import com.smmjocker.db.Tasks;
import com.smmjocker.db.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TasksDao extends JpaRepository<Tasks, Long> {
    @Query("SELECT t FROM Tasks t where t.nextStart<=CURRENT_TIMESTAMP and t.lastEnd is Not NULL and t.go=true")
    List<Tasks> getTasksForStart();


}