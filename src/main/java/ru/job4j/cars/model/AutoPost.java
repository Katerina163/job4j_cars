package ru.job4j.cars.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "auto_post")
@Data
public class AutoPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private Date created;

    @ManyToOne
    @JoinColumn(name = "auto_user_id")
    private User user;
}
