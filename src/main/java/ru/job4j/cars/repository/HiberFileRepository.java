package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.File;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HiberFileRepository implements FileRepository {
    private CrudRepository crudRepository;

    @Override
    public Optional<File> findById(int id) {
        return crudRepository.optional(
                "from File where id = :fId", File.class, Map.of("fId", id));
    }

    @Override
    public File create(File file) {
        crudRepository.run(session -> session.save(file));
        return file;
    }

    @Override
    public void update(File file) {
        crudRepository.run(session -> session.update(file));
    }

    @Override
    public void delete(int id) {
        crudRepository.run(
                "delete from File where id = :fId",
                Map.of("fId", id)
        );
    }
}