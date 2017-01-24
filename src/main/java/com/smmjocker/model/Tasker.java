package com.smmjocker.model;

import com.smmjocker.db.Tasks;
import com.smmjocker.services.InviteService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

/**
 * Created by Oleg on 19.01.2017.
 */
@Component

public class Tasker {
    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    InviteService inviteService;

    @Autowired
    EntityManagerFactory emf;

    @Scheduled(fixedDelay = 1 * 5 * 60 * 1000, initialDelay = 30 * 1000)
    public void runTasks() {
        System.out.println("Run tasker");


        System.out.println(Thread.currentThread().getName());
        for (Tasks task : inviteService.getTasksForStart()) {

            System.out.println("ActiveCount " + taskScheduler.getActiveCount());
            taskScheduler.execute(new FBInviter(task.getId(), emf.unwrap(SessionFactory.class)));
        }
    }
}
