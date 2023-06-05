package ru.job4j.cars.service;

import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user);

    void update(User user);

    void delete(int userId);

    List<User> findAllOrderById();

    List<User> findParticipatesById(int userId);

    void addAutoPostByUserId(int userId, int postId);

    void deleteAutoPostByUserId(int userId, int postId);

    Optional<User> findById(int userId);

    List<User> findByLikeLogin(String key);

    Optional<User> findByLogin(String login);

    Optional<User> findByLoginAndPassword(String login, String password);
}
