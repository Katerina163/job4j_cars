package ru.job4j.cars.service;

import com.querydsl.core.Tuple;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.dto.Profile;
import ru.job4j.cars.mapper.Mapper;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleUserService implements UserService {
    private final UserRepository repository;
    private final Mapper<Tuple, Banner> mapper;

    public SimpleUserService(UserRepository hiberUserRepository,
                             Mapper<Tuple, Banner> tupleBannerMapper) {
        repository = hiberUserRepository;
        mapper = tupleBannerMapper;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return repository.findByLoginAndPassword(login, password);
    }

    @Override
    public Optional<Profile> findAllPostsByLogin(String login) {
        var usersPosts = repository.findUsersPostByLogin(login);
        var usersParticipates = repository.findParticipatesByLogin(login);
        var result = new Profile();
        if (usersPosts != null) {
            result.setUserPosts(usersPosts.stream().map(mapper::convert).toList());
        }
        if (usersParticipates != null) {
            result.setUserSubscribe(usersParticipates.stream().map(mapper::convert).toList());
        }
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
