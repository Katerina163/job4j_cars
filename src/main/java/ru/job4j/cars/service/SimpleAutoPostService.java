package ru.job4j.cars.service;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.dto.Criterion;
import ru.job4j.cars.dto.QPredicate;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.AutoPostRepository;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.MarkRepository;
import ru.job4j.cars.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

import static ru.job4j.cars.model.QAutoPost.autoPost;

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
    public Collection<Banner> search(Criterion criterion, Function<QPredicate, Predicate> function) {
        var banner = new Banner();
        var predicate = QPredicate.builder();
        if (criterion.isFindAll()) {
            return banner.convert(repository.findWithPredicate(predicate.and()));
        }
        if (criterion.isWithFile()) {
            predicate.addPredicate(1, autoPost.files.size()::goe);
        }
        if (criterion.isFresh()) {
            predicate.addBiPredicate(LocalDateTime.now().minusDays(1L), LocalDateTime.now(), autoPost.created::between);
        }
        if (criterion.getBrand() != null && !criterion.getBrand().isEmpty()) {
            predicate.addPredicate(criterion.getBrand(), autoPost.car.name::eq);
        }
        if (!criterion.getColors().isEmpty()) {
            for (var color : criterion.getColors()) {
                if (color == null) {
                    break;
                }
                predicate.addPredicate(color, autoPost.car.color::eq);
            }
        }
        if (!criterion.getMarkIds().isEmpty()) {
            for (var markId : criterion.getMarkIds()) {
                if (markId == null || markId == 0) {
                    break;
                }
                predicate.addPredicate(markId, autoPost.car.mark.id::eq);
            }
        }
        return banner.convert(repository.findWithPredicate(function.apply(predicate)));
    }

    @Override
    public Collection<Banner> search(Map<String, String> param, List<String> markIds, List<String> colors,
                                     Function<QPredicate, Predicate> supplier) {
        var criterion = new Criterion();
        if (markIds != null && !markIds.isEmpty()) {
            for (var markId : markIds) {
                criterion.addMarkIds(Long.parseLong(markId));
            }
        }
        if (colors != null && !colors.isEmpty()) {
            for (var color : colors) {
                criterion.addColor(Color.valueOf(color));
            }
        }
        if (param.containsKey("brand")) {
            criterion.addBrand(param.get("brand"));
        }
        if (param.containsKey("fresh") && param.get("fresh").equals("on")) {
            criterion.fresh();
        }
        return search(criterion, supplier);
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
        repository.cud(post, session -> session.delete(post));
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
    public void save(String login, Map<String, String> params, MultipartFile file) {
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
                Long.parseLong(params.get("price")));
        post.addPriceHistory(priceHistory);
        repository.cud(post, session -> session.persist(post));
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

//            var format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//            post.get().setCreated(format.parse(params.get("created")));
            repository.cud(post.get(), session -> session.update(post));
        }
    }

    private Car convertCar(Map<String, String> params, Car car) {
        car.setName(params.get("car.name"));
        car.setColor(Color.valueOf(params.get("color")));
        car.setMark(markRepository.findById(Integer.parseInt(params.get("mark.id"))).get());
        car.setOwners(params.get("owners"));
        return car;
    }
}
