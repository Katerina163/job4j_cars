package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.PriceHistory;

@Repository
@AllArgsConstructor
public class HiberPriceHistoryRepository implements PriceHistoryRepository {
    private final CrudRepository crud;

    @Override
    public PriceHistory save(PriceHistory priceHistory) {
        crud.run(session -> session.save(priceHistory));
        return priceHistory;
    }
}
