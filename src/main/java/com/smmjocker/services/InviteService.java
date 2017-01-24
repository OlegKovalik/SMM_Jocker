package com.smmjocker.services;

import com.smmjocker.dao.AssistantsDao;
import com.smmjocker.dao.PagesDao;
import com.smmjocker.dao.TasksDao;
import com.smmjocker.db.Assistants;
import com.smmjocker.db.Pages;
import com.smmjocker.db.Tasks;
import com.smmjocker.db.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Oleg on 21.01.2017.
 */
@Service(value = "InviteService")
public class InviteService {

    @Autowired
    private PagesDao pagesDao;

    @Autowired
    private AssistantsDao assistantsDao;

    @Autowired
    private TasksDao tasksDao;


    @Transactional
    public void saveTask(Tasks tasks) {
        tasksDao.save(tasks);
    }

    @Transactional(readOnly = true)
    public Tasks getOneTask(Long id) {
        return tasksDao.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Tasks> getTasksForStart() {
        return tasksDao.getTasksForStart();
    }


    @Transactional
    public void savePage(Pages page) {
        pagesDao.save(page);
    }

    @Transactional(readOnly = true)
    public List<Pages> getPagesForUser(Users user) {
        return pagesDao.getPagersForUser(user);
    }

    @Transactional(readOnly = true)
    public Pages getOnePage(Long id) {
        return pagesDao.getOne(id);
    }


    @Transactional
    public void delPage(Long id) {
        pagesDao.delete(id);
    }

    @Transactional
    public void saveAssistant(Assistants assistant) {
        assistantsDao.save(assistant);
    }

    @Transactional(readOnly = true)
    public List<Assistants> getAssistants() {
        return assistantsDao.findAll();
    }

    @Transactional(readOnly = true)
    public Assistants getOneAssistant(Long id) {
        return assistantsDao.findOne(id);
    }

    @Transactional(readOnly = true)
    public Assistants getOneFreeAssistant() {
        return assistantsDao.findFirstByUserIsNullAndPageIsNull();
    }

    @Transactional(readOnly = true)
    public List<Assistants> getAssistantForUserWithoutPages(Users user) {
        return assistantsDao.findByUserAndPageIsNull(user);
    }

    @Transactional(readOnly = true)
    public List<Assistants> getAssistantForUserWithPages(Users user) {
        return assistantsDao.findByUserAndPageIsNotNullAndTaskIsNull(user);
    }


    @Transactional(readOnly = true)
    public List<Assistants> getAssistantForUserWithPagesAndTasks(Users user) {
        return assistantsDao.findByUserAndPageIsNotNullAndTaskIsNotNull(user);
    }

//    @Transactional(readOnly = true)
//    public Assistants getAssistantBindedPage(Users user, Pages page) {
//        return assistantsDao.findByUserAndPage();
//    }


}
