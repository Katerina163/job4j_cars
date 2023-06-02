package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleUserService implements UserService {
    private UserRepository repository;

    public SimpleUserService(UserRepository hiberUserRepository) {
        repository = hiberUserRepository;
    }

    @Override
    public User create(User user) {
        return repository.create(user);
    }

    @Override
    public void update(User user) {
        repository.update(user);
    }

    @Override
    public void delete(int userId) {
        repository.delete(userId);
    }

    @Override
    public List<User> findAllOrderById() {
        return repository.findAllOrderById();
    }

    @Override
    public Optional<User> findById(int userId) {
        return repository.findById(userId);
    }

    @Override
    public List<User> findByLikeLogin(String key) {
        return repository.findByLikeLogin(key);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login);
    }
}
