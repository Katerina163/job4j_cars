package ru.job4j.cars.repository;

import ru.job4j.cars.model.AutoPost;

import java.util.Collection;
import java.util.Optional;

public interface AutoPostRepository {
    Collection<AutoPost> findAll();

    Collection<AutoPost> findWithFile();

    Collection<AutoPost> findAllNew();

    Collection<AutoPost> findByCarBrand(String brand);

    Collection<AutoPost> findByColor(long id);

    Collection<AutoPost> findByMark(long id);

    Optional<AutoPost> findById(long id);

    void create(AutoPost post);

    void soldById(long postId, boolean sold);

    void delete(long id);

    void modify(AutoPost post);
}
