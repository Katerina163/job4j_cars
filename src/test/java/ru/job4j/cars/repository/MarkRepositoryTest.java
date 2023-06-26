package ru.job4j.cars.repository;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.cars.utill.HibernateTestUtil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MarkRepositoryTest {
    private final MarkRepository repository = new HiberMarkRepository(HibernateTestUtil.buildSessionFactory());

    @Before
    public void before() {
        HibernateTestUtil.insertMarks();
    }

    @Test
    public void whenFindAll() {
        var list = repository.findAll();
        assertThat(list.get(0).getName(), is("Fiat"));
        assertThat(list.get(1).getName(), is("Lamborghini"));
        assertThat(list.get(2).getName(), is("Audi"));
    }

    @Test
    public void whenFindById() {
        assertThat(repository.findById(1L).get().getName(), is("Fiat"));
    }
}