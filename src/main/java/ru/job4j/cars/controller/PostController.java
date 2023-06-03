package ru.job4j.cars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.service.PostService;

@Controller
@RequestMapping("/post")
public class PostController {
    private PostService service;

    public PostController(PostService simplePostService) {
        service = simplePostService;
    }

    @GetMapping("/")
    public String getAllPage(Model model) {
        model.addAttribute("posts", service.findAll());
        return "/post/list";
    }

    @GetMapping("/new")
    public String getNewPage(Model model) {
        model.addAttribute("posts", service.findAllNew());
        return "/post/list";
    }

    @GetMapping("/with-photo")
    public String getPhotoPage(Model model) {
        model.addAttribute("posts", service.findWithFile());
        return "/post/list";
    }

    @PostMapping("/mark")
    public String getMarkPage(@RequestParam String brand, Model model) {
        var list = service.findCarBrand(brand);
        if (list.isEmpty()) {
            model.addAttribute("message", "По запросу \"" + brand + "\" ничего не найдено");
            return "/error";
        }
        model.addAttribute("posts", list);
        return "/post/list";
    }

    @GetMapping("/{id}")
    public String getPostPage(@PathVariable int id, Model model) {
        var postOptional = service.findById(id);
        if (postOptional.isEmpty()) {
            model.addAttribute("message", "Не удалось найти пост");
            return "/error";
        }
        model.addAttribute("post", postOptional.get());
        return "/post/post";
    }

    @GetMapping("/create")
    public String getCreatePage() {
        return "/post/create";
    }

    @GetMapping("/modify")
    public String getModifyPage() {
        return "/post/modify";
    }
}
