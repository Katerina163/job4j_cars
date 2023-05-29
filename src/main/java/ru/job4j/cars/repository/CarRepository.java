package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class CarRepository {
    private CrudRepository crudRepository;

    public Optional<Car> findById(int id) {
        return crudRepository.optional(
                "from Car c left join fetch c.owners where c.id = :fId",
                Car.class, Map.of("fId", id));
    }

    public Car create(Car car) {
        crudRepository.run(session -> session.save(car));
        return car;
    }

    public void update(Car car) {
        crudRepository.run(session -> session.merge(car));
    }

    public void delete(int id) {
        crudRepository.run(
                "delete from Car where id = :fId",
                Map.of("fId", id)
        );
    }
}
