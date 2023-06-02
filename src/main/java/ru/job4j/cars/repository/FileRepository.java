package ru.job4j.cars.repository;

import ru.job4j.cars.model.File;

import java.util.Optional;

public interface FileRepository {
    Optional<File> findById(int id);

    File create(File file);

    void update(File file);

    void delete(int id);
}
