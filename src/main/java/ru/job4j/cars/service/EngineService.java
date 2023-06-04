package ru.job4j.cars.service;

import ru.job4j.cars.model.Engine;

import java.util.Optional;

public interface EngineService {
    Optional<Engine> findById(int id);

    Engine create(Engine engine);

    void update(Engine engine);

    void delete(int id);
}
