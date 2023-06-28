package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.PriceHistoryRepository;

import java.util.Optional;

@Service
public class SimplePriceHistoryService implements PriceHistoryService {
    private final PriceHistoryRepository repository;

    public SimplePriceHistoryService(PriceHistoryRepository hiberPriceHistoryRepository) {
        repository = hiberPriceHistoryRepository;
    }

    @Override
    public Optional<PriceHistory> save(long price, long postId) {
        return repository.save(price, postId);
    }
}
