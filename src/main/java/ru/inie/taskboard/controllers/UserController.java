package ru.inie.taskboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.inie.taskboard.entity.User;
import ru.inie.taskboard.repositories.UserRepository;
import ru.inie.taskboard.service.UserRepresentation;
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

    @GetMapping("/id{id}/edit")
    public String editPage(@PathVariable("id") long id,
                       Principal principal,
                       Model model) {

        User authorizedUser = userService.findByEmail(principal.getName());
        User user = userService.findById(id);

        if (authorizedUser.equals(user)) {
            model.addAttribute("user", new UserRepresentation());
            model.addAttribute("authorizedUser", authorizedUser);
            return "edit";
        }

        return "redirect:/id" + authorizedUser.getId();
    }

    @PostMapping("/id{id}/edit")
    public String editUser(@PathVariable("id") long id,
                           Principal principal,
                           Model model,
                           @ModelAttribute("user") UserRepresentation userRepresentation,
                           BindingResult bindingResult) {
        User authorizedUser = userService.findByEmail(principal.getName());
        User user = userService.findById(id);

        if (authorizedUser.equals(user)) {
            if(bindingResult.hasErrors())
                return "edit";
            if(!userRepresentation.getPassword().equals(userRepresentation.getRepeatPassword())) {
                bindingResult.rejectValue("password", "Пароли не совпадают");
                return "edit";
            }

            userRepresentation.setId(authorizedUser.getId());
            userService.create(userRepresentation);
        }

        return "redirect:/id" + authorizedUser.getId();
    }
}
