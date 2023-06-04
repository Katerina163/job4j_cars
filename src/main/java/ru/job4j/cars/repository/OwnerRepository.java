package ru.job4j.cars.repository;

import ru.job4j.cars.model.Owner;

import java.util.Optional;

public interface OwnerRepository {
    Optional<Owner> findById(int id);

    Optional<Owner> findByName(String name);

    Owner create(Owner owner);

    void update(Owner owner);

    void delete(int id);
}
