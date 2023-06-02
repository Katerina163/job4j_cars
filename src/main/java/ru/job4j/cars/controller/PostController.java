package ru.job4j.cars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.PostService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/post")
public class PostController {
    private PostService service;

    public PostController(PostService simplePostService) {
        service = simplePostService;
    }

    @GetMapping("/")
    public String getAllPage(Model model, HttpSession session) {
        registration(model, session);
        model.addAttribute("posts", service.findAll());
        return "/post/list";
    }

    @GetMapping("/new")
    public String getNewPage(Model model, HttpSession session) {
        registration(model, session);
        model.addAttribute("posts", service.findAllNew());
        return "/post/list";
    }

    @GetMapping("/with-photo")
    public String getPhotoPage(Model model, HttpSession session) {
        registration(model, session);
        model.addAttribute("posts", service.findWithFile());
        return "/post/list";
    }

    @PostMapping("/mark")
    public String getMarkPage(@RequestParam String brand, Model model, HttpSession session) {
        registration(model, session);
        var list = service.findCarBrand(brand);
        if (list.isEmpty()) {
            model.addAttribute("message", "По запросу \"" + brand + "\" ничего не найдено");
            return "/error";
        }
        model.addAttribute("posts", list);
        return "/post/list";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model, HttpSession session) {
        registration(model, session);
        return "/post/create";
    }

    @GetMapping("/modify")
    public String getModifyPage(Model model, HttpSession session) {
        registration(model, session);
        return "/post/modify";
    }

    private void registration(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setLogin("Гость");
        }
        model.addAttribute("user", user);
    }
}
