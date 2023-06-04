package ru.job4j.cars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.FileDTO;
import ru.job4j.cars.model.*;
import ru.job4j.cars.service.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

@Controller
@RequestMapping("/post")
public class PostController {
    private PostService service;
    private FileService fileService;
    private CarService carService;
    private EngineService engineService;
    private OwnerService ownerService;
    private PriceHistoryService priceHistoryService;

    public PostController(PostService simplePostService, FileService simpleFileService, CarService simpleCarService,
                          EngineService simpleEngineService, OwnerService simpleOwnerService,
                          PriceHistoryService simplePriceHistoryService) {
        service = simplePostService;
        fileService = simpleFileService;
        carService = simpleCarService;
        engineService = simpleEngineService;
        ownerService = simpleOwnerService;
        priceHistoryService = simplePriceHistoryService;
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

    @PostMapping("/create")
    public String create(@RequestParam String engineName, @RequestParam String carName, @RequestParam String owners,
                         @RequestParam String description, @RequestParam Integer price, @RequestParam MultipartFile file,
                         HttpSession session) throws IOException {
        var today = Date.valueOf(LocalDate.now());
        var user = (User) session.getAttribute("user");
        var engine = new Engine();
        engine.setName(engineName);
        engineService.create(engine);
        var car = new Car();
        car.setName(carName);
        car.setEngine(engine);
        carService.create(car);
        var post = new AutoPost();
        post.setDescription(description);
        post.setCar(car);
        post.setCreated(today);
        post.setSold(false);
        post.setUser(user);
        String[] ownersNames = owners.split(", ");
        for (var name : ownersNames) {
            var owner = new Owner();
            owner.setUser(user);
            owner.setName(name);
            ownerService.create(owner);
            post.getCar().getOwners().add(owner);
        }
        service.add(post);
        var priceHistory = new PriceHistory();
        priceHistory.setBefore(price);
        priceHistory.setAfter(price);
        priceHistory.setCreated(today);
        priceHistory.setPost(post);
        priceHistoryService.create(priceHistory);
        fileService.save(new FileDTO(file.getOriginalFilename(), post.getId(), file.getBytes()));
        return "redirect:/user/profile";
    }

    @PostMapping("/add/{id}")
    public String add(@RequestParam MultipartFile file, @PathVariable int id) throws IOException {
        fileService.save(new FileDTO(file.getOriginalFilename(), id, file.getBytes()));
        return "redirect:/post/" + id;
    }

    @GetMapping("/modify")
    public String getModifyPage() {
        return "/post/modify";
    }
}
