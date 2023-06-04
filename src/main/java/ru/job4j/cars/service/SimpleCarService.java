package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.CarRepository;

import java.util.Optional;

@Service
public class SimpleCarService implements CarService {
    private CarRepository repository;

    public SimpleCarService(CarRepository simpleCarRepository) {
        repository = simpleCarRepository;
    }

    @Override
    public Optional<Car> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public Car create(Car car) {
        return repository.create(car);
    }

    @Override
    public void update(Car car) {
        repository.update(car);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }
}
