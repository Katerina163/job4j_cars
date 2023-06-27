package ru.job4j.cars.repository;

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
        var result = repository.findWithPredicate(QPredicate.builder().and());
        assertThat(result.size(), is(3));
        var post = new AutoPost();
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
                .and());
        assertThat(result.size(), is(1));
        var post = new AutoPost();
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
                        .and());
        assertThat(result.size(), is(2));
        var post = new AutoPost();
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
                        .and());
        assertThat(result.size(), is(1));
        var post = new AutoPost();
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
                        .and());
        assertThat(result.size(), is(1));
        var post = new AutoPost();
        for (var p : result) {
            post = p;
        }
        carIsLamborghini(post);
    }

    @Test
    public void whenFindByMark() {
        var result = repository.findWithPredicate(QPredicate.builder()
                .addPredicate(1L, autoPost.car.mark.id::eq)
                .and());
        assertThat(result.size(), is(1));
        var post = new AutoPost();
        for (var p : result) {
            post = p;
        }
        carIsFiat(post);
    }

    @Test
    public void whenFindById() {
        var result = repository.findById(2L).get();
        carIsLamborghini(result);
    }

    @Test
    public void whenSaveAndFindById() {
        var mark = markRepository.findById(1L).get();
        var author = userRepository.findByLogin("Sidorov").get();
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
        repository.soldById(post.getId());
        var result = repository.findById(1L).get();
        assertThat(result.isSold(), is(!post.isSold()));
    }

    @Test
    public void whenDeleteAndFindNothing() {
        var post = repository.findById(1L).get();
        repository.cud(post, session -> session.delete(post));
        assertThat(repository.findById(1L), is(Optional.empty()));
    }

    private void carIsFiat(AutoPost post) {
        assertThat(post.getFiles().size(), is(0));
        assertThat(post.getHistory().size(), is(1));
        assertThat(post.getCreated().getDayOfWeek(), is(LocalDateTime.now().getDayOfWeek()));
        assertThat(post.getCar().getName(), is("500"));
        assertThat(post.getCar().getColor(), is(Color.GREEN));
        assertThat(post.getCar().getMark().getName(), is("Fiat"));
        assertThat(post.getHistory().last().getPrice(), is(450000L));
    }

    private void carIsAudi(AutoPost post) {
        assertThat(post.getFiles().size(), is(1));
        assertThat(post.getFiles().last().getName(), is("name"));
        assertThat(post.getDescription(), is("Audi description"));
        assertThat(post.getCreated().getDayOfWeek(), is(LocalDateTime.now().getDayOfWeek()));
        assertThat(post.isSold(), is(true));
        assertThat(post.getCar().getName(), is("RS 5"));
        assertThat(post.getCar().getOwners(), is("Андрей Андреевич, Василий Васильевич"));
        assertThat(post.getCar().getColor(), is(Color.YELLOW));
        assertThat(post.getCar().getMark().getName(), is("Audi"));
        assertThat(post.getAuthor().getLogin(), is("Petrov"));
        assertThat(post.getHistory().last().getPrice(), is(5120000L));
    }

    private void carIsLamborghini(AutoPost post) {
        assertThat(post.getFiles().size(), is(0));
        assertThat(post.getHistory().size(), is(1));
        assertThat(post.getDescription(), is("Lamborghini description"));
        assertThat(post.getCreated().getDayOfWeek(), is(LocalDateTime.now().minusDays(10).getDayOfWeek()));
        assertThat(post.isSold(), is(false));
        assertThat(post.getCar().getName(), is("Huracán Evo"));
        assertThat(post.getCar().getOwners(), is("Александра Александровна"));
        assertThat(post.getCar().getColor(), is(Color.RED));
        assertThat(post.getCar().getMark().getName(), is("Lamborghini"));
        assertThat(post.getAuthor().getLogin(), is("Petrov"));
        assertThat(post.getHistory().last().getPrice(), is(16500000L));
    }
}