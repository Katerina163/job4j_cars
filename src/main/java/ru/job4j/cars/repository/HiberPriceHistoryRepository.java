package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.PriceHistory;

@Slf4j
@Repository
@AllArgsConstructor
public class HiberPriceHistoryRepository implements PriceHistoryRepository {
    private final SessionFactory sf;

    @Override
    public PriceHistory save(PriceHistory priceHistory) {
        Transaction tr = null;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            session.persist(priceHistory);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при сохранении цены {}", priceHistory);
            throw e;
        }
        return priceHistory;
    }
}
