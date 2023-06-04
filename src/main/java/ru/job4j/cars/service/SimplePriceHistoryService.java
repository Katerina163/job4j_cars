package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.PriceHistoryRepository;

import java.util.Optional;

@Service
public class SimplePriceHistoryService implements PriceHistoryService {
    private PriceHistoryRepository repository;

    public SimplePriceHistoryService(PriceHistoryRepository hiberPriceHistoryRepository) {
        repository = hiberPriceHistoryRepository;
    }

    @Override
    public Optional<PriceHistory> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public PriceHistory create(PriceHistory priceHistory) {
        return repository.create(priceHistory);
    }

    @Override
    public void update(PriceHistory priceHistory) {
        repository.update(priceHistory);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }
}
