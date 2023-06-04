package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.PriceHistory;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HiberPriceHistoryRepository implements PriceHistoryRepository {
    private CrudRepository crudRepository;

    @Override
    public Optional<PriceHistory> findById(int id) {
        return crudRepository.optional(
                "from PriceHistory where id = :fId", PriceHistory.class, Map.of("fId", id));
    }

    @Override
    public PriceHistory create(PriceHistory priceHistory) {
        crudRepository.run(session -> session.save(priceHistory));
        return priceHistory;
    }

    @Override
    public void update(PriceHistory priceHistory) {
        crudRepository.run(session -> session.update(priceHistory));
    }

    @Override
    public void delete(int id) {
        crudRepository.run(
                "delete from PriceHistory where id = :fId",
                Map.of("fId", id)
        );
    }
}
