package ru.job4j.cars.service;

import ru.job4j.cars.dto.FileDTO;
import ru.job4j.cars.model.File;

import java.util.Optional;

public interface FileService {
    Optional<FileDTO> findById(long id);

    Optional<File> save(FileDTO fileDto);

    void deleteById(long id);
}
