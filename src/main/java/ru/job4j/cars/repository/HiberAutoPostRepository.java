package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.Color;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HiberAutoPostRepository implements AutoPostRepository {
    private final CrudRepository crud;

    @Override
    public Collection<AutoPost> findAll() {
        return crud.query(getSqlQuery(), AutoPost.class);
    }

    private String getSqlQuery() {
        return "select distinct ap from AutoPost ap left join fetch ap.history"
                + " left join fetch ap.files left join fetch ap.car as car";
    }

    @Override
    public Collection<AutoPost> findWithFile() {
        return crud.query(getSqlQuery() + " where size(ap.files) >= 1", AutoPost.class);
    }

    @Override
    public Collection<AutoPost> findAllNew() {
        return crud.query(
                getSqlQuery() + " where ap.created between :start and :end order by ap.created",
                AutoPost.class, Map.of(
                        "start", LocalDateTime.now().minusDays(1L),
                        "end", LocalDateTime.now()));
    }

    @Override
    public Collection<AutoPost> findByColor(Color color) {
        return crud.query(
                getSqlQuery() + " where car.color = :color",
                AutoPost.class, Map.of("color", color));
    }

    @Override
    public Collection<AutoPost> findByMark(long id) {
        return crud.query(
                getSqlQuery() + " where car.mark.id = :id",
                AutoPost.class, Map.of("id", id));
    }

    @Override
    public Collection<AutoPost> findByCarBrand(String brand) {
        return crud.query(
                getSqlQuery() + " where car.name = :brand",
                AutoPost.class, Map.of("brand", brand));
    }

    @Override
    public Optional<AutoPost> findById(long id) {
        return crud.optional(getSqlQuery() + " where ap.id = :id",
                AutoPost.class, Map.of("id", id));
    }

    @Override
    public void save(AutoPost post) {
        crud.run(session -> session.saveOrUpdate(post));
    }

    @Override
    public void soldById(long postId, boolean sold) {
        crud.run("update AutoPost set sold = :sold where id = :id",
                Map.of("sold", sold, "id", postId));
    }

    @Override
    public void delete(AutoPost post) {
        crud.run(session -> session.delete(post));
    }
}
