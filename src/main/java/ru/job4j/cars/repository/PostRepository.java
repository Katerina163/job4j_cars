package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.AutoPost;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
@Repository
public class PostRepository {
    private CrudRepository crudRepository;

    public Collection<AutoPost> findAllNew() {
        String s = "from AutoPost where created between :fStart and :fEnd order by created";
        return crudRepository.query(s, AutoPost.class, Map.of(
                "fStart", LocalDateTime.now().minusHours(24),
                "fEnd", LocalDateTime.now()));
    }

    public  Collection<AutoPost> findWithFile() {
        return crudRepository.query(
                "from AutoPost a join a.files where size(a.files) > 0", AutoPost.class);
    }

    public  Collection<AutoPost> findCarBrand(String brand) {
        return crudRepository.query(
                "from AutoPost a join a.car as c where c.name = :fBrand",
                AutoPost.class, Map.of("fBrand", brand));
    }
}
