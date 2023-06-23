package ru.job4j.cars.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    private String name;

    private String owners;

    @Enumerated(EnumType.STRING)
    private Color color;

    @OneToOne
    @JoinColumn(name = "mark_id", nullable = false, insertable = false, updatable = false)
    private Mark mark;
}