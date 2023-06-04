package ru.job4j.cars.service;

import ru.job4j.cars.model.AutoPost;

import java.util.Collection;
import java.util.Optional;

public interface PostService {
    Collection<AutoPost> findAllNew();

    Collection<AutoPost> findAll();

    Collection<AutoPost> findWithFile();

    Collection<AutoPost> findCarBrand(String brand);

    Collection<AutoPost> findUsersCar(String login);

    void add(AutoPost post);

    void delete(AutoPost post);

    Optional<AutoPost> findById(int id);

    void update(AutoPost post);

    void soldById(int id, boolean sold);
}
