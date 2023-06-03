package ru.job4j.cars.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.service.FileService;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService service;

    public FileController(FileService simpleFileService) {
        service = simpleFileService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        var contentOptional = service.findById(id);
        if (contentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contentOptional.get().getContent());
    }
}