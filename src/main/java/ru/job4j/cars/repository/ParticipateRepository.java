package ru.job4j.cars.repository;

public interface ParticipateRepository {
    void create(int userId, int postId);

    void delete(int userId, int postId);
}
