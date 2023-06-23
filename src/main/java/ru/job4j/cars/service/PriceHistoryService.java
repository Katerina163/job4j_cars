package ru.job4j.cars.service;

import ru.job4j.cars.model.PriceHistory;

import java.util.Optional;

public interface PriceHistoryService {
    Optional<PriceHistory> create(long before, long after, long postId);
}
