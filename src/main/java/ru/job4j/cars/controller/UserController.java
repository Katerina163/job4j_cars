package ru.job4j.cars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.ColorService;
import ru.job4j.cars.service.MarkService;
import ru.job4j.cars.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService service;
    private final ColorService colorService;
    private final MarkService markService;

    public UserController(UserService simpleUserService, ColorService simpleColorService,
                          MarkService simpleMarkService) {
        service = simpleUserService;
        colorService = simpleColorService;
        markService = simpleMarkService;
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
        var userSession = (User) session.getAttribute("user");
        var users = service.findByLogin(userSession.getLogin()).get();
        model.addAttribute("posts", users.getUserPosts().stream().map(Banner::new).toList())
                .addAttribute("subscribe", users.getParticipates().stream().map(Banner::new).toList())
                .addAttribute("marks", markService.findAll())
                .addAttribute("colors", colorService.findAll());
        return "/user/home";
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam int id, HttpSession session) {
        var user = (User) session.getAttribute("user");
        service.subscribe(user.getId(), id);
        return "redirect:/user/profile";
    }

    @GetMapping("/unsubscribe/{id}")
    public String unsubscribe(@PathVariable int id, HttpSession session) {
        var user = (User) session.getAttribute("user");
        service.unsubscribe(user.getId(), id);
        return "redirect:/user/profile";
    }
}
