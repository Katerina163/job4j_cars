package ru.job4j.cars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.Criterion;
import ru.job4j.cars.dto.FileDTO;
import ru.job4j.cars.dto.QPredicate;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.Color;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.AutoPostService;
import ru.job4j.cars.service.FileService;
import ru.job4j.cars.service.MarkService;
import ru.job4j.cars.service.PriceHistoryService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/post")
public class PostController {
    private final AutoPostService service;
    private final FileService fileService;
    private final PriceHistoryService priceService;
    private final MarkService markService;

    public PostController(AutoPostService simpleAutoPostService, FileService simpleFileService,
                          PriceHistoryService simplePriceHistoryService, MarkService simpleMarkService) {
        service = simpleAutoPostService;
        fileService = simpleFileService;
        priceService = simplePriceHistoryService;
        markService = simpleMarkService;
    }

    @GetMapping("/")
    public String getAllPage(Model model) {
        addMarkAndColor(model).addAttribute("posts",
                service.search(new Criterion().findAll(), QPredicate::and));
        return "/post/list";
    }

    @GetMapping("/new")
    public String getNewPage(Model model) {
        addMarkAndColor(model).addAttribute("posts",
                service.search(new Criterion().fresh(), QPredicate::and));
        return "/post/list";
    }

    @GetMapping("/with-photo")
    public String getPageWithFile(Model model) {
        addMarkAndColor(model).addAttribute("posts",
                service.search(new Criterion().withFile(), QPredicate::and));
        return "/post/list";
    }

    @PostMapping("/brand")
    public String getMarkPage(@RequestParam String brand, Model model) {
        var list = service.search(new Criterion().addBrand(brand), QPredicate::and);
        if (list.isEmpty()) {
            addMarkAndColor(model).addAttribute("message", "По запросу \"" + brand + "\" ничего не найдено");
            return "/error";
        }
        addMarkAndColor(model).addAttribute("posts", list);
        return "/post/list";
    }

    @GetMapping("/mark/{id}")
    public String getByMark(@PathVariable long id, Model model) {
        addMarkAndColor(model).addAttribute("posts",
                service.search(new Criterion().addMarkId(id), QPredicate::and));
        return "/post/list";
    }

    @GetMapping("/color/{name}")
    public String getByColor(@PathVariable String name, Model model) {
        var color = Color.valueOf(name);
        addMarkAndColor(model).addAttribute("posts",
                service.search(new Criterion().addColor(color), QPredicate::and));
        return "/post/list";
    }

    @GetMapping("/{id}")
    public String getPostPage(@PathVariable int id, Model model) {
        var postOptional = service.findById(id);
        if (postOptional.isEmpty()) {
            addMarkAndColor(model).addAttribute("message", "Не удалось найти пост");
            return "/error";
        }
        addMarkAndColor(model).addAttribute("post", postOptional.get());
        return "/post/post";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model) {
        addMarkAndColor(model);
        return "/post/create";
    }

    @PostMapping("/create")
    public String create(@RequestParam Map<String, String> allParams,
                         @RequestParam MultipartFile file, HttpSession session) throws IOException {
        var user = (User) session.getAttribute("user");
        service.save(user.getLogin(), allParams, file);
        return "redirect:/user/profile";
    }

    @PostMapping("/add/{id}")
    public String addFile(@RequestParam MultipartFile file, @PathVariable int id) throws IOException {
        if (!file.isEmpty()) {
            fileService.save(new FileDTO(file.getOriginalFilename(), id, file.getBytes()));
        }
        return "redirect:/post/" + id;
    }

    @PostMapping("/sold")
    public String sold(@ModelAttribute AutoPost post) {
        service.soldById(post.getId(), !post.isSold());
        return "redirect:/post/" + post.getId();
    }

    @PostMapping("/change-price")
    public String changePrice(@RequestParam long id, @RequestParam long price) {
        priceService.save(price, id);
        return "redirect:/post/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        service.delete(id);
        return "redirect:/user/profile";
    }

    @GetMapping("/modify/{id}")
    public String getModifyPage(@PathVariable int id, Model model) {
        var post = service.findById(id);
        if (post.isEmpty()) {
            addMarkAndColor(model).addAttribute("message", "Не удалось найти пост");
            return "error";
        }
        addMarkAndColor(model).addAttribute("post", post.get());
        return "/post/modify";
    }

    @PostMapping("/modify")
    public String modify(@RequestParam AutoPost post, HttpSession session) {
        System.out.println(post);
        //  var user = (User) session.getAttribute("user");
        //     service.modify(params);
        return "/post/modify";
    }

    private Model addMarkAndColor(Model model) {
        model.addAttribute("marks", markService.findAll())
                .addAttribute("colors", Color.values());
        return model;
    }
}
