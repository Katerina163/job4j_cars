package ru.job4j.cars.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

import javax.persistence.*;

@Entity
@Table(name = "files")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String name;

    private String path;

    @ManyToOne
    @JoinColumn(name = "auto_post_id")
    private AutoPost autoPost;
}
