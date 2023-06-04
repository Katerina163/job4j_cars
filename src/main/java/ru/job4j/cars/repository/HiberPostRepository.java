package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.AutoPost;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HiberPostRepository implements PostRepository {
    private CrudRepository crudRepository;

    @Override
    public Collection<AutoPost> findAllNew() {
        return crudRepository.query(
                getAutoPostQuery("where ap.created between :fStart and :fEnd order by ap.created"),
                AutoPost.class, Map.of(
                        "fStart", Date.valueOf(LocalDate.now().minusDays(1L)),
                        "fEnd", Date.valueOf(LocalDate.now())));
    }

    private String getAutoPostQuery(String condition) {
        return "from AutoPost ap left join fetch ap.priceHistories"
                + " left join fetch ap.car as car left join fetch ap.files "
                + condition;
    }

    @Override
    public Collection<AutoPost> findAll() {
        return crudRepository.query(
                getAutoPostQuery(""), AutoPost.class);
    }

    @Override
    public Collection<AutoPost> findWithFile() {
        return crudRepository.query(
                getAutoPostQuery("where size(ap.files) >= 1"), AutoPost.class);
    }

    @Override
    public Collection<AutoPost> findCarBrand(String brand) {
        return crudRepository.query(
                getAutoPostQuery("where car.name = :fBrand"),
                AutoPost.class, Map.of("fBrand", brand));
    }

    @Override
    public Collection<AutoPost> findUsersCar(String login) {
        return crudRepository.query(
                getAutoPostQuery("where ap.user.login = :fUser"),
                AutoPost.class, Map.of("fUser", login));
    }

    @Override
    public void add(AutoPost post) {
        crudRepository.run(session -> session.save(post));
    }

    @Override
    public void delete(AutoPost post) {
        crudRepository.run(session -> session.delete(post));
    }

    @Override
    public Optional<AutoPost> findById(int id) {
        String s = " left join fetch car.owners left join fetch ap.user"
                + " left join fetch car.engine where ap.id = :fId";
        return crudRepository.optional(
                getAutoPostQuery(s),
                AutoPost.class, Map.of("fId", id));
    }

    @Override
    public void update(AutoPost post) {
        crudRepository.run(session -> session.merge(post));
    }
}
