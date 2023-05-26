package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class OwnerRepository {
    private CrudRepository crudRepository;

    public Optional<Owner> findById(int id) {
        return crudRepository.optional(
                "from Owner o left join fetch o.cars where id = :fId",
                Owner.class, Map.of("fId", id));
    }

    public Owner create(Owner owner) {
        crudRepository.run(session -> session.save(owner));
        return owner;
    }

    public void update(Owner owner) {
        crudRepository.run(session -> session.merge(owner));
    }

    public void delete(int id) {
        crudRepository.run(
                "delete from Owner where id = :fId",
                Map.of("fId", id)
        );
    }
}
