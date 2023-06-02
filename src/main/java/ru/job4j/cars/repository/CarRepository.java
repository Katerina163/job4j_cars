package ru.job4j.cars.repository;

import ru.job4j.cars.model.Car;

import java.util.Optional;

public interface CarRepository {
    Optional<Car> findById(int id);

    Car create(Car car);

    void update(Car car);

    void delete(int id);
}
