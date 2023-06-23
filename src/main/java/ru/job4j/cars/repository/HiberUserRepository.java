package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.User;

import java.util.*;

@AllArgsConstructor
@Repository
public class HiberUserRepository implements UserRepository {
    private final CrudRepository crud;

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crud.optional(
                "from User where login = :login and password = :password",
                User.class, Map.of("login", login, "password", password));
    }

    @Override
    public User create(User user) {
        crud.run(session -> session.persist(user));
        return user;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        String sql = "from User u left join fetch u.userPosts post left join fetch u.participates part"
                + " left join fetch post.history left join fetch post.files"
                + " left join fetch part.history left join fetch part.files"
                + " where login = :login";
        return crud.optional(sql, User.class, Map.of("login", login));
    }
}
