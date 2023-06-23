package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Mark;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HiberMarkRepository implements MarkRepository {
    private final CrudRepository crud;

    @Override
    public List<Mark> findAll() {
        return crud.query("from Mark", Mark.class);
    }

    @Override
    public Optional<Mark> findById(int id) {
        return crud.optional("from Mark where id = :id", Mark.class, Map.of("id", id));
    }
}
