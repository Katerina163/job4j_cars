package ru.job4j.cars.repository;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.User;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

import static ru.job4j.cars.model.QUser.user;

@Slf4j
@AllArgsConstructor
@Repository
public class HiberUserRepository implements UserRepository {
    private final SessionFactory sf;

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Transaction tr = null;
        User result;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            result = new JPAQuery<User>(session)
                            .select(user)
                            .from(user)
                            .where(user.login.eq(login).and(user.password.eq(password)))
                            .fetchOne();
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при поиске пользователя с логином {} и паролем {}", login, password);
            throw e;
        }
        return Optional.ofNullable(result);
    }

    @Override
    public User create(User user) {
        Transaction tr = null;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            session.persist(user);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при сохранении пользователя: {}", user);
            throw e;
        }
        return user;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        Transaction tr = null;
        Optional<User> result;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            var graph = session.getEntityGraph("profile");
            result = session.createQuery("from User where login = :login", User.class)
                    .setParameter("login", login)
                    .setHint(GraphSemantic.LOAD.getJpaHintName(), graph)
                    .uniqueResultOptional();
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при попытке найти пользователя с логином {}", login);
            throw e;
        }

        return result;
    }

    @Override
    public void participate(long userId, long postId, BiFunction<Set<AutoPost>, AutoPost, Boolean> function) {
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();
            var user = session.find(User.class, userId);
            var post = session.find(AutoPost.class, postId);
            function.apply(user.getParticipates(), post);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("Ошибка при попытке подписки/отписки пользователя с id {} поста с id {}", userId, postId);
            throw e;
        }
    }
}
