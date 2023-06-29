package ru.job4j.cars.repository;

import com.querydsl.core.types.Predicate;
import org.hibernate.Session;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.model.AutoPost;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

public interface AutoPostRepository {
    Collection<Banner> findWithPredicate(Predicate predicate);

    Optional<AutoPost> findById(long id);

    void soldById(long postId, boolean sold);

    void cud(AutoPost post, Consumer<Session> function);
}
