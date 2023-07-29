package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.PriceHistory;

import javax.persistence.LockModeType;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class HibernatePriceHistoryRepository implements PriceHistoryRepository {
    private final SessionFactory sf;

    @Override
    public Optional<PriceHistory> save(long price, long postId) {
        Transaction tr = null;
        PriceHistory priceHistory;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            var post = session.find(AutoPost.class, postId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            priceHistory = new PriceHistory(price);
            post.addPriceHistory(priceHistory);
            session.persist(priceHistory);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при сохранении цены {} в посте с id {}", price, postId);
            throw e;
        }
        return Optional.of(priceHistory);
    }
}
