package ru.job4j.cars.repository;

import ru.job4j.cars.model.Color;

import java.util.List;

public interface ColorRepository {
    List<Color> findAll();
}
