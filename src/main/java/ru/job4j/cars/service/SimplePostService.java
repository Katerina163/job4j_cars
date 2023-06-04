package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.repository.PostRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class SimplePostService implements PostService {
    private PostRepository repository;

    public SimplePostService(PostRepository hiberPostRepository) {
        repository = hiberPostRepository;
    }

    @Override
    public Collection<AutoPost> findAllNew() {
        return repository.findAllNew();
    }

    @Override
    public Collection<AutoPost> findAll() {
        return repository.findAll();
    }

    @Override
    public Collection<AutoPost> findWithFile() {
        return repository.findWithFile();
    }

    @Override
    public Collection<AutoPost> findCarBrand(String brand) {
        return repository.findCarBrand(brand);
    }

    @Override
    public Collection<AutoPost> findUsersCar(String login) {
        return repository.findUsersCar(login);
    }

    @Override
    public void add(AutoPost post) {
        repository.add(post);
    }

    @Override
    public void delete(AutoPost post) {
        repository.delete(post);
    }

    @Override
    public Optional<AutoPost> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public void update(AutoPost post) {
        repository.update(post);
    }

    @Override
    public void soldById(int id, boolean sold) {
        repository.soldById(id, sold);
    }
}
