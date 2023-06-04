package ru.job4j.cars.repository;

import ru.job4j.cars.model.PriceHistory;

import java.util.Optional;

public interface PriceHistoryRepository {
    Optional<PriceHistory> findById(int id);

    PriceHistory create(PriceHistory priceHistory);

    void update(PriceHistory priceHistory);

    void delete(int id);
}