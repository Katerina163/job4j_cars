package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.File;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HiberFileRepository implements FileRepository {
    private CrudRepository crud;

    @Override
    public Optional<File> findById(long id) {
        return crud.optional(
                "select distinct f from File f where f.id = :fId", File.class, Map.of("fId", id));
    }

    @Override
    public File create(File file) {
        crud.run(session -> session.save(file));
        return file;
    }

    @Override
    public void delete(File file) {
        crud.run(session -> session.delete(file));
    }
}