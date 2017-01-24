package com.smmjocker.model;

import com.smmjocker.db.Tasks;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Oleg on 23.01.2017.
 */
@Service
public class Test implements Runnable {


    private Long id;
    SessionFactory sessionFact;

    public Test() {
    }


    public Test(Long id, SessionFactory sessionFact) {
        this.id = id;
        this.sessionFact = sessionFact;
    }

    //@Transactional
    //@PostConstruct
    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName());

        Session session = sessionFact.openSession();
        session.getTransaction().begin();
        Tasks task = (Tasks) session.get(Tasks.class, id);
        System.out.print(task.getId() + " ");
        task.setLastEnd(null);


//        cal.add(Calendar.MINUTE, task.getPerCount());
//        task.setNextStart(cal.getTime());
        System.out.println("Starting task " + task.getAssistant().getLogin());
        session.save(task);

        session.getTransaction().commit();
        session.close();


        try {
            Thread.sleep(1000 * 60 * 10 * task.getPerId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        session = sessionFact.openSession();
        session.getTransaction().begin();
        task = (Tasks) session.get(Tasks.class, id);

        System.out.println("End task " + task.getAssistant().getLogin());
        task.setLastEnd(new Date());

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, task.getPerCount());
        task.setNextStart(cal.getTime());

        session.save(task);
        session.getTransaction().commit();
        session.close();


    }
}
