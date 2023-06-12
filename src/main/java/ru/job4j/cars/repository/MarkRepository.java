package ru.job4j.cars.repository;

import ru.job4j.cars.model.Mark;

import java.util.List;

public interface MarkRepository {
    List<Mark> findAll();
}
