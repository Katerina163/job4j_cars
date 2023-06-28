package ru.job4j.cars.repository;

import ru.job4j.cars.model.PriceHistory;

import java.util.Optional;

public interface PriceHistoryRepository {
    Optional<PriceHistory> save(long price, long postId);
}
