package ru.job4j.cars.repository;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Test;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HiberUserRepositoryTest {
    private HiberUserRepository repository = new HiberUserRepository(new CrudRepository(
            new MetadataSources(
                    new StandardServiceRegistryBuilder()
                            .configure().build())
                    .buildMetadata().buildSessionFactory()
    ));

    @Test
    public void whenCreateAndFindById() {
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");
        repository.create(user);
        User result = repository.findById(user.getId()).get();
        assertThat(result.getLogin(), is("login"));
        assertThat(result.getPassword(), is("password"));
    }

    @Test
    public void whenUpdateAndFindById() {
        User user = new User();
        user.setLogin("lagin");
        user.setPassword("possword");
        repository.create(user);
        user.setLogin("login");
        user.setPassword("password");
        repository.update(user);
        User result = repository.findById(user.getId()).get();
        assertThat(result.getLogin(), is("login"));
        assertThat(result.getPassword(), is("password"));
    }

    @Test
    public void whenDelete() {
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");
        repository.create(user);
        repository.delete(user.getId());
        var result = repository.findById(user.getId());
        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void whenFindAllOrderById() {
        User user1 = new User();
        user1.setLogin("login");
        user1.setPassword("password");
        User user2 = new User();
        user2.setLogin("loggin");
        user2.setPassword("passwword");
        repository.create(user1);
        repository.create(user2);
        assertThat(repository.findAllOrderById(), is(List.of(user1, user2)));
    }

    @Test
    public void whenFindByLikeLogin() {
        User user1 = new User();
        user1.setLogin("login");
        user1.setPassword("password");
        User user2 = new User();
        user2.setLogin("loggin");
        user2.setPassword("passwword");
        User user3 = new User();
        user3.setLogin("no");
        user3.setPassword("no");
        repository.create(user1);
        repository.create(user2);
        repository.create(user3);
        assertThat(repository.findByLikeLogin("log"), is(List.of(user1, user2)));
    }

    @Test
    public void whenFindByLogin() {
        User user1 = new User();
        user1.setLogin("login");
        user1.setPassword("password");
        User user2 = new User();
        user2.setLogin("loggin");
        user2.setPassword("passwword");
        repository.create(user1);
        repository.create(user2);
        assertThat(repository.findByLogin("login").get(), is(user1));
    }
}