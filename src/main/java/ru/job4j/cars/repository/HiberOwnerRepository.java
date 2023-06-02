package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HiberOwnerRepository implements OwnerRepository {
    private CrudRepository crudRepository;

    @Override
    public Optional<Owner> findById(int id) {
        return crudRepository.optional(
                "from Owner o left join fetch o.cars where o.id = :fId",
                Owner.class, Map.of("fId", id));
    }

    @Override
    public Owner create(Owner owner) {
        crudRepository.run(session -> session.save(owner));
        return owner;
    }

    @Override
    public void update(Owner owner) {
        crudRepository.run(session -> session.merge(owner));
    }

    @Override
    public void delete(int id) {
        crudRepository.run(
                "delete from Owner where id = :fId",
                Map.of("fId", id)
        );
    }
}
