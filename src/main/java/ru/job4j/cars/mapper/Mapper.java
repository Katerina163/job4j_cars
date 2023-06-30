package ru.job4j.cars.mapper;

public interface Mapper<F, T> {
    T convert(F object);
}