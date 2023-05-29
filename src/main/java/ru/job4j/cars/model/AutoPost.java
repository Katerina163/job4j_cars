package ru.job4j.cars.model;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "auto_post")
@Data
public class AutoPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private Date created;

    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "auto_user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "auto_post_id")
    private List<PriceHistory> priceHistories = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "participates",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private List<User> participates = new ArrayList<>();

    @OneToMany(cascade =  CascadeType.ALL)
    @JoinColumn(name = "auto_post_id")
    private Set<File> files = new HashSet<>();
}
