package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDTO {
    private String name;
    private int postId;
    private byte[] content;
}