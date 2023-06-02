package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HiberEngineRepository implements EngineRepository {
    private CrudRepository crudRepository;

    @Override
    public Optional<Engine> findById(int id) {
        return crudRepository.optional(
                "from Engine where id = :fId", Engine.class, Map.of("fId", id));
    }

    @Override
    public Engine create(Engine engine) {
        crudRepository.run(session -> session.save(engine));
        return engine;
    }

    @Override
    public void update(Engine engine) {
        crudRepository.run(session -> session.merge(engine));
    }

    @Override
    public void delete(int id) {
        crudRepository.run(
                "delete from Engine where id = :fId",
                Map.of("fId", id)
        );
    }
}
