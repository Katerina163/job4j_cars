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
        String s = "from AutoPost where created between :fStart and :fEnd order by created";
        return crudRepository.query(s, AutoPost.class, Map.of(
                "fStart", Date.valueOf(LocalDate.now().minusDays(1L)),
                "fEnd", Date.valueOf(LocalDate.now())));
    }

    @Override
    public Collection<AutoPost> findWithFile() {
        return crudRepository.query(
                "from AutoPost a where size(a.files) >= 1", AutoPost.class);
    }

    @Override
    public Collection<AutoPost> findCarBrand(String brand) {
        return crudRepository.query(
                "from AutoPost a where a.car.name = :fBrand",
                AutoPost.class, Map.of("fBrand", brand));
    }

    @Override
    public Collection<AutoPost> findUsersCar(String login) {
        return crudRepository.query(
                "from AutoPost a where a.user.login = :fUser",
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
        return crudRepository.optional("from AutoPost where id = :fId",
                AutoPost.class, Map.of("fId", id));
    }
}
