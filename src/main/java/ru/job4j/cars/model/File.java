package ru.job4j.cars.model;

import lombok.*;

import javax.persistence.Entity;

import javax.persistence.*;

@Entity
@Table(name = "files")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String name;

    private String path;

    public File(String name, String path) {
        this.name = name;
        this.path = path;
    }
}
