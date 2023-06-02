package ru.job4j.cars.repository;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Test;
import ru.job4j.cars.model.File;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HiberFileRepositoryTest {
    private HiberFileRepository repository = new HiberFileRepository(new CrudRepository(
            new MetadataSources(
                    new StandardServiceRegistryBuilder()
                            .configure().build())
                    .buildMetadata().buildSessionFactory()
    ));

    @Test
    public void whenCreateAndFindById() {
        File file = new File();
        file.setName("name");
        file.setPath("/path");
        repository.create(file);
        var result = repository.findById(file.getId()).get();
        assertThat(result.getName(), is("name"));
        assertThat(result.getPath(), is("/path"));
    }

    @Test
    public void whenUpdateAndFindById() {
        File file = new File();
        file.setName("name");
        file.setPath("/path");
        repository.create(file);
        file.setName("test");
        repository.update(file);
        var result = repository.findById(file.getId()).get();
        assertThat(result.getName(), is("test"));
    }

    @Test
    public void whenDelete() {
        File file = new File();
        file.setName("name");
        file.setPath("/path");
        repository.create(file);
        repository.delete(file.getId());
        var result = repository.findById(file.getId());
        assertThat(result, is(Optional.empty()));
    }
}