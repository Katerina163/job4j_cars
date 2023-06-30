package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.dto.Profile;
import ru.job4j.cars.mapper.Mapper;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class SimpleUserService implements UserService {
    private final UserRepository repository;
    private final Mapper<AutoPost, Banner> mapper;

    public SimpleUserService(UserRepository hiberUserRepository,
                             Mapper<AutoPost, Banner> autoPostBannerMapper) {
        repository = hiberUserRepository;
        mapper = autoPostBannerMapper;
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
        var result = new Profile(
                user.get().getUserPosts().stream().map(mapper::convert).toList(),
                user.get().getParticipates().stream().map(mapper::convert).toList());
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
