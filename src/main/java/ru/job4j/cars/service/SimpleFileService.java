package ru.job4j.cars.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.FileDTO;
import ru.job4j.cars.model.File;
import ru.job4j.cars.repository.AutoPostRepository;
import ru.job4j.cars.repository.FileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Service
public class SimpleFileService implements FileService {
    private final AutoPostRepository postRepository;
    private final FileRepository repository;
    private final String storageDirectory;

    public SimpleFileService(AutoPostRepository hiberAutoPostRepository,
                             FileRepository simpleFileRepository,
                             @Value("${file.directory}") String storageDirectory) {
        postRepository = hiberAutoPostRepository;
        repository = simpleFileRepository;
        this.storageDirectory = storageDirectory;
        createStorageDirectory(storageDirectory);
    }

    private void createStorageDirectory(String path) {
        try {
            Files.createDirectories(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FileDTO> findById(long id) {
        var fileOptional = repository.findById(id);
        if (fileOptional.isEmpty()) {
            return Optional.empty();
        }
        var content = readFileAsBytes(fileOptional.get().getPath());
        return Optional.of(new FileDTO(
                fileOptional.get().getName(), fileOptional.get().getPost().getId(), content));
    }

    private byte[] readFileAsBytes(String path) {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<File> save(FileDTO fileDto) {
        var path = getNewFilePath(fileDto.getName());
        writeFileBytes(path, fileDto.getContent());
        var file = new File();
        file.setName(fileDto.getName());
        file.setPath(path);
        var post = postRepository.findById(fileDto.getPostId());
        if (post.isEmpty()) {
            return Optional.empty();
        }
        post.get().addFile(file);
        return Optional.of(repository.create(file));
    }

    private String getNewFilePath(String sourceName) {
        return storageDirectory + java.io.File.separator + UUID.randomUUID() + sourceName;
    }

    private void writeFileBytes(String path, byte[] content) {
        try {
            Files.write(Path.of(path), content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(long id) {
        var fileOptional = repository.findById(id);
        if (fileOptional.isPresent()) {
            deleteFile(fileOptional.get().getPath());
            repository.delete(id);
        }
    }

    private void deleteFile(String path) {
        try {
            Files.deleteIfExists(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
