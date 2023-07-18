package ru.job4j.cars.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static ru.job4j.cars.model.QAutoPost.autoPost;
import static ru.job4j.cars.model.QCar.car;
import static ru.job4j.cars.model.QFile.file;
import static ru.job4j.cars.model.QMark.mark;
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
    public Optional<User> create(User user) {
        Transaction tr = null;
        Optional<User> result = Optional.empty();
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            var findUser = session.createQuery("from User where login = :login", User.class)
                    .setParameter("login", user.getLogin())
                    .uniqueResultOptional();
            if (findUser.isEmpty()) {
                session.persist(user);
                result = Optional.of(user);
            }
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при сохранении пользователя: {}", user);
            throw e;
        }
        return result;
    }

    @Override
    public Optional<User> findByLoginWithPosts(String login) {
        Transaction tr = null;
        Optional<User> result;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            result = session.createQuery("from User where login = :login", User.class)
                    .setParameter("login", login)
                    .setHint(GraphSemantic.LOAD.getJpaHintName(), session.getEntityGraph("withUserPosts"))
                    .uniqueResultOptional();
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при поиске пользователя с логином: {}", login);
            throw e;
        }
        return result;
    }

    @Override
    public Collection<Tuple> findParticipatesByLogin(String login) {
        return findByLogin(autoPost.id.in(
                new JPAQuery<>()
                        .select(autoPost.id)
                        .from(user)
                        .leftJoin(autoPost)
                        .on(user.participates.contains(autoPost))
                        .where(user.login.eq(login))
        ));
    }

    @Override
    public Collection<Tuple> findUsersPostByLogin(String login) {
        return findByLogin(autoPost.author.login.eq(login));
    }

    private Collection<Tuple> findByLogin(Predicate predicate) {
        Transaction tr = null;
        Collection<Tuple> result;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            result = new JPAQuery<AutoPost>(session)
                    .distinct()
                    .select(
                            autoPost.id,
                            autoPost.created,
                            car.name,
                            car.mark.name,
                            file.id
                    )
                    .from(autoPost)
                    .leftJoin(file).on(file.id.eq(
                            new JPAQuery<>()
                                    .select(file.id.max())
                                    .from(file)
                                    .where(file.post.id.eq(autoPost.id))
                    ))
                    .innerJoin(car).on(autoPost.car.id.eq(car.id))
                    .innerJoin(mark).on(autoPost.car.mark.id.eq(mark.id))
                    .where(predicate)
                    .fetch();
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при попытке найти пользователя по условию {}", predicate);
            throw e;
        }
        return result;
    }

    @Override
    public void participate(long userId, long postId, BiFunction<List<AutoPost>, AutoPost, Boolean> function) {
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
