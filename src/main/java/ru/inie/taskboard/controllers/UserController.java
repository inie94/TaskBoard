package ru.inie.taskboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.inie.taskboard.entity.User;
import ru.inie.taskboard.repositories.UserRepository;
import ru.inie.taskboard.service.UserService;

import java.security.Principal;

@Controller
//@RequestMapping("user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{id}")
    public String userInfo(@PathVariable("id") long id,
                           Model model,
                           Principal principal) {
        model.addAttribute("user", userRepository.findById(id).get());
        return "user";
    }


}
