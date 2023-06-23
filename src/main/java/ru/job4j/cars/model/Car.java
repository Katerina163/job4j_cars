package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Car extends BaseId<Long> {

    private String name;

    private String owners;

    @Enumerated(EnumType.STRING)
    private Color color;

    @OneToOne
    @JoinColumn(name = "mark_id", updatable = false)
    private Mark mark;
}