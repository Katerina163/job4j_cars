package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String owners;

    @Enumerated(EnumType.STRING)
    private Color color;

    @OneToOne
    @JoinColumn(name = "mark_id", updatable = false)
    private Mark mark;
}