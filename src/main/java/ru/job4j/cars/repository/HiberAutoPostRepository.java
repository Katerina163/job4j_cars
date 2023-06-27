package ru.job4j.cars.repository;

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

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static ru.job4j.cars.model.QAutoPost.autoPost;

@Slf4j
@Repository
@AllArgsConstructor
public class HiberAutoPostRepository implements AutoPostRepository {
    private final SessionFactory sf;

    @Override
    public Collection<AutoPost> findWithPredicate(Predicate predicate) {
        Transaction tr = null;
        Collection<AutoPost> result;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            result = new JPAQuery<>(session)
                    .setHint(GraphSemantic.LOAD.getJpaHintName(), session.getEntityGraph("All"))
                    .select(autoPost)
                    .from(autoPost)
                    .where(predicate)
                    .fetch();
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при поиске постов по условию {}", predicate);
            throw e;
        }
        return result;
    }

    @Override
    public Optional<AutoPost> findById(long id) {
        Transaction tr = null;
        AutoPost result;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            Map<String, Object> map = Map.of(GraphSemantic.LOAD.getJpaHintName(), session.getEntityGraph("All"));
            result = session.find(AutoPost.class, id, map);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при поиске постa по id {}", id);
            throw e;
        }
        return Optional.ofNullable(result);
    }

    @Override
    public void soldById(long postId) {
        Transaction tr = null;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            var post = session.find(AutoPost.class, postId);
            post.setSold(!post.isSold());
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при изменении sold поста с id {}", postId);
            throw e;
        }
    }

    @Override
    public void cud(AutoPost post, Consumer<Session> function) {
        Transaction tr = null;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            function.accept(session);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при операции с постом {}", post);
            throw e;
        }
    }
}
