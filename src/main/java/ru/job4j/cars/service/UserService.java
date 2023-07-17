package ru.job4j.cars.service;

import ru.job4j.cars.dto.Profile;
import ru.job4j.cars.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByLoginAndPassword(String login, String password);

    Optional<User> create(User user);

    Optional<Profile> findAllPostsByLogin(String login);

    void subscribe(long userId, long postId);

    void unsubscribe(long userId, long postId);
}