package ru.job4j.cars.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @OneToOne
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    @OneToOne
    @JoinColumn(name = "mark_id", nullable = false)
    private Mark mark;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "history_owners",
            joinColumns = {@JoinColumn(name = "car_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "owner_id", nullable = false)})
    private Set<Owner> owners = new HashSet<>();
}