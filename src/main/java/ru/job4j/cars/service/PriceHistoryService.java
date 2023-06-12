package ru.job4j.cars.service;

import ru.job4j.cars.model.PriceHistory;

public interface PriceHistoryService {
    PriceHistory create(long before, long after, long postId);
}
