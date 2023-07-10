package ru.job4j.cars.service;

import com.querydsl.core.types.Predicate;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.*;
import ru.job4j.cars.model.AutoPost;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface AutoPostService {
    Collection<Banner> search(Criterion criterion, Function<QPredicate, Predicate> supplier);

    Optional<AutoPost> findById(long id);

    void soldById(long postId, boolean sold);

    void delete(long id);

    void save(String login, PostCreateDTO dto, MultipartFile file);

    void modify(PostModifyDTO dto);
}
