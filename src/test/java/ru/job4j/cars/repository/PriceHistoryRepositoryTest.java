package ru.job4j.cars.repository;

import org.junit.Test;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.utill.HibernateTestUtil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PriceHistoryRepositoryTest {
    private final PriceHistoryRepository repository;
    private final AutoPostRepository postRepository;

    public PriceHistoryRepositoryTest() {
        var crud = new CrudRepository(HibernateTestUtil.buildSessionFactory());
        repository = new HiberPriceHistoryRepository(crud);
        postRepository = new HiberAutoPostRepository(crud);
    }

    @Test
    public void whenCreate() {
        HibernateTestUtil.insertPosts();
        var post = postRepository.findById(1L).get();
        var price = new PriceHistory(200);
        post.addPriceHistory(price);
        repository.save(price);
        var postRes = postRepository.findById(1L).get();
        var result = postRes.getHistory().last();
        assertThat(result.getPrice(), is(200L));
    }
}