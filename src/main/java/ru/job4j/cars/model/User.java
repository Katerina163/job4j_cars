package ru.job4j.cars.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String login;
    private String password;
}
