package ru.job4j.cars.service;

import ru.job4j.cars.model.Car;

import java.util.Optional;

public interface CarService {
    Optional<Car> findById(int id);

    Car create(Car car);

    void update(Car car);

    void delete(int id);
}
