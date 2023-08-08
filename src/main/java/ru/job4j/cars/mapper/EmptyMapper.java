package ru.job4j.cars.mapper;

public class EmptyMapper implements Mapper {
    @Override
    public Object convert(Object object) {
        return null;
    }
}
