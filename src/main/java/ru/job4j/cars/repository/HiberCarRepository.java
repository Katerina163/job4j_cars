package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HiberCarRepository implements CarRepository {
    private final SessionFactory sf;

    @Override
    public Optional<Car> findById(long id) {
        Transaction tr = null;
        Car result;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            Map<String, Object> map = Map.of(GraphSemantic.LOAD.getJpaHintName(), session.getEntityGraph("CarWithMark"));
            result = session.find(Car.class, id, map);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при поиске машины c id: {}", id);
            throw e;
        }
        return Optional.ofNullable(result);
    }
}
