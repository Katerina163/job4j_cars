package ru.job4j.cars.repository;

import ru.job4j.cars.model.PriceHistory;

public interface PriceHistoryRepository {
    PriceHistory create(PriceHistory priceHistory);
}
