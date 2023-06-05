package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HiberUserRepository implements UserRepository {
    private final CrudRepository crudRepository;

    @Override
    public User create(User user) {
        crudRepository.run(session -> session.persist(user));
        return user;
    }

    @Override
    public void update(User user) {
        crudRepository.run(session -> session.merge(user));
    }

    @Override
    public void delete(int userId) {
        crudRepository.run(
                "delete from User where id = :fId",
                Map.of("fId", userId)
        );
    }

    @Override
    public List<User> findParticipatesById(int userId) {
        String s = "select distinct u from User u left join fetch u.participates as p"
                + " left join fetch p.car left join fetch p.files"
                + " where u.id = :fId";
        return crudRepository.query(s, User.class, Map.of("fId", userId));
    }

    @Override
    public List<User> findAllOrderById() {
        return crudRepository.query("from User order by id asc", User.class);
    }

    @Override
    public Optional<User> findById(int userId) {
        return crudRepository.optional(
                "from User where id = :fId", User.class,
                Map.of("fId", userId)
        );
    }

    @Override
    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                "from User where login like :fKey", User.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional(
                "from User where login = :fLogin and password = :fPassword",
                User.class, Map.of("fLogin", login, "fPassword", password)
        );
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                "from User u left join fetch u.participates where u.login = :fLogin",
                User.class, Map.of("fLogin", login)
        );
    }

    @Override
    public void addAutoPostByUserId(int userId, int postId) {
        crudRepository.runSql(
                "insert into participates(user_id, post_id) values(:fUserId, :fPostId)",
                Map.of("fUserId", userId, "fPostId", postId));
    }

    @Override
    public void deleteAutoPostByUserId(int userId, int postId) {
        crudRepository.runSql(
                "delete from participates where post_id = :fPostId and user_id = :fUserId",
                Map.of("fUserId", userId, "fPostId", postId));
    }
}