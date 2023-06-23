package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.AutoPost;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

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
                + " left join fetch ap.car as car left join fetch ap.files"
                + " left join fetch car.mark left join fetch car.color";
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
                        "start", Date.valueOf(LocalDate.now().minusDays(1L)),
                        "end", Date.valueOf(LocalDate.now())));
    }

    @Override
    public Collection<AutoPost> findByColor(long id) {
        return crud.query(
                getSqlQuery() + " where car.color.id = :id",
                AutoPost.class, Map.of("id", id));
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
        String sql = " left join fetch ap.user left join fetch car.owners left join fetch car.mark left join"
                + " fetch car.color where ap.id = :id";
        return crud.optional(getSqlQuery() + sql,
                AutoPost.class, Map.of("id", id));
    }

    @Override
    public void create(AutoPost post) {
        crud.run(session -> session.save(post));
    }

    @Override
    public void soldById(long postId, boolean sold) {
        crud.run("update AutoPost set sold = :sold where id = :id",
                Map.of("sold", sold, "id", postId));
    }

    @Override
    public void delete(long id) {
        crud.run(session -> session.remove(new AutoPost(id)));
    }

    @Override
    public void modify(AutoPost post) {
       crud.run(session -> session.update(post));
    }
}
