package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.PriceHistoryRepository;

import java.util.Date;

@Service
public class SimplePriceHistoryService implements PriceHistoryService {
    private final PriceHistoryRepository repository;

    public SimplePriceHistoryService(PriceHistoryRepository hiberPriceHistoryRepository) {
        repository = hiberPriceHistoryRepository;
    }

    @Override
    public PriceHistory create(long before, long after, long postId) {
        var result = new PriceHistory();
        result.setCreated(new Date());
        result.setBefore(before);
        result.setAfter(after);
        result.setPost(new AutoPost(postId));
        return repository.create(result);
    }
}
