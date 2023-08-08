package ru.job4j.cars.mapper;

/*
    При реализации нового маппера, добавить его в CompositeMapper
 */
public interface Mapper<F, T> {
    T convert(F object);
}