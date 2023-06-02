package ru.job4j.cars.repository;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Test;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HiberOwnerRepositoryTest {
    private HiberOwnerRepository repository;
    private User user;

    public HiberOwnerRepositoryTest() {
        CrudRepository crud = new CrudRepository(
                new MetadataSources(
                        new StandardServiceRegistryBuilder()
                                .configure().build())
                        .buildMetadata().buildSessionFactory());
        repository = new HiberOwnerRepository(crud);

        var userRepository = new HiberUserRepository(crud);
        this.user = new User();
        user.setLogin("login");
        user.setPassword("password");
        userRepository.create(user);
    }

    @Test
    public void whenCreateAndFindById() {
        Owner owner = new Owner();
        owner.setName("test");
        owner.setUser(user);
        repository.create(owner);
        var result = repository.findById(owner.getId()).get();
        assertThat(result.getName(), is("test"));
    }

    @Test
    public void whenUpdateAndFindById() {
        Owner owner = new Owner();
        owner.setName("tist");
        owner.setUser(user);
        repository.create(owner);
        owner.setName("test");
        repository.update(owner);
        var result = repository.findById(owner.getId()).get();
        assertThat(result.getName(), is("test"));
    }

    @Test
    public void whenDelete() {
        Owner owner = new Owner();
        owner.setName("test");
        owner.setUser(user);
        repository.create(owner);
        repository.delete(owner.getId());
        var result = repository.findById(owner.getId());
        assertThat(result, is(Optional.empty()));
    }
}