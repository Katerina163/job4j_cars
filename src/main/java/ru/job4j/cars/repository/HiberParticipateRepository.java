package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@AllArgsConstructor
public class HiberParticipateRepository implements ParticipateRepository {
    private CrudRepository crudRepository;

    @Override
    public void create(int userId, int postId) {
        crudRepository.runSql(
                "insert into participates(user_id, post_id) values(:fUserId, :fPostId)",
                Map.of("fUserId", userId, "fPostId", postId));
    }

    @Override
    public void delete(int userId, int postId) {
        crudRepository.runSql(
                "delete from participates where post_id = :fPostId and user_id = :fUserId",
                Map.of("fPostId", postId, "fUserId", userId));
    }
}
