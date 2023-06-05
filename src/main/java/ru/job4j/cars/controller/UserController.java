package ru.job4j.cars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.PostService;
import ru.job4j.cars.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService service;
    private PostService postService;

    public UserController(UserService simpleUserService, PostService simplePostService) {
        service = simpleUserService;
        postService = simplePostService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model, HttpServletRequest request) {
        var loginUser = service.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (loginUser.isEmpty()) {
            model.addAttribute("message", "Неверно введен логин или пароль");
            return "/error";
        }
        request.getSession().setAttribute("user", loginUser.get());
        return "redirect:/post/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
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

    @GetMapping("/profile")
    public String getProfile(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        model.addAttribute("posts", postService.findUsersCar(user.getLogin()));
        model.addAttribute("subscribe",
                service.findParticipatesById(user.getId())
                .stream()
                .flatMap(u -> u.getParticipates().stream())
                .toList());
        return "/user/home";
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam int id, HttpSession session) {
        var user = (User) session.getAttribute("user");
        service.addAutoPostByUserId(user.getId(), id);
        return "redirect:/user/profile";
    }

    @GetMapping("/unsubscribe/{id}")
    public String unsubscribe(@PathVariable int id, HttpSession session) {
        var user = (User) session.getAttribute("user");
        service.deleteAutoPostByUserId(user.getId(), id);
        return "redirect:/user/profile";
    }
}
