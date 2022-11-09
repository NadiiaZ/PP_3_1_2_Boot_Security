package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.UserDetailsImp;
import ru.kata.spring.boot_security.demo.servises.UserServiceImp;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImp userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserServiceImp userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")
    public String index(Model model, @ModelAttribute("userForm") User userForm) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImp userDetails = (UserDetailsImp) auth.getPrincipal();
        model.addAttribute("authUser", userDetails.getUser());

        List<Role> roles = userService.getAllRoles();
        model.addAttribute("roles",roles);

        List<User> users = userService.showAllUsers();
        model.addAttribute("user",users);

        return "admin/index";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
    @PostMapping("/new")
    public String addUser(@ModelAttribute ("userForm") User formUser, Model model) {
        if(!formUser.getPassword().equals(formUser.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Different passwords!");
            return "admin/index";
        }
        formUser.setPassword(passwordEncoder.encode(formUser.getPassword()));
        userService.save(formUser);
        return "redirect:/admin";
    }

    @PatchMapping ("/edit/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("userForm") User userUpdate) {

        userService.updateUser(id, userUpdate);
        return "redirect:/admin";
    }
}