package ru.inie.taskboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.inie.taskboard.entity.User;
import ru.inie.taskboard.repositories.UserRepository;
import ru.inie.taskboard.service.UserService;

import java.security.Principal;

@Controller
//@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/id{id}")
    public String userInfo(@PathVariable("id") long id,
                           Model model,
                           Principal principal) {

        User authorizedUser = userService.findByEmail(principal.getName());
        User user = userService.findById(id);

        model.addAttribute("authorizedUser", authorizedUser);
        model.addAttribute("user", user);
        if(authorizedUser.equals(user)) {
            model.addAttribute("subscribers", authorizedUser.getSubscribers());
            model.addAttribute("subscriptions", authorizedUser.getSubscriptions());
        } else {
            model.addAttribute("subscribers", user.getSubscribers());
            model.addAttribute("subscriptions", user.getSubscriptions());
        }

        return "user";
    }

    @PostMapping("/id{id}/subscribe")
    public String subscribe(@PathVariable("id") long id,
                            Principal principal) {
        User authorizedUser = userService.findByEmail(principal.getName());
        User user = userService.findById(id);

        userService.subscribe(authorizedUser, user);

        return "redirect:/id" + authorizedUser.getId();
    }

    @PostMapping("/id{id}/unsubscribe")
    public String unsubscribe(@PathVariable("id") long id,
                            Principal principal) {
        User authorizedUser = userService.findByEmail(principal.getName());
        User user = userService.findById(id);

        userService.unsubscribe(authorizedUser, user);

        return "redirect:/id" + authorizedUser.getId();
    }


}
