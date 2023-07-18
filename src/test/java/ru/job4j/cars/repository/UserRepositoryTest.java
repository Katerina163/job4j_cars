package ru.job4j.cars.repository;

import com.querydsl.core.Tuple;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.cars.model.User;
import ru.job4j.cars.utill.HibernateTestUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.job4j.cars.model.QAutoPost.autoPost;
import static ru.job4j.cars.model.QCar.car;
import static ru.job4j.cars.model.QFile.file;

public class UserRepositoryTest {
    private final UserRepository repository;
    private final
    AutoPostRepository postRepository;

    public UserRepositoryTest() {
        var sf = HibernateTestUtil.buildSessionFactory();
        repository = new HiberUserRepository(sf);
        postRepository = new HiberAutoPostRepository(sf);
    }

    @Before
    public void before() {
        HibernateTestUtil.insertPosts();
    }

    private Collection<Tuple> whenFindByLogin(Function<String, Collection<Tuple>> function) {
        HibernateTestUtil.insertParticipates();
        HibernateTestUtil.insertPrice();
        HibernateTestUtil.insertFiles();
        var result = function.apply("Ivanov");
        assertThat(result.size(), is(1));
        return result;
    }

    @Test
    public void whenParticipatesFind() {
        var result = whenFindByLogin(repository::findParticipatesByLogin);
        for (var post : result) {
            assertThat(post.get(autoPost.id), is(3L));
            assertThat(post.get(autoPost.created).getDayOfWeek(), is(LocalDate.now().getDayOfWeek()));
            assertThat(post.get(car.name), is("RS 5"));
            assertThat(post.get(car.mark.name), is("Audi"));
            assertThat(post.get(file.id), is(1L));
        }
    }

    @Test
    public void whenUsersPostFind() {
        var result = whenFindByLogin(repository::findUsersPostByLogin);
        for (var post : result) {
            assertThat(post.get(autoPost.id), is(1L));
            assertThat(post.get(autoPost.created).getDayOfWeek(), is(LocalDate.now().getDayOfWeek()));
            assertThat(post.get(car.name), is("500"));
            assertThat(post.get(car.mark.name), is("Fiat"));
            assertNull(post.get(file.id));
        }
    }

    @Test
    public void whenFindByLoginWithUsersPosts() {
        HibernateTestUtil.insertParticipates();
        HibernateTestUtil.insertPrice();
        HibernateTestUtil.insertFiles();
        User result = repository.findByLoginWithPosts("Ivanov").get();
        assertThat(result.getLogin(), is("Ivanov"));
        assertThat(result.getPassword(), is("root"));
        assertThat(result.getUserPosts().size(), is(1));
        for (var post : result.getUserPosts()) {
            assertThat(post.getDescription(), is("Fiat description"));
            assertFalse(post.isSold());
            assertThat(post.getCreated().getDayOfWeek(), is(LocalDateTime.now().getDayOfWeek()));
            assertThat(post.getVersion(), is(1));
            assertThat(post.getId(), is(1L));
        }
    }

    @Test
    public void whenFindByLoginAndPassword() {
        HibernateTestUtil.insertParticipates();
        User result = repository.findByLoginAndPassword("Ivanov", "root").get();
        assertThat(result.getLogin(), is("Ivanov"));
        assertThat(result.getPassword(), is("root"));
    }

    @Test
    public void whenCreateAndFindByLoginAndPassword() {
        User user = new User("login", "password");
        repository.create(user);
        User result = repository.findByLoginAndPassword(user.getLogin(), user.getPassword()).get();
        assertThat(result.getLogin(), is("login"));
        assertThat(result.getPassword(), is("password"));
    }

    @Test
    public void whenUserSubscribe() {
        var user = new User("login", "password");
        user = repository.create(user).get();
        var post = postRepository.findById(1).get();
        repository.participate(user.getId(), post.getId(), List::add);
        var result = repository.findParticipatesByLogin("login");
        assertThat(result.size(), is(1));
    }
}