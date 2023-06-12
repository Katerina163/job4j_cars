package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Color;

import java.util.List;

@Repository
@AllArgsConstructor
public class HiberColorRepository implements ColorRepository {
    private final CrudRepository crud;

    @Override
    public List<Color> findAll() {
        return crud.query("from Color", Color.class);
    }
}
