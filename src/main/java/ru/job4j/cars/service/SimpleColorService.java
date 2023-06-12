package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Color;
import ru.job4j.cars.repository.ColorRepository;

import java.util.List;

@Service
public class SimpleColorService implements ColorService {
    private final ColorRepository repository;

    public SimpleColorService(ColorRepository hiberColorRepository) {
        repository = hiberColorRepository;
    }

    @Override
    public List<Color> findAll() {
        return repository.findAll();
    }
}
