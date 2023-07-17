package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.dto.Profile;
import ru.job4j.cars.mapper.Mapper;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.UserRepository;

import java.util.List;
import java.util.Optional;

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
        var usersPosts = repository.findByLoginUsersPost(login);
        var usersParticipates = repository.findByLoginParticipates(login);
        if (usersPosts.isEmpty() || usersParticipates.isEmpty()) {
            return Optional.empty();
        }
        var result = new Profile(
                usersPosts.get().getUserPosts().stream().map(mapper::convert).toList(),
                usersParticipates.get().getParticipates().stream().map(mapper::convert).toList());
        return Optional.of(result);
    }

    @Override
    public Optional<User> create(User user) {
        return repository.create(user);
    }

    @Override
    public void subscribe(long userId, long postId) {
        repository.participate(userId, postId, List::add);
    }

    @Override
    public void unsubscribe(long userId, long postId) {
        repository.participate(userId, postId, List::remove);
    }
}
