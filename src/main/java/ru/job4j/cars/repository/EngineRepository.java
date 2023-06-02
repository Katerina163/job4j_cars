package ru.job4j.cars.repository;

import ru.job4j.cars.model.Engine;

import java.util.Optional;

public interface EngineRepository {
    Optional<Engine> findById(int id);

    Engine create(Engine engine);

    void update(Engine engine);

    void delete(int id);
}
