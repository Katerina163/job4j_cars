package ru.job4j.cars.repository;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Ignore;
import org.junit.Test;
import ru.job4j.cars.model.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HiberPostRepositoryTest {
    private Set<File> files;
    private HiberPostRepository repository;
    private User user;
    private Car car;
    private Car alterCar;

    public HiberPostRepositoryTest() {
        var sf = new MetadataSources(new StandardServiceRegistryBuilder()
                .configure().build()).buildMetadata().buildSessionFactory();
        CrudRepository crud = new CrudRepository(sf);
        repository = new HiberPostRepository(sf, crud);

        var fileRepository = new HiberFileRepository(crud);
        File file = new File();
        file.setName("name");
        file.setPath("/path");
        fileRepository.create(file);
        files = Set.of(file);

        var engineRepository = new HiberEngineRepository(crud);
        Engine engine = new Engine();
        engine.setName("test");
        engine = engineRepository.create(engine);

        var carRepository = new HiberCarRepository(crud);
        this.car = new Car();
        car.setEngine(engine);
        car.setName("car");
        carRepository.create(car);
        this.alterCar = new Car();
        alterCar.setEngine(engine);
        alterCar.setName("name");
        carRepository.create(alterCar);

        var userRepository = new HiberUserRepository(crud);
        this.user = new User();
        user.setLogin("login");
        user.setPassword("password");
        userRepository.create(user);
    }

    @Test
    public void whenAddAndFindAllNew() {
        AutoPost post1 = new AutoPost();
        post1.setDescription("description1");
        post1.setCreated(Date.valueOf(LocalDate.now()));
        post1.setUser(user);
        post1.setCar(car);
        repository.add(post1);
        AutoPost post2 = new AutoPost();
        post2.setDescription("description2");
        post2.setCreated(Date.valueOf(LocalDate.now()));
        post2.setUser(user);
        post2.setCar(car);
        repository.add(post2);
        AutoPost post3 = new AutoPost();
        post3.setDescription("description");
        post3.setCreated(Date.valueOf("1212-12-12"));
        post3.setUser(user);
        post3.setCar(car);
        repository.add(post3);
        assertThat(repository.findAllNew(), is(List.of(post1, post2)));
    }

    @Test
    @Ignore
    public void whenFindWithFile() {
        AutoPost post1 = new AutoPost();
        post1.setDescription("description");
        post1.setUser(user);
        post1.setCar(car);
        post1.setFiles(files);
        repository.add(post1);
        AutoPost post2 = new AutoPost();
        post2.setDescription("description2");
        post2.setCreated(Date.valueOf(LocalDate.now()));
        post2.setUser(user);
        post2.setCar(car);
        repository.add(post2);
        assertThat(repository.findWithFile(), is(List.of(post1)));
    }

    @Test
    public void whenFindCarBrand() {
        AutoPost post1 = new AutoPost();
        post1.setDescription("description");
        post1.setUser(user);
        post1.setCar(car);
        repository.add(post1);
        AutoPost post2 = new AutoPost();
        post2.setDescription("description2");
        post2.setCreated(Date.valueOf(LocalDate.now()));
        post2.setUser(user);
        post2.setCar(alterCar);
        repository.add(post2);
        assertThat(repository.findCarBrand("car"), is(List.of(post1)));
    }

    @Test
    public void whenFindUsersCars() {
        AutoPost post1 = new AutoPost();
        post1.setDescription("description");
        post1.setUser(user);
        post1.setCar(car);
        repository.add(post1);
        AutoPost post2 = new AutoPost();
        post2.setDescription("description2");
        post2.setCreated(Date.valueOf(LocalDate.now()));
        post2.setUser(user);
        post2.setCar(alterCar);
        repository.add(post2);
        assertThat(repository.findUsersCar(user.getLogin()), is(List.of(post1, post2)));
    }

    @Test
    public void whenAddThenDelete() {
        AutoPost post1 = new AutoPost();
        post1.setDescription("description");
        post1.setUser(user);
        post1.setCar(car);
        repository.add(post1);
        repository.delete(post1);
        assertThat(repository.findById(post1.getId()), is(Optional.empty()));
    }
}