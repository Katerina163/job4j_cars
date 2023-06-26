package ru.job4j.cars.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.job4j.cars.model.Color;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class Criterion {
    private boolean findAll = false;
    private boolean withFile = false;
    private boolean fresh = false;
    private String brand;
    private List<Color> colors = new ArrayList<>(Color.values().length - 1);
    private List<Long> markId = new ArrayList<>();

    public Criterion findAll() {
        this.findAll = true;
        return this;
    }

    public Criterion withFile() {
        this.withFile = true;
        return this;
    }

    public Criterion fresh() {
        this.fresh = true;
        return this;
    }

    public Criterion addBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public Criterion addColor(Color color) {
        this.colors.add(color);
        return this;
    }

    public Criterion addMarkId(Long markId) {
        this.getMarkId().add(markId);
        return this;
    }
}
