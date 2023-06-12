package ru.job4j.cars.repository;

import ru.job4j.cars.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByLoginAndPassword(String login, String password);

    User create(User user);

    Optional<User> findByLogin(String login);

    void subscribe(long userId, long postId);

    void unsubscribe(long userId, long postId);
}
