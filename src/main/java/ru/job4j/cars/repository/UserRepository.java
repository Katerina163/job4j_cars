package ru.job4j.cars.repository;

import com.querydsl.core.Tuple;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public interface UserRepository {
    Optional<User> findByLoginAndPassword(String login, String password);

    Optional<User> create(User user);

    Collection<Tuple> findParticipatesByLogin(String login);

    Collection<Tuple> findUsersPostByLogin(String login);

    void participate(long userId, long postId, BiFunction<List<AutoPost>, AutoPost, Boolean> function);

    Optional<User> findByLoginWithPosts(String login);
}
