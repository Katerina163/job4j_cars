package ru.job4j.cars.service;

import com.querydsl.core.Tuple;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.dto.Profile;
import ru.job4j.cars.mapper.CompositeMapper;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleUserService implements UserService {
    private final UserRepository repository;
    private final CompositeMapper mapper;

    public SimpleUserService(UserRepository hiberUserRepository,
                             CompositeMapper mapper) {
        repository = hiberUserRepository;
        this.mapper = mapper;
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
        var tupleBannerMapper = mapper.mapper(Tuple.class, Banner.class);
        if (usersPosts != null) {
            result.setUserPosts(usersPosts.stream().map(
                    post -> (Banner) tupleBannerMapper.convert(post))
                    .toList());
        }
        if (usersParticipates != null) {
            result.setUserSubscribe(usersParticipates.stream().map(
                    post -> (Banner) tupleBannerMapper.convert(post))
            .toList());
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
