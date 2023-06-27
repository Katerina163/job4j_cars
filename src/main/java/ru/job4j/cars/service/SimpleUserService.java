package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.dto.Profile;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class SimpleUserService implements UserService {
    private final UserRepository repository;

    public SimpleUserService(UserRepository hiberUserRepository) {
        repository = hiberUserRepository;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return repository.findByLoginAndPassword(login, password);
    }

    @Override
    public Optional<Profile> findAllPostsByLogin(String login) {
        var user = repository.findByLogin(login);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        var banner = new Banner();
        var result = new Profile(
                banner.convert(user.get().getUserPosts()),
                banner.convert(user.get().getParticipates())
        );
        return Optional.of(result);
    }

    @Override
    public User create(User user) {
        return repository.create(user);
    }

    @Override
    public void subscribe(long userId, long postId) {
        repository.participate(userId, postId, Set::add);
    }

    @Override
    public void unsubscribe(long userId, long postId) {
        repository.participate(userId, postId, Set::remove);
    }
}
