package ru.job4j.cars.repository;

import org.junit.Test;
import ru.job4j.cars.model.Color;
import ru.job4j.cars.utill.HibernateTestUtil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CarRepositoryTest {
    private final CarRepository repository = new HiberCarRepository(HibernateTestUtil.buildSessionFactory());

    @Test
    public void whenFindById() {
        HibernateTestUtil.insertCars();
        var car = repository.findById(1L).get();
        assertThat(car.getName(), is("500"));
        assertThat(car.getOwners(), is("Иван Иванов"));
        assertThat(car.getColor(), is(Color.GREEN));
        assertThat(car.getMark().getName(), is("Fiat"));
    }
}