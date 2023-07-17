package ru.job4j.cars.service;

import ru.job4j.cars.dto.PriceDto;
import ru.job4j.cars.model.PriceHistory;

import java.util.Optional;

public interface PriceHistoryService {
    Optional<PriceHistory> save(PriceDto dto);
}
