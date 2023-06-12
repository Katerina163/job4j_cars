package ru.job4j.cars.repository;

import org.junit.Test;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HiberUserRepositoryTest {
    private HiberUserRepository repository = new HiberUserRepository(new CrudRepository(
            new MetadataSources(
                    new StandardServiceRegistryBuilder()
                            .configure().build())
                    .buildMetadata().buildSessionFactory()));

    @Test
    public void whenCreateAndFindByLogin() {
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");
        repository.create(user);
        User result = repository.findByLogin(user.getLogin()).get();
        assertThat(result.getLogin(), is("login"));
        assertThat(result.getPassword(), is("password"));
    }

    @Test
    public void whenCreateAndFindByLoginAndPassword() {
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");
        repository.create(user);
        User result = repository.findByLoginAndPassword(user.getLogin(), user.getPassword()).get();
        assertThat(result.getLogin(), is("login"));
        assertThat(result.getPassword(), is("password"));
    }
}