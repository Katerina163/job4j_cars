package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Mark;

import java.util.List;

@Repository
@AllArgsConstructor
public class HiberMarkRepository implements MarkRepository {
    private final CrudRepository crud;

    @Override
    public List<Mark> findAll() {
        return crud.query("from Mark", Mark.class);
    }
}
