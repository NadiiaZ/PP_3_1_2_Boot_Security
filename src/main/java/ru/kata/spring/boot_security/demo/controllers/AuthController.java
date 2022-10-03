package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.servises.UserServiceImp;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserServiceImp userService;

    @Autowired
    public AuthController(UserServiceImp userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("userForm") User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String addNewUser(@ModelAttribute ("userForm") @Valid User formUser,
                             BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "auth/registration";
        }
        if(!formUser.getPassword().equals(formUser.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Different passwords!");
            return "auth/registration";
        }

        if(!userService.registerDefaultUser(formUser)) {
            model.addAttribute("usernameError",
                    "User with this email or password already exists");
            return "auth/registration";
        }
        return "redirect:auth/login";
    }
}