package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.EngineRepository;

import java.util.Optional;

@Service
public class SimpleEngineService implements EngineService {
    private EngineRepository repository;

    public SimpleEngineService(EngineRepository simpleEngineRepository) {
        repository = simpleEngineRepository;
    }

    @Override
    public Optional<Engine> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public Engine create(Engine engine) {
        return repository.create(engine);
    }

    @Override
    public void update(Engine engine) {
        repository.update(engine);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }
}
