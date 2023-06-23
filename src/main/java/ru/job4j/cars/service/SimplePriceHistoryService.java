package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.AutoPostRepository;
import ru.job4j.cars.repository.PriceHistoryRepository;

import java.util.Optional;

@Service
public class SimplePriceHistoryService implements PriceHistoryService {
    private final AutoPostRepository postRepository;
    private final PriceHistoryRepository repository;

    public SimplePriceHistoryService(AutoPostRepository hiberAutoPostRepository,
                                     PriceHistoryRepository hiberPriceHistoryRepository) {
        postRepository = hiberAutoPostRepository;
        repository = hiberPriceHistoryRepository;
    }

    @Override
    public Optional<PriceHistory> create(long before, long after, long postId) {
        var result = new PriceHistory(before, after);
        var postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            return Optional.empty();
        }
        postOpt.get().addPriceHistory(result);
        return Optional.of(repository.create(result));
    }
}
