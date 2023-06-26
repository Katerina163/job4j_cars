package ru.job4j.cars.repository;

import com.querydsl.core.types.Predicate;
import org.hibernate.Session;
import ru.job4j.cars.model.AutoPost;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

public interface AutoPostRepository {
    Collection<AutoPost> findWithPredicate(Predicate predicate);

    Optional<AutoPost> findById(long id);

    void soldById(long postId);

    void cud(AutoPost post, Consumer<Session> function);
}
