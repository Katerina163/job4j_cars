package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Mark;
import ru.job4j.cars.repository.MarkRepository;

import java.util.List;

@Service
public class SimpleMarkService implements MarkService {
    private final MarkRepository repository;

    public SimpleMarkService(MarkRepository hiberMarkRepository) {
        repository = hiberMarkRepository;
    }

    @Override
    public List<Mark> findAll() {
        return repository.findAll();
    }
}
