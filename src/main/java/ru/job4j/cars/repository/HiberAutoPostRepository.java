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

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static ru.job4j.cars.model.QAutoPost.autoPost;
import static ru.job4j.cars.model.QCar.car;
import static ru.job4j.cars.model.QFile.file;
import static ru.job4j.cars.model.QMark.mark;
import static ru.job4j.cars.model.QPriceHistory.priceHistory;

@Slf4j
@Repository
@AllArgsConstructor
public class HiberAutoPostRepository implements AutoPostRepository {
    private final SessionFactory sf;

    @Override
    public Collection<Tuple> findWithPredicate(Predicate predicate, Long limit, Long offset) {
        Transaction tr = null;
        Collection<Tuple> result;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            session.setDefaultReadOnly(true);
            result = new JPAQuery<Tuple>(session)
                    .select(
                            autoPost.id,
                            autoPost.created,
                            car.name,
                            car.mark.name,
                            priceHistory.price,
                            file.id
                    )
                    .from(autoPost)
                    .innerJoin(priceHistory).on(priceHistory.id.eq(
                            new JPAQuery<>()
                                    .select(priceHistory.id.max())
                                    .from(priceHistory)
                                    .where(priceHistory.post.id.eq(autoPost.id))
                    ))
                    .leftJoin(file).on(file.id.eq(
                            new JPAQuery<>()
                                    .select(file.id.max())
                                    .from(file)
                                    .where(file.post.id.eq(autoPost.id))
                    ))
                    .innerJoin(car).on(autoPost.car.id.eq(car.id))
                    .innerJoin(mark).on(autoPost.car.mark.id.eq(mark.id))
                    .where(predicate)
                    .limit(limit)
                    .offset(offset)
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
            session.setDefaultReadOnly(true);
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
    public void soldById(long postId, boolean sold) {
        Transaction tr = null;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            var post = session.find(AutoPost.class, postId);
            post.setSold(sold);
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
