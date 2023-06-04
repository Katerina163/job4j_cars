package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.OwnerRepository;

import java.util.Optional;

@Service
public class SimpleOwnerService implements OwnerService {
    private OwnerRepository repository;

    public SimpleOwnerService(OwnerRepository hiberOwnerRepository) {
        repository = hiberOwnerRepository;
    }

    @Override
    public Optional<Owner> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Owner> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Owner create(Owner owner) {
        return repository.create(owner);
    }

    @Override
    public void update(Owner owner) {
        repository.update(owner);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }
}
