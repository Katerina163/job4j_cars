package ru.job4j.cars.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.AutoPostRepository;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.MarkRepository;
import ru.job4j.cars.repository.UserRepository;

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
    private final UserRepository userRepository;

    public SimpleAutoPostService(AutoPostRepository hiberAutoPostRepository,
                                 CarRepository hiberCarRepository,
                                 @Value("${file.directory}") String storageDirectory,
                                 MarkRepository hiberMarkRepository,
                                 UserRepository hiberUserRepository) {
        repository = hiberAutoPostRepository;
        carRepository = hiberCarRepository;
        this.storageDirectory = storageDirectory;
        markRepository = hiberMarkRepository;
        userRepository = hiberUserRepository;
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
    public Collection<Banner> findByColor(Color color) {
        return convert(repository.findByColor(color));
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

    @Transactional
    @Override
    public void delete(long id) {
        var post = repository.findById(id).get();
        var set = post.getFiles();
        for (var file : set) {
            deleteFile(file.getPath());
        }
        repository.delete(post);
    }

    private void deleteFile(String path) {
        try {
            Files.deleteIfExists(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public void create(String login, Map<String, String> params, MultipartFile file) {
        var car = new Car();
        convertCar(params, car);
        var post = new AutoPost();
        post.setDescription(params.get("description"));
        var user = userRepository.findByLogin(login).get();
        user.addUserPost(post);
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
        repository.save(post);
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

    @Transactional
    @Override
    public void modify(Map<String, String> params) {
        var post = repository.findById(Long.parseLong(params.get("id")));
        if (post.isPresent()) {
            post.get().setDescription(params.get("description"));

            var car = carRepository.findById(Long.parseLong(params.get("car.id"))).get();
            convertCar(params, car);

            post.get().setCar(car);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            try {
                post.get().setCreated(format.parse(params.get("created")));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            repository.save(post.get());
        }
    }

    private Car convertCar(Map<String, String> params, Car car) {
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
