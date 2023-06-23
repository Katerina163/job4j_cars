package ru.job4j.cars.service;

import ru.job4j.cars.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByLoginAndPassword(String login, String password);

    User create(User user);

    Optional<User> findByLogin(String login);

    void subscribe(String login, long postId);

    void unsubscribe(String login, long postId);
}