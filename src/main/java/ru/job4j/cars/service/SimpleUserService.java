package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.AutoPostRepository;
import ru.job4j.cars.repository.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

@Service
public class SimpleUserService implements UserService {
    private final UserRepository repository;
    private final AutoPostRepository postRepository;

    public SimpleUserService(UserRepository hiberUserRepository, AutoPostRepository hiberAutoPostRepository) {
        repository = hiberUserRepository;
        postRepository = hiberAutoPostRepository;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return repository.findByLoginAndPassword(login, password);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login);
    }

    @Override
    public User create(User user) {
        return repository.create(user);
    }

    @Override
    public void subscribe(String login, long postId) {
        participate(login, postId, Set::add);
    }

    private void participate(String login, long postId, BiFunction<Set<AutoPost>, AutoPost, Boolean> function) {
        var user = repository.findByLogin(login).get();
        var post = postRepository.findById(postId).get();
        function.apply(user.getParticipates(), post);
    }

    @Override
    public void unsubscribe(String login, long postId) {
        participate(login, postId, Set::remove);
    }
}
