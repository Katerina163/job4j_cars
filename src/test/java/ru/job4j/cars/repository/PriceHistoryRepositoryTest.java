package ru.job4j.cars.repository;

import org.junit.Test;
import ru.job4j.cars.utill.HibernateTestUtil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PriceHistoryRepositoryTest {
    private final PriceHistoryRepository repository;
    private final AutoPostRepository postRepository;

    public PriceHistoryRepositoryTest() {
        var sf = HibernateTestUtil.buildSessionFactory();
        repository = new HiberPriceHistoryRepository(sf);
        postRepository = new HiberAutoPostRepository(sf);
    }

    @Test
    public void whenCreate() {
        HibernateTestUtil.insertPosts();
        repository.save(200, 1L);
        var postRes = postRepository.findById(1L).get();
        var result = postRes.getHistory().last();
        assertThat(result.getPrice(), is(200L));
    }
}