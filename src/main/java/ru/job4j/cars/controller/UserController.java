package ru.job4j.cars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService service;

    public UserController(UserService simpleUserService) {
        service = simpleUserService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model, HttpServletRequest request) {
        var loginUser = service.findByLogin(user.getLogin());
        if (loginUser.isEmpty()) {
            model.addAttribute("message", "Неверно введен логин");
            return "/error";
        }
        if (!loginUser.get().getPassword().equals(user.getPassword())) {
            model.addAttribute("message", "Неверно введен пароль");
            return "/error";
        }
        request.getSession().setAttribute("user", loginUser.get());
        return "redirect:/post/";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "/user/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        service.create(user);
        return "/user/login";
    }
}
