package ru.inie.taskboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.inie.taskboard.entity.User;
import ru.inie.taskboard.service.UserRepresentation;
import ru.inie.taskboard.service.UserService;

import java.security.Principal;

@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserRepresentation());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute("user") UserRepresentation userRepresentation,
                                  BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "register";
        if(!userRepresentation.getPassword().equals(userRepresentation.getRepeatPassword())) {
            bindingResult.rejectValue("password", "Пароли не совпадают");
            return "register";
        }

        userService.create(userRepresentation);
        return "redirect:/login";
    }

    @GetMapping("feed")
    public String userInfo(Model model,
                           Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        return "feed";
    }
}
