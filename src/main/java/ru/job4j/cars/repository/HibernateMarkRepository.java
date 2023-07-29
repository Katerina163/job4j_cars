package ru.job4j.cars.repository;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Mark;

import java.util.List;
import java.util.Optional;

import static ru.job4j.cars.model.QMark.mark;

@Slf4j
@Repository
@AllArgsConstructor
public class HibernateMarkRepository implements MarkRepository {
    private SessionFactory sf;

    @Override
    public List<Mark> findAll() {
        Transaction tr = null;
        List<Mark> list;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            list = new JPAQuery<Mark>(session)
                    .select(mark)
                    .from(mark)
                    .setHint(QueryHints.HINT_CACHEABLE, true)
                    .fetch();
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при поиске марок");
            throw e;
        }
        return list;
    }

    @Override
    public Optional<Mark> findById(long id) {
        Transaction tr = null;
        Mark result;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            result = session.find(Mark.class, id);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при поиске марки по id: {}", id);
            throw e;
        }
        return Optional.ofNullable(result);
    }
}
