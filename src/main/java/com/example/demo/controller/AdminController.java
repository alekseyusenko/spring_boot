package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("users", userService.allUsers());
        return "adminPage";
    }

    @GetMapping("/admin/new")
    public String newUserForm(@ModelAttribute("user") User user) {
        return "new";
    }

    @PostMapping("/admin/new")
    public String createNewUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit/{id}")
    public String editForm(Model model, @PathVariable Long id) {
        model.addAttribute("user", userService.findUserById(id));
        return "edit";
    }

    @PostMapping("/admin/edit/{id}")
    public String update(User user) {
        userService.editUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

}

