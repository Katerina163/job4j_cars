package ru.job4j.cars.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.AutoPostRepository;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.MarkRepository;

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
    private final CarRepository carRepository;
    private final String storageDirectory;
    private final MarkRepository markRepository;

    public SimpleAutoPostService(AutoPostRepository hiberAutoPostRepository,
                                 CarRepository hiberCarRepository,
                                 @Value("${file.directory}") String storageDirectory,
                                 MarkRepository hiberMarkRepository) {
        repository = hiberAutoPostRepository;
        carRepository = hiberCarRepository;
        this.storageDirectory = storageDirectory;
        markRepository = hiberMarkRepository;
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
        post.setAuthor(user);
        post.setCar(car);
        post.setSold(false);

        if (!file.isEmpty()) {
            var f = convertFile(file);
            post.addFile(f);
        }

        var priceHistory = new PriceHistory(
                Long.parseLong(params.get("price")),
                Long.parseLong(params.get("price")));
        post.addPriceHistory(priceHistory);

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
        var post = repository.findById(Long.parseLong(params.get("id")));
        if (post.isPresent()) {
            post.get().setDescription(params.get("description"));
            var car = convertCar(params);
            post.get().setCar(car);
            post.get().setAuthor(user);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            try {
                post.get().setCreated(format.parse(params.get("created")));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            repository.modify(post.get());
        }
    }

    private Car convertCar(Map<String, String> params) {
        var car = carRepository.findById(Long.parseLong(params.get("car.id"))).get();
        car.setName(params.get("car.name"));
        car.setColor(Color.valueOf(params.get("color")));
        car.setMark(markRepository.findById(Integer.parseInt(params.get("mark.id"))).get());
        car.setOwners(params.get("owners"));
        return car;
    }

    private Collection<Banner> convert(Collection<AutoPost> posts) {
        return posts.stream().map(Banner::new).toList();
    }
}
