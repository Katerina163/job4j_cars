package ru.job4j.cars.repository;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Test;
import ru.job4j.cars.model.Engine;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EngineRepositoryTest {
    private EngineRepository repository = new EngineRepository(new CrudRepository(
            new MetadataSources(
                    new StandardServiceRegistryBuilder()
                            .configure().build())
                    .buildMetadata().buildSessionFactory()
    ));

    @Test
    public void whenCreateAndFindById() {
        Engine engine = new Engine();
        engine.setName("test");
        repository.create(engine);
        Engine result = repository.findById(engine.getId()).get();
        assertThat(result.getName(), is("test"));
    }

    @Test
    public void whenUpdateAndFindById() {
        Engine engine = new Engine();
        engine.setName("tist");
        repository.create(engine);
        engine.setName("test");
        repository.update(engine);
        Engine result = repository.findById(engine.getId()).get();
        assertThat(result.getName(), is("test"));
    }

    @Test
    public void whenDelete() {
        Engine engine = new Engine();
        engine.setName("test");
        repository.create(engine);
        repository.delete(engine.getId());
        var result = repository.findById(engine.getId());
        assertThat(result, is(Optional.empty()));
    }
}