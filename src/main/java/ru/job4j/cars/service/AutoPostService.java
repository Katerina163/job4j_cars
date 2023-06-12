package ru.job4j.cars.service;

import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface AutoPostService {
    Collection<Banner> findAll();

    Collection<Banner> findWithFile();

    Collection<Banner> findAllNew();

    Collection<Banner> findByCarBrand(String brand);

    Collection<Banner> findByColor(long id);

    Collection<Banner> findByMark(long id);

    Optional<AutoPost> findById(long id);

    void soldById(long postId, boolean sold);

    void delete(long id);

    void create(User user, Map<String, String> params, MultipartFile file);

    void modify(User user, Map<String, String> params);
}