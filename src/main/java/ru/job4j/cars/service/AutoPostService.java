package ru.job4j.cars.service;

import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.dto.Criterion;
import ru.job4j.cars.model.AutoPost;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface AutoPostService {
    Collection<Banner> search(Criterion criterion);

    Optional<AutoPost> findById(long id);

    void soldById(long postId, boolean sold);

    void delete(long id);

    void save(String login, Map<String, String> params, MultipartFile file);

    void modify(Map<String, String> params);
}
