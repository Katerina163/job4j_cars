package ru.job4j.cars.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Color {
    BLACK("Черный"),
    WHITE("Белый"),
    BROWN("Коричневый"),
    GREEN("Зеленый"),
    YELLOW("Желтый"),
    RED("Красный"),
    BLUE("Синий");

    private final String name;
}
