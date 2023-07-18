package ru.job4j.cars.repository;

import com.querydsl.core.Tuple;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.cars.dto.QPredicate;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Color;
import ru.job4j.cars.utill.HibernateTestUtil;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.job4j.cars.model.QAutoPost.autoPost;
import static ru.job4j.cars.model.QCar.car;
import static ru.job4j.cars.model.QPriceHistory.priceHistory;

public class AutoPostRepositoryTest {
    private final AutoPostRepository repository;
    private final MarkRepository markRepository;
    private final UserRepository userRepository;

    public AutoPostRepositoryTest() {
        var sf = HibernateTestUtil.buildSessionFactory();
        repository = new HiberAutoPostRepository(sf);
        markRepository = new HiberMarkRepository(sf);
        userRepository = new HiberUserRepository(sf);
    }

    @Before
    public void before() {
        HibernateTestUtil.insertPosts();
        HibernateTestUtil.insertFiles();
        HibernateTestUtil.insertPrice();
    }

    @Test
    public void whenFindAll() {
        var result = repository.findWithPredicate(QPredicate.builder().and(), 10L, 0L);
        assertThat(result.size(), is(3));
        Tuple post = null;
        for (var p : result) {
            post = p;
            break;
        }
        carIsFiat(post);
    }

    @Test
    public void whenFindWithFile() {
        var result = repository.findWithPredicate(QPredicate.builder()
                .addPredicate(1, autoPost.files.size()::goe)
                .and(), 10L, 0L);
        assertThat(result.size(), is(1));
        Tuple post = null;
        for (var p : result) {
            post = p;
        }
        carIsAudi(post);
    }

    @Test
    public void whenFindAllNew() {
        var result = repository.findWithPredicate(
                QPredicate.builder()
                        .addBiPredicate(LocalDateTime.now().minusDays(1L), LocalDateTime.now(), autoPost.created::between)
                        .and(), 10L, 0L);
        assertThat(result.size(), is(2));
        Tuple post = null;
        for (var p : result) {
            post = p;
        }
        carIsAudi(post);
    }

    @Test
    public void whenFindByCarBrand() {
        var result = repository.findWithPredicate(
                QPredicate.builder()
                        .addPredicate("500", autoPost.car.name::eq)
                        .and(), 10L, 0L);
        assertThat(result.size(), is(1));
        Tuple post = null;
        for (var p : result) {
            post = p;
        }
        carIsFiat(post);
    }

    @Test
    public void whenFindByColor() {
        var result = repository.findWithPredicate(
                QPredicate.builder()
                        .addPredicate(Color.RED, autoPost.car.color::eq)
                        .and(), 10L, 0L);
        assertThat(result.size(), is(1));
        Tuple post = null;
        for (var p : result) {
            post = p;
        }
        carIsLamborghini(post);
    }

    @Test
    public void whenFindByMark() {
        var result = repository.findWithPredicate(QPredicate.builder()
                .addPredicate(1L, autoPost.car.mark.id::eq)
                .and(), 10L, 0L);
        assertThat(result.size(), is(1));
        Tuple post = null;
        for (var p : result) {
            post = p;
        }
        carIsFiat(post);
    }

    @Test
    public void whenFindById() {
        var result = repository.findById(2L).get();
        assertThat(result.getFiles().size(), is(0));
        assertThat(result.getHistory().size(), is(1));
        assertThat(result.getDescription(), is("Lamborghini description"));
        assertThat(result.getCreated().getDayOfWeek(), is(LocalDateTime.now().minusDays(10).getDayOfWeek()));
        assertThat(result.isSold(), is(false));
        assertThat(result.getCar().getName(), is("Huracán Evo"));
        assertThat(result.getCar().getOwners(), is("Александра Александровна"));
        assertThat(result.getCar().getColor(), is(Color.RED));
        assertThat(result.getCar().getMark().getName(), is("Lamborghini"));
        assertThat(result.getAuthor().getLogin(), is("Petrov"));
        assertThat(result.getHistory().last().getPrice(), is(16500000L));

    }

    @Test
    public void whenSaveAndFindById() {
        var mark = markRepository.findById(1L).get();
        var author = userRepository.findByLoginWithPosts("Sidorov").get();
        var post = AutoPost.builder()
                .description("description")
                .created(LocalDateTime.now())
                .sold(true)
                .author(author)
                .car(new Car("car name", "owners", Color.BLACK, mark))
                .build();
        repository.cud(post, session -> session.persist(post));
        var result = repository.findById(4L).get();
        assertThat(result.getDescription(), is("description"));
        assertThat(result.getCreated().getDayOfWeek(), is(LocalDateTime.now().getDayOfWeek()));
        assertThat(result.isSold(), is(true));
        assertThat(result.getCar().getName(), is("car name"));
        assertThat(result.getCar().getOwners(), is("owners"));
        assertThat(result.getCar().getColor(), is(Color.BLACK));
        assertThat(result.getCar().getMark().getName(), is("Fiat"));
        assertThat(result.getAuthor().getLogin(), is("Sidorov"));
    }

    @Test
    public void whenSoldAndFind() {
        var post = repository.findById(1L).get();
        repository.soldById(post.getId(), !post.isSold());
        var result = repository.findById(1L).get();
        assertThat(result.isSold(), is(!post.isSold()));
    }

    @Test
    public void whenDeleteAndFindNothing() {
        var post = repository.findById(1L).get();
        repository.cud(post, session -> session.delete(post));
        assertThat(repository.findById(1L), is(Optional.empty()));
    }

    private void carIsFiat(Tuple tuple) {
        assertThat(tuple.get(autoPost.created).getDayOfWeek(), is(LocalDateTime.now().getDayOfWeek()));
        assertThat(tuple.get(car.name), is("500"));
        assertThat(tuple.get(car.mark.name), is("Fiat"));
        assertThat(tuple.get(priceHistory.price), is(450000L));
    }

    private void carIsAudi(Tuple tuple) {
        assertThat(tuple.get(autoPost.created).getDayOfWeek(), is(LocalDateTime.now().getDayOfWeek()));
        assertThat(tuple.get(car.name), is("RS 5"));
        assertThat(tuple.get(car.mark.name), is("Audi"));
        assertThat(tuple.get(priceHistory.price), is(5120000L));
    }

    private void carIsLamborghini(Tuple tuple) {
        assertThat(tuple.get(autoPost.created).getDayOfWeek(), is(LocalDateTime.now().minusDays(10).getDayOfWeek()));
        assertThat(tuple.get(car.name), is("Huracán Evo"));
        assertThat(tuple.get(car.mark.name), is("Lamborghini"));
        assertThat(tuple.get(priceHistory.price), is(16500000L));
    }
}