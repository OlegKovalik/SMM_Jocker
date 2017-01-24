package com.smmjocker.controlers;

import com.smmjocker.db.Assistants;
import com.smmjocker.db.Pages;
import com.smmjocker.db.Tasks;
import com.smmjocker.db.Users;
import com.smmjocker.services.InviteService;
import com.smmjocker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequestMapping("/invite")
public class InviteController {

    @Autowired
    private UserService userService;

    @Autowired
    private InviteService inviteService;

    private Assistants assistant;

    @RequestMapping("/pages")
    public String pages(Model model) {
        model.addAttribute("pages", inviteService.getPagesForUser(getCurrentUser()));

        return "pages";
    }

    @RequestMapping(value = "/pages/add", method = RequestMethod.POST)
    public String pagesAdd(Model model, @RequestParam(name = "page-url", required = false) String page) {
        System.out.println(page);

        inviteService.savePage(new Pages(page, getCurrentUser()));
        return "redirect:/invite/pages";
    }

    @RequestMapping(value = "/pages/del", method = RequestMethod.POST)
    public ResponseEntity<Void> pagesDel(Model model, @RequestParam(required = false) Long pageId) {
        System.out.println(pageId);
        if (pageId != null) {
            inviteService.delPage(pageId);
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping("/bind_assistant")
    public String bindAssistant(Model model) {
        model.addAttribute("userPages", inviteService.getPagesForUser(getCurrentUser()));
        model.addAttribute("userAssistants", inviteService.getAssistantForUserWithoutPages(getCurrentUser()));
        model.addAttribute("pages", inviteService.getPagesForUser(getCurrentUser()));

        return "bind_assis";
    }

    @RequestMapping(value = "/bind_assistant/bind", method = RequestMethod.POST)
    public String bindingAssistant(Model model, @RequestParam(name = "pageId", required = false) Long pageId, @RequestParam(name = "assisId", required = false) Long assisId) {

        if (pageId != null && assisId != null) {
            Assistants assistant = inviteService.getOneAssistant(assisId);
            assistant.setPage(inviteService.getOnePage(pageId));
            inviteService.saveAssistant(assistant);
        }

//        model.addAttribute("userPages", inviteService.getPagesForUser(getCurrentUser()));
//        model.addAttribute("userAssistants", inviteService.getAssistantForUserWithoutPages(getCurrentUser()));
//        model.addAttribute("pages", inviteService.getPagesForUser(getCurrentUser()));

        return "redirect:/invite/bind_assistant";
    }

    @RequestMapping("/bind_assistant/buy")
    public ResponseEntity<Void> buyAssistant(Model model) {

        Assistants assistant = inviteService.getOneFreeAssistant();
        assistant.setUser(getCurrentUser());
        inviteService.saveAssistant(assistant);

//        model.addAttribute("userPages", inviteService.getPagesForUser(getCurrentUser()));
//        model.addAttribute("userAssistants", inviteService.getAssistantForUserWithoutPages(getCurrentUser()));
//        model.addAttribute("pages", inviteService.getPagesForUser(getCurrentUser()));


        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping("/tasker")
    public String tasker(Model model) {


        model.addAttribute("assistants", inviteService.getAssistantForUserWithPages(getCurrentUser()));
        model.addAttribute("tasks", inviteService.getAssistantForUserWithPagesAndTasks(getCurrentUser()));


        return "tasker";
    }

    @RequestMapping(value = "/tasker/add", method = RequestMethod.POST)
    public String taskerAdd(Model model, @RequestParam(required = false) Long assisId,
                            @DateTimeFormat(pattern = "dd.MM.yyyy - HH:mm") @RequestParam(required = false) Date datetime,
                            @RequestParam(required = false) Integer perCount,
                            @RequestParam(required = false) Integer perId) {

        System.out.println(assisId);
        System.out.println(datetime);
        System.out.println(perCount);
        System.out.println(perId);

        inviteService.saveTask(new Tasks(inviteService.getOneAssistant(assisId), datetime, perId, perCount, false));


        return "redirect:/invite/tasker";
    }

    @RequestMapping(value = "/tasker/action", method = RequestMethod.POST)
    public ResponseEntity<Void> taskerAction(Model model, @RequestParam(required = false) Long taskId) {
        System.out.println(taskId);
        if (taskId != null) {
            Tasks task = inviteService.getOneTask(taskId);
            task.setGo(!task.isGo());
            inviteService.saveTask(task);
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @RequestMapping("/add_assistant")
    public String addAssistant(Model model) {
        System.out.println();
        model.addAttribute("assistants", inviteService.getAssistants());


        if (assistant != null) {
            System.out.println("Edit mode");
            model.addAttribute("assistant", assistant);
            assistant = null;
        }
        return "add_assis";
    }

    @RequestMapping(value = "/add_assistant/add", method = RequestMethod.POST)
    public String addAssistantDo(Model model, @RequestParam(required = false) String login, @RequestParam(required = false) String pass) {
        if (login != null && pass != null) {
            inviteService.saveAssistant(new Assistants(login, pass));
        }
        return "redirect:/invite/add_assistant";
    }


    @RequestMapping(value = "/add_assistant/edit", method = RequestMethod.POST)
    public ResponseEntity<Void> addAssistantEdit(Model model, @RequestParam(required = false) Long assisId) {
        System.out.println(assisId);
        if (assisId != null) {
            assistant = inviteService.getOneAssistant(assisId);
        }
        System.out.println(assistant);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/add_assistant/update", method = RequestMethod.POST)
    public String addAssistantEdit(Model model, @RequestParam(required = false) String login, @RequestParam(required = false) String pass, @RequestParam(required = false) Long assisId) {
        System.out.println(login + " " + pass + " " + assisId);
        if (login != null && pass != null && assisId != null) {
            Assistants assistant = inviteService.getOneAssistant(assisId);
            assistant.setLogin(login);
            assistant.setPassword(pass);
            inviteService.saveAssistant(assistant);
        }

        return "redirect:/invite/add_assistant";
    }


    @RequestMapping("/menu")
    public String menu(Model model) {
        return "menu";
    }

    private Users getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = user.getUsername();

        return userService.getUserByLogin(login);
    }

}
