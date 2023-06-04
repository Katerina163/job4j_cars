package ru.job4j.cars.service;

import ru.job4j.cars.model.Owner;

import java.util.Optional;

public interface OwnerService {
    Optional<Owner> findById(int id);

    Optional<Owner> findByName(String name);

    Owner create(Owner owner);

    void update(Owner owner);

    void delete(int id);
}
