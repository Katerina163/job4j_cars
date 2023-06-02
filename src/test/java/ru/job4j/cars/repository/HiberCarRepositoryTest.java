package ru.job4j.cars.repository;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HiberCarRepositoryTest {
    private Engine engine;
    private HiberCarRepository repository;

    public HiberCarRepositoryTest() {
        CrudRepository crud = new CrudRepository(
                new MetadataSources(
                        new StandardServiceRegistryBuilder()
                                .configure().build())
                        .buildMetadata().buildSessionFactory());
        HiberEngineRepository hiberEngineRepository = new HiberEngineRepository(crud);
        repository = new HiberCarRepository(crud);
        this.engine = new Engine();
        engine.setName("test");
        engine = hiberEngineRepository.create(engine);
    }

    @Test
    public void whenCreateAndFindById() {
        Car car = new Car();
        car.setEngine(engine);
        car.setName("car");
        repository.create(car);
        Car result = repository.findById(car.getId()).get();
        assertThat(result.getName(), is("car"));
        assertThat(result.getEngine(), is(engine));
    }

    @Test
    public void whenUpdateAndFindById() {
        Car car = new Car();
        car.setEngine(engine);
        car.setName("car");
        repository.create(car);
        car.setName("name");
        repository.update(car);
        Car result = repository.findById(car.getId()).get();
        assertThat(result.getName(), is("name"));
        assertThat(result.getEngine(), is(engine));
    }

    @Test
    public void whenDelete() {
        Car car = new Car();
        car.setEngine(engine);
        car.setName("car");
        repository.create(car);
        repository.delete(car.getId());
        var result = repository.findById(car.getId());
        assertThat(result, is(Optional.empty()));
    }
}