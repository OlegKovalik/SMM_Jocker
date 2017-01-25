package com.smmjocker.controlers;

import com.smmjocker.exceptions.*;
import com.smmjocker.model.*;
import com.smmjocker.services.UserService;
import com.smmjocker.db.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyController {
    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = user.getUsername();

        Users dbUser = userService.getUserByLogin(login);

        model.addAttribute("login", login);
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("email", dbUser.getEmail());
        model.addAttribute("phone", dbUser.getPhone());

        return "index";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(required = false) String email, @RequestParam(required = false) String phone) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = user.getUsername();

        Users dbUser = userService.getUserByLogin(login);
        dbUser.setEmail(email);
        dbUser.setPhone(phone);

        userService.updateUser(dbUser);

        return "redirect:/";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping("/unauthorized")
    public String unauthorized(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("login", user.getUsername());
        return "unauthorized";
    }

    @RequestMapping("/pundyk")
    public String pundyk(Model model) {


        


        fbInviter.init();


        try {
            fbInviter.login();
            fbInviter.stepByStep();
        } catch (InviteLimitException e) {
            System.out.println("Invite Limit");
        } catch (InviteBlockException e) {
            System.out.println("Invite Block");
        } catch (InfiniteLoopException e) {
            e.printStackTrace();
        } catch (PostLimitException e) {
            e.printStackTrace();
        } catch (NoLikersException e) {
            e.printStackTrace();
        } finally {
            fbInviter.stop();
        }

        return "index";
    }

    @RequestMapping("/sribnyk")
    public String sribnyk(Model model) {


        


        fbInviter.init();

        try {
            fbInviter.login();
            fbInviter.stepByStep();
        } catch (InviteLimitException e) {
            System.out.println("Invite Limit");
        } catch (InviteBlockException e) {
            System.out.println("Invite Block");
        } catch (InfiniteLoopException e) {
            e.printStackTrace();
        } catch (PostLimitException e) {
            e.printStackTrace();
        } catch (NoLikersException e) {
            e.printStackTrace();
        } finally {
            fbInviter.stop();
        }

        return "index";
    }

    @RequestMapping("/menu")
    public String menu(Model model) {
        return "menu";
    }
}
