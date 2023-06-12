package ru.job4j.cars.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.AutoPostRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SimpleAutoPostService implements AutoPostService {
    private final AutoPostRepository repository;
    private final String storageDirectory;

    public SimpleAutoPostService(AutoPostRepository hiberAutoPostRepository,
                                 @Value("${file.directory}") String storageDirectory) {
        repository = hiberAutoPostRepository;
        this.storageDirectory = storageDirectory;
    }

    @Override
    public Collection<Banner> findAll() {
        return convert(repository.findAll());
    }

    @Override
    public Collection<Banner> findWithFile() {
        return convert(repository.findWithFile());
    }

    @Override
    public Collection<Banner> findAllNew() {
        return convert(repository.findAllNew());
    }

    @Override
    public Collection<Banner> findByCarBrand(String brand) {
        return convert(repository.findByCarBrand(brand));
    }

    @Override
    public Collection<Banner> findByColor(long id) {
        return convert(repository.findByColor(id));
    }

    @Override
    public Collection<Banner> findByMark(long id) {
        return convert(repository.findByMark(id));
    }

    @Override
    public Optional<AutoPost> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public void soldById(long postId, boolean sold) {
        repository.soldById(postId, sold);
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }

    @Override
    public void create(User user, Map<String, String> params, MultipartFile file) {
        var car = convertCar(params);

        var post = new AutoPost();
        post.setDescription(params.get("description"));
        post.setCreated(new Date());
        post.setUser(user);
        post.setCar(car);
        post.setSold(false);

        if (!file.isEmpty()) {
            var f = convertFile(file);
            f.setPost(post);
            post.getFiles().add(f);
        }

        var priceHistory = new PriceHistory();
        priceHistory.setCreated(new Date());
        priceHistory.setBefore(Long.parseLong(params.get("price")));
        priceHistory.setAfter(Long.parseLong(params.get("price")));
        priceHistory.setPost(post);
        post.getHistory().add(priceHistory);

        repository.create(post);
    }

    private File convertFile(MultipartFile file) {
        var path = getNewFilePath(file.getOriginalFilename());
        try {
            Files.write(Path.of(path), file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new File(file.getOriginalFilename(), path);
    }

    private String getNewFilePath(String sourceName) {
        return storageDirectory + java.io.File.separator + UUID.randomUUID() + sourceName;
    }

    @Override
    public void modify(User user, Map<String, String> params) {
        var post = new AutoPost(Long.parseLong(params.get("id")));
        post.setDescription(params.get("description"));
        var car = convertCar(params);
        post.setCar(car);
        post.setUser(user);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            post.setCreated(format.parse(params.get("created")));
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        repository.modify(post);
    }

    private Car convertCar(Map<String, String> params) {
        var car = new Car();
        if (params.get("car.id") != null && !params.get("car.id").isEmpty()) {
            car.setId(Long.parseLong(params.get("car.id")));
        }
        car.setName(params.get("car.name"));
        car.setColor(new Color(Long.parseLong(params.get("color.id"))));
        car.setMark(new Mark(Long.parseLong(params.get("mark.id"))));
        if (params.get("owners") != null && !params.get("owners").isEmpty()) {
            String[] ownersNames = params.get("owners").split(", ");
            for (var name : ownersNames) {
                var owner = new Owner(name);
                car.getOwners().add(owner);
            }
        }
        return car;
    }

    private Collection<Banner> convert(Collection<AutoPost> posts) {
        return posts.stream().map(Banner::new).toList();
    }
}
