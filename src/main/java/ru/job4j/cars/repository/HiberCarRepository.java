package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HiberCarRepository implements CarRepository {
    private CrudRepository crud;

    @Override
    public Optional<Car> findById(long id) {
        return crud.optional("from Car where id = :id", Car.class, Map.of("id", id));
    }
}
